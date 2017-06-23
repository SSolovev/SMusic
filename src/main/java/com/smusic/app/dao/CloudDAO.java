package com.smusic.app.dao;

/**
 * Created by sergey on 28.05.17.
 */
public interface CloudDAO {

    String getToken(String code);

    void updateToken(String code);

    void uploadToCloud(String songUrl, String songName);

    String getListOfFiles(String token);
}
