package com.smusic.app.dao;

import com.smusic.app.CredentialManager;
import com.smusic.app.pojo.HttpEntityBuilder;
import com.smusic.app.pojo.Token;
import com.smusic.app.pojo.UrlBuilder;
import com.smusic.app.pojo.yad.Resource;
import com.squareup.okhttp.OkHttpClient;
import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.OkHttpClientFactory;
import com.yandex.disk.rest.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * Created by sergey on 28.05.17.
 */
@Repository
public class YandexDiskDao implements CloudDAO {

    private final Logger logger = LoggerFactory.getLogger(YandexDiskDao.class);

//    private static final String DEF_FILENAME = "defaultName";

    private static final int DEF_LIMIT = 20;

    @Value("${yandex.resources.url}")
    private String resourcesUrl;

    @Value("${yandex.token.url}")
    private String tokenUrl;

    @Value("${yandex.client.id}")
    private String clientId;

    @Value("${yandex.client.secret}")
    private String clientSecret;

    @Value("${yandex.root.path}")
    private String rootPath;

    @Autowired
    private CredentialManager credentialManager;

    @Autowired
    private RestTemplate restTemplate;

//    private String normilizeFileName(String filename) {
//        String filenameNorm = filename.replaceAll("&#quote;", " ")
//                .replaceAll("&#039;", " ")
//                .replaceAll("[$#@&:;{}\\[\\]\\']", "");
//        if (!filenameNorm.isEmpty()) {
//            return filenameNorm;
//        } else {
//            return DEF_FILENAME;
//        }
//    }

//    private String normalizeSongPath(String saveFolder, String songName) {
//        String songNameNorm = normilizeFileName(songName);
//        if (saveFolder != null && !saveFolder.isEmpty()) {
//            songNameNorm = normilizeFileName(saveFolder) + "/" + songNameNorm;
//        }
//        return songNameNorm;
//    }

    @Override
    public void uploadToCloud(String songUrl, String songName, String path) {
        logger.debug("Starting uploading songName:{}, songUrl:{}", songName, songUrl);
        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken()).build();

        String url = UrlBuilder.getInstance(resourcesUrl + "/upload").addPath(path, songName).addSongUrl(songUrl).build();
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST,
                requestEntity, String.class);
        logger.debug("Uploading complete songName:{}, songUrl:{}, responce:{}", songName, songUrl, result.getBody());
    }

//    private Link getPathForUploading(String songName, String folder) {
//        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
//                .addHeaderValue("Authorization", credentialManager.getToken())
//                .build();
//
//        String url = resourcesUrl + "/upload?path=SMusic/" + normalizeSongPath(folder, songName) + "&overwrite=true";
//
//        ResponseEntity<Link> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
//                Link.class);
//        return result.getBody();
//    }

    public String getToken(String code) {
        logger.debug("Requesting token for code:{} ", code);
        HttpEntity<MultiValueMap<String, String>> requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Content-type", "application/x-www-form-urlencoded")
                .addBodyValue("grant_type", "authorization_code")
                .addBodyValue("code", code)
                .addBodyValue("client_id", clientId)
                .addBodyValue("client_secret", clientSecret)
                .build();

        ResponseEntity<Token> result = restTemplate.postForEntity(tokenUrl, requestEntity, Token.class);
        logger.debug("Received token for code:{} status:{} expire:{}", code, result.getStatusCode(), result.getBody().getExpires_in());
        return result.getBody().getAccess_token();
    }

    @Override
    public void updateToken(String code) {
        credentialManager.setToken(getToken(code));
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
        logger.debug("Requested url for RootFileInfo: {}",url);
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
        String url = UrlBuilder.getInstance(resourcesUrl).addPath(rootPath, dirName).build();
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
                String.class);

        logger.debug("Received list of files for code:{} response:{}", result.getBody());
        return result.getBody();

    }

//    private String getEncodedUrl(String... paths) {
//        String fullPath = Arrays.stream(paths).map(this::normilizeFileName).collect(Collectors.joining("/"));
//        try {
//            return URLEncoder.encode(fullPath, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            logger.error("Encoding exception:", e.getMessage());
//            return fullPath;
//        }
//    }

    public static RestClient getInstance(final Credentials credentials) {
        OkHttpClient client = OkHttpClientFactory.makeClient();
        return new RestClient(credentials, client);
    }
}
