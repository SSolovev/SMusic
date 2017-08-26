package com.smusic.app.service.cloudstorage;

import com.smusic.app.service.cloudstorage.yandex.pojo.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by sergey on 28.05.17.
 */
@Repository
public class FileSystemAccessService implements CloudAccessService {

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
    public Future uploadToCloud(String songUrl, String songFullPath) {
//        try (InputStream in = getConnectionBinaryStream(songUrl, "GET");
//             OutputStream out = new FileOutputStream(new File(mediaLibraryPath + songName))
//        ) {
//
//            writeSongToFile(in, out);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new Future() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Object get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }

    @Override
    public String getListOfFiles(String token) {
        return null;
    }

    @Override
    public Resource getRootFileInfo() {
        return null;
    }

    @Override
    public Resource getFileInfo(String file) {
        return null;
    }

    @Override
    public Resource getFileInfo(String file, int limit) {
        return null;
    }

    @Override
    public String createNewRootDir() {
        return null;
    }

    @Override
    public String createNewDir(String dirName) {
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
