package com.vilmantas.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InputStreamToString {

    private InputStreamToString() {
    }

    /**
     * Method converts InputStream to String
     */
    public static String getRawGameData(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
