package com.smusic.app.dao;

import com.smusic.app.pojo.yad.Resource;

/**
 * Created by sergey on 28.05.17.
 */
public interface CloudDAO {

    String getToken(String code);

    void updateToken(String code);

    void uploadToCloud(String songUrl, String songName, String path);

    String getListOfFiles(String token);

    Resource getRootFileInfo();

    Resource getFileInfo(String file);

    Resource getFileInfo(String file, int limit);

    String createNewRootDir();

    String createNewDir(String dirName);

}
