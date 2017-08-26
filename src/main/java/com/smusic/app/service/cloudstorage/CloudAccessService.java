package com.smusic.app.service.cloudstorage;

import com.smusic.app.service.cloudstorage.yandex.pojo.Resource;
import com.yandex.disk.rest.json.Operation;

import java.util.concurrent.Future;

/**
 * Created by sergey on 28.05.17.
 */
public interface CloudAccessService {

    String getToken(String code);

    void updateToken(String code);

    Future<Operation> uploadToCloud(String songUrl, String songFullPath);

    String getListOfFiles(String token);

    Resource getRootFileInfo();

    Resource getFileInfo(String file);

    Resource getFileInfo(String file, int limit);

    String createNewRootDir();

    String createNewDir(String dirName);

}
