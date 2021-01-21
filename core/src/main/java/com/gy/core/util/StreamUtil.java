package com.gy.core.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {

    public static void copy(InputStream inputStream, OutputStream outputStream) {
        byte[] temp = new byte[1024];
        int read;
        while (true) {
            try {
                read = inputStream.read(temp);
                if (read == -1) break;

                outputStream.write(temp, 0, read);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
