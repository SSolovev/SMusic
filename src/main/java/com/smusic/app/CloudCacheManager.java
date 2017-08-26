package com.smusic.app;

import com.smusic.app.service.cloudstorage.CloudAccessService;
import com.smusic.app.service.cloudstorage.yandex.pojo.Resource;
import com.smusic.app.service.cloudstorage.yandex.pojo.ResourceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CloudCacheManager {
    @Autowired
    @Qualifier("yandexDiskAccessService")
    private CloudAccessService cloudAccessService;

    @Value("${yandex.root.path}")
    private String rootPath;

    private Map<String, Resource> dirCache;

    @PostConstruct
    public void init() {
        dirCache = new ConcurrentHashMap<>();
        Resource root;
        try {
            root = cloudAccessService.getRootFileInfo();
        } catch (Exception e) {
            System.out.println("Root not exist: "+ e.getMessage());
            cloudAccessService.createNewRootDir();
            root = cloudAccessService.getRootFileInfo();
        }

        dirCache.put(rootPath + "/", root);
        ResourceList resourceList = root.get_embedded();
        if (resourceList != null)
            for (Resource res : resourceList.getItems()) {
                if (res.isDirectory()) {
                    dirCache.put(res.getPath(), res);
                }
            }
    }

    public Resource getCachedResource(String dirName) {
        return dirCache.get(rootPath + "/" + dirName);
    }

    public Resource updateCachedResource(Resource resource) {
        if (resource.isDirectory()) {
            return dirCache.put(resource.getPath(), resource);
        } else {
            System.out.println("IT IS NOT DIR!");
            return resource;
        }

    }

}
