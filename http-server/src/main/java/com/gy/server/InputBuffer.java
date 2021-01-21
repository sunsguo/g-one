package com.gy.server;

import com.gy.core.util.LogFactory;
import com.gy.core.util.StopWatch;
import com.gy.core.util.StringUtil;
import com.gy.server.exception.BadRequest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gy.core.util.ArrayUtil.indexOf;
import static com.gy.server.RequestUtil.isMultipart;

/**
 * 套接字输入流缓冲区，添加读取一行功能
 */
public class InputBuffer {

    Logger log = LogFactory.getLogger();

    /**
     * 默认缓冲区大小
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static final byte[] DELIMITER = Constants.CRLF.getBytes(StandardCharsets.UTF_8);

    private final InputStream inputStream;

    private final byte[] buffer;

    private boolean needRead = true;

    private boolean closed = false;

    private Request request;

    private boolean parsed = false;

    /**
     * 可读开始位置
     */
    private int position;

    /**
     * 可读结束位置(不包括)
     */
    private int limit;

    /**
     * 容量
     */
    private int capacity;

    public InputBuffer(InputStream inputStream) {
        this(inputStream, DEFAULT_BUFFER_SIZE);
    }

    public InputBuffer(InputStream inputStream, int defaultBufferSize) {
        this.inputStream = inputStream;
        this.capacity = defaultBufferSize;

        buffer = new byte[defaultBufferSize];
        request = new Request();

        // parse();
    }

    /**
     * 解析请求
     */
    private void parse() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        parseRequestLine();
        parseHeaders();
        parseBody();
        parsed = true;

        stopWatch.stop();
        log.info(String.format("解析请求 %s 耗费时间：%sms", request.getPath(), stopWatch.getTotalTime()));
    }

    /**
     * 解析请求行
     */
    public void parseRequestLine() {
        String line;

        line = readLine();

        if (line == null)
            throw new BadRequest("连接已经关闭");

        String reg = "^(?<method>[a-zA-Z]+)\\s+(?<path>/([a-zA-Z0-9_\\-/.]+))(\\?(?<query>.+))?\\s+(?<protocol>(http|HTTP)/[0-9.]+)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(line);

        if (!matcher.find()) throw new BadRequest("错误的请求行格式");

        String protocol = matcher.group("protocol");
        String method = matcher.group("method");
        String path = matcher.group("path");
        String query = matcher.group("query");

        this.request.setProtocol(protocol).setMethod(method).setPath(path).setQuery(query);
    }

    /**
     * 解析请求头
     */
    private void parseHeaders() {
        Headers headers = request.getHeaders();

        doParseHeader(headers);

        String host = headers.get("Host");

        if (host == null) return;

        String[] split = host.split(":");
        request.setHost(split[0].trim());
        if (split.length == 2) request.setPort(Integer.parseInt(split[1].trim()));
    }

    /**
     * 解析请求体
     */
    private void parseBody() {
        String contentLength = request.getHeader("Content-Length");
        if (contentLength == null) return;
        int length = Integer.parseInt(contentLength);

        String contentType = request.getContentType();

        if (!isMultipart(contentType)) {
            int remaining = remaining();

            System.out.println(String.format("parse body: length %s  remaining %s", length, remaining));

            if (remaining < length) fill();

            String requestBody = new String(buffer, position, length);

            System.out.println(requestBody);


            position += length;
            request.setBody(requestBody);
            return;
        }

        // 处理二进制请求
        parseMultipartBody();
    }

    private void parseMultipartBody() {
        String contentType = request.getContentType();
        String boundary = contentType.split(";")[1].split("=")[1];
        String divider = "--" + boundary;

        Parameters parameters = request.parameters();

        while (true) {
            String readLine = readLine();
            // 请求体结束
            if ((divider + "--").equals(readLine)) return;

            Headers headers = new Headers();
            doParseHeader(headers);

            String disposition = headers.get("Content-Disposition");
            if (disposition == null) throw new BadRequest("错误的数据格式");

            Function<String, String> getDisposition = name -> {
                String reg = String.format("%s=\"(?<value>.+?)\"", name);

                Pattern pattern = Pattern.compile(reg);
                Matcher matcher = pattern.matcher(disposition);

                if (matcher.find())
                    return matcher.group("value");

                return null;
            };


            String name = getDisposition.apply("name");
            String fileName = getDisposition.apply("filename");

            // 普通表单参数
            if (fileName == null) {
                String value = readLine();
                parameters.putArg(name, value);
                continue;
            }

            // 文件参数
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("./" + fileName);

                byte[] dividerBytes = ("\r\n" + divider).getBytes(StandardCharsets.UTF_8);

                while (true) {
                    int index = indexOf(buffer, position, limit, dividerBytes);
                    if (index < 0) {
                        fileOutputStream.write(buffer, position, remaining());

                        position = limit;
                        fill();
                        continue;
                    }

                    fileOutputStream.write(buffer, position, index - position);
                    position += index + DELIMITER.length;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    private void doParseHeader(Headers headers) {
        String line;
        do {
            line = readLine();

            if ("".equals(line)) break;

            headers.add(line);
        } while (true);
    }

    /**
     * 读取一行
     *
     * @return
     */
    public String readLine() {
        int shouldSkip = 0;

        while (true) {
            if (closed) return null;

            if (needRead) fill();

            // todo: 可以优化，记录上次的查找的位置
            int index = indexOf(buffer, position + shouldSkip, limit, DELIMITER);

            if (index > -1) {
                // 读取到一行 position 移动到行首
                String line = StringUtil.bytes2string(buffer, position, index);
                position = index + DELIMITER.length;
                needRead = false;
                return line;
            }

            // 容量不够或者 换行符被杜读断
            if (limit == capacity) {
                if (remaining() > capacity / 2) throw new BadRequest("缓冲区过小");

                System.arraycopy(buffer, position, buffer, 0, remaining());
                limit = remaining();
                position = 0;

                // shouldSkip = limit - DELIMITER.length;
            }

            needRead = true;
        }
    }

    private boolean fill() {
        if (closed) return false;

        if (position == capacity) {
            position = 0;
            limit = 0;
        }

        try {
            int read = inputStream.read(buffer, limit, free());

            if (read == -1) {
                throw new BadRequest("connection closed");
            }

            needRead = false;
            limit += read;
            return true;
        } catch (IOException e) {
            throw new BadRequest("connection closed");
        }
    }


    public int position() {
        return position;
    }

    public int limit() {
        return limit;
    }

    public int capacity() {
        return capacity;
    }

    /**
     * 剩余可读数据
     *
     * @return
     */
    public int remaining() {
        return limit - position;
    }

    /**
     * 剩余可写数据
     *
     * @return
     */
    public int free() {
        return capacity - limit;
    }

    public Request getRequest() {
        if (!parsed) parse();

        parsed = false;
        needRead = true;
        Request old = request;
        request = new Request();
        return old;
    }
}
