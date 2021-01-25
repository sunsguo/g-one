package com.gy.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {

    private OutputStream outputStream;

    private Headers headers = new Headers();

    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void addHeader(String name, String value) {
        this.headers.add(name, value);
    }

    public void write(String content) {
        write(content.getBytes(StandardCharsets.UTF_8));
    }

    // todo: 此处可以优化 如果 buffer 太大可以改为 transfer-encoding chunked 传输
    public void write(byte[] bytes) {
        try {
            buffer.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        this.headers.add("Content-Length", buffer.size() + "");
        try {
            writeHead();
            outputStream.write(buffer.toByteArray());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHead() throws IOException {
        String firstLine = "HTTP/1.1 200 OK\r\n";
        String header = firstLine + headers + "\r\n";

        outputStream.write(header.getBytes(StandardCharsets.UTF_8));
    }

}
