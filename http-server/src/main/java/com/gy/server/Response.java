package com.gy.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Response {

    private int statusCode = 200;

    private String statusMsg = "OK";

    private OutputStream outputStream;

    private int bufferSize = 1024 << 3;

    private ByteBuffer byteBuffer;

    private Headers headers = new Headers();

    private boolean flushed = false;

    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void addHeader(String name, String value) {
        this.headers.add(name, value);
    }

    public void setHeader(String name, String value) {
        this.headers.set(name, value);
    }

    public void write(String content) {
        write(content.getBytes(StandardCharsets.UTF_8));
    }

    // todo: 此处可以优化 如果 buffer 太大可以改为 transfer-encoding chunked 传输
    public void write(byte[] bytes) {
        write(bytes, 0, bytes.length);
    }

    public void write(byte[] bytes, int offset, int length) {
        try {
            getOutputStream().write(bytes, offset, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initBufferIfNeeded() {
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(bufferSize);
        }
    }

    public void flush() {
        if (flushed) return;

        byteBuffer.flip();
        int remaining = byteBuffer.remaining();

        this.headers.add("Content-Length", remaining + "");

        try {
            writeHead();

            byte[] data = new byte[remaining];
            byteBuffer.get(data);

            outputStream.write(data);

            outputStream.flush();
            buffer.close();

            flushed = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHead() throws IOException {
        String firstLine = String.format("HTTP/1.1 %s %s\r\n", statusCode, statusMsg);
        String header = firstLine + headers + "\r\n";

        outputStream.write(header.getBytes(StandardCharsets.UTF_8));
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public Headers getHeaders() {
        return headers;
    }

    public void sendError(int sc, String msg) {
        setStatus(sc, msg);
        write("");
    }

    public void sendError(int sc) {
        sendError(sc, null);
    }

    public void setStatus(int sc, String sm) {
        statusCode = sc;
        statusMsg = sm;

        if (statusMsg == null) {
            HttpStatus httpStatus = HttpStatus.valueOf(sc);
            statusMsg = httpStatus.getReasonPhrase();
        }
    }

    public void setStatus(int sc) {
        setStatus(sc, null);
    }

    public int getStatus() {
        return statusCode;
    }

    public OutputStream getOutputStream() {
        initBufferIfNeeded();

        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                byteBuffer.put((byte) b);
            }
        };
    }

    public void setBufferSize(int size) {
        bufferSize = size;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void resetBuffer() {
        byteBuffer.clear();
    }

    public void sendRedirect(String location) {
        setStatus(HttpStatus.FOUND.value());
        addHeader("Location", location);
        write("");
    }
}
