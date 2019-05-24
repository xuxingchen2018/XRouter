package com.mrcd.xrouter.compiler.utils;

import java.io.Closeable;
import java.io.IOException;

public class FileUtils {

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
