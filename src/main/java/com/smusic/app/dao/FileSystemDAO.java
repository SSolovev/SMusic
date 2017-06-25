package com.smusic.app.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sergey on 28.05.17.
 */
@Repository
public class FileSystemDAO implements CloudDAO {

    @Value("${fs.save.path}")
    private String mediaLibraryPath;

    @Override
    public String getToken(String code) {
        return null;
    }

    @Override
    public void updateToken(String code) {

    }

//    @Override
//    public void uploadToCloud(String songUrl) {
//        uploadToCloud(songUrl, mediaLibraryPath + songUrl.hashCode());
//    }

    @Override
    public void uploadToCloud(String songUrl, String songName, String folder) {
        try (InputStream in = getConnectionBinaryStream(songUrl, "GET");
             OutputStream out = new FileOutputStream(new File(mediaLibraryPath + songName))
        ) {

            writeSongToFile(in, out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getListOfFiles(String token) {
        return null;
    }

    private InputStream getConnectionBinaryStream(String connectionStringPostfix, String requestMethod) throws IOException {
        URL url = new URL(connectionStringPostfix);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestMethod);
        return con.getInputStream();
    }

    private void writeSongToFile(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[1024];
        int length;
        while ((length = in.read(buff)) != -1) {
            out.write(buff, 0, length);
        }
    }
}
