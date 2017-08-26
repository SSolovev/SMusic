package com.smusic.app.service.cloudstorage.yandex;

import com.smusic.app.CredentialManager;
import com.smusic.app.service.cloudstorage.yandex.pojo.Link;
import com.smusic.app.utils.HttpEntityBuilder;
import com.smusic.app.pojo.Token;
import com.smusic.app.utils.UrlBuilder;
import com.smusic.app.service.cloudstorage.yandex.pojo.Resource;
import com.smusic.app.service.cloudstorage.CloudAccessService;
import com.squareup.okhttp.OkHttpClient;
import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.OkHttpClientFactory;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.json.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * Created by sergey on 28.05.17.
 */
@Repository
public class YandexDiskAccessService implements CloudAccessService {

    private final Logger logger = LoggerFactory.getLogger(YandexDiskAccessService.class);

    private static final int DEF_LIMIT = 20;

    @Value("${yandex.resources.url}")
    private String resourcesUrl;

//    @Value("${yandex.token.url}")
//    private String tokenUrl;
//
//    @Value("${yandex.client.id}")
//    private String clientId;
//
//    @Value("${yandex.client.secret}")
//    private String clientSecret;

    @Value("${yandex.root.path}")
    private String rootPath;

    @Autowired
    private CredentialManager credentialManager;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExecutorService globalExecutorPool;

    private static final int LIMIT = 20;

    @Override
    public Future<Operation> uploadToCloud(String songUrl, String songFullPath) {
        logger.debug("Starting uploading song to path:{}, songUrl:{}", songFullPath, songUrl);

        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken()).build();

        Callable<Operation> r = () -> {

            String url = UrlBuilder.getInstance(resourcesUrl + "/upload")
                    .addPath(songFullPath)
                    .addSongUrl(songUrl)
                    .addOverwrite(true)
                    .build();

            ResponseEntity<Link> startUploadingResult = restTemplate.exchange(url, HttpMethod.POST,
                    requestEntity, Link.class);
            logger.debug("Uploading started songPath:{}, songUrl:{}, response:{}", songFullPath, songUrl, startUploadingResult.getBody());

            Link linkOb = startUploadingResult.getBody();
            int counter = 0;
            ResponseEntity<Operation> result;
            boolean repeat = false;
            do {
                Thread.sleep(500);
                result = restTemplate.exchange(linkOb.getHref(), linkOb.getMethod(),
                        requestEntity, Operation.class);

                if (result.getBody().isInProgress()) {
                    logger.debug("Uploading inProgress songPath:{}, response:{}", songFullPath, result.getBody());
                    counter++;
                    if (counter < LIMIT) {
                        repeat = true;
                    } else {
                        repeat = false;
                        logger.debug("Exceed limit, canceled songPath:{}, response:{}", songFullPath, result.getBody());
                    }

                } else if (result.getBody().isSuccess()) {
                    logger.debug("Uploading Success songPath:{}, response:{}", songFullPath, result.getBody());
                    repeat = false;
                } else {
                    logger.warn("Something goes wrong songPath:{}, response:{}", songFullPath, result.getBody());
                    repeat = false;
                }
            } while (repeat);


            logger.debug("Uploading finished songPath:{}, songUrl:{}, response:{}", songFullPath, songUrl, startUploadingResult.getBody());

            return result.getBody();

        };

        return globalExecutorPool.submit(r);
    }

    public String getToken(String code) {
//        logger.debug("Requesting token for code:{} ", code);
//        HttpEntity<MultiValueMap<String, String>> requestEntity = HttpEntityBuilder.getInstance()
//                .addHeaderValue("Content-type", "application/x-www-form-urlencoded")
//                .addBodyValue("grant_type", "authorization_code")
//                .addBodyValue("code", code)
//                .addBodyValue("client_id", clientId)
//                .addBodyValue("client_secret", clientSecret)
//                .build();
//
//        ResponseEntity<Token> result = restTemplate.postForEntity(tokenUrl, requestEntity, Token.class);
//        logger.debug("Received token for code:{} status:{} expire:{}", code, result.getStatusCode(), result.getBody().getExpires_in());
//        return result.getBody().getAccess_token();
        return null;
    }

    @Override
    public void updateToken(String code) {
//        credentialManager.setToken(getToken(code));
    }

    @Override
    public String getListOfFiles(String code) {
        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken()).build();

        ResponseEntity<String> result = restTemplate.exchange(resourcesUrl + "/files", HttpMethod.GET,
                requestEntity, String.class);
        logger.debug("Received list of files for code:{} response:{}", code, result.getBody());
        return result.getBody();
    }

    @Override
    public Resource getRootFileInfo() {
        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken()).build();
        String url = UrlBuilder.getInstance(resourcesUrl).addPath(rootPath).build();
        logger.debug("Requested url for RootFileInfo: {}", url);
        ResponseEntity<Resource> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Resource.class);

        logger.debug("Received list of files for code:{} response:{}", result.getBody());
        return result.getBody();
    }

    @Override
    public Resource getFileInfo(String file) {
        return getFileInfo(file, DEF_LIMIT);
    }

    @Override
    public Resource getFileInfo(String file, int limit) {
        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken()).build();
        String url = UrlBuilder.getInstance(resourcesUrl).addPath(rootPath, file).addLimit(limit).build();
        ResponseEntity<Resource> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Resource.class);
        logger.debug("Received list of files for code:{} response:{}", result.getBody());
        return result.getBody();
    }

    @Override
    public String createNewRootDir() {
        return createNewDir("");

    }

    @Override
    public String createNewDir(String dirName) {
        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken())
                .build();
        String url = UrlBuilder.getInstance(resourcesUrl)
                .addPath(rootPath, dirName).build();
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
                String.class);

        logger.debug("Received list of files for code:{} response:{}", result.getBody());
        return result.getBody();

    }


    public static RestClient getInstance(final Credentials credentials) {
        OkHttpClient client = OkHttpClientFactory.makeClient();
        return new RestClient(credentials, client);
    }
}
