package com.vilmantas.helpers;

import java.io.InputStream;

public class FileReader {

    private FileReader() {
    }

    /**
     * Method reads file from classpath and returns InputStream.
     */
    public static InputStream getInputStream(String fileName) {
        return FileReader.class.getResourceAsStream("/" + fileName);
    }
}
