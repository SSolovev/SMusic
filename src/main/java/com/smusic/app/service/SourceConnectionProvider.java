package com.smusic.app.service;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sergey on 28.05.17.
 */
@Component
public class SourceConnectionProvider {

    public BufferedReader getConnectionStreamReader(String connectionStringPostfix, String requestMethod) throws IOException {
        InputStream stream = getConnectionBinaryStream(connectionStringPostfix, requestMethod);
        return new BufferedReader(new InputStreamReader(stream));
    }

    public InputStream getConnectionBinaryStream(String connectionStringPostfix, String requestMethod) throws IOException {
        URL url = new URL(connectionStringPostfix);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestMethod);
        return con.getInputStream();
    }

}
