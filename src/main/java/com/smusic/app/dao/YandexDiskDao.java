package com.smusic.app.dao;

import com.smusic.app.CredentialManager;
import com.smusic.app.pojo.HttpEntityBuilder;
import com.smusic.app.pojo.Token;
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


/**
 * Created by sergey on 28.05.17.
 */
@Repository
public class YandexDiskDao implements CloudDAO {

    private final Logger logger = LoggerFactory.getLogger(YandexDiskDao.class);

    @Value("${yandex.resources.url}")
    private String resourcesUrl;

    @Value("${yandex.token.url}")
    private String tokenUrl;

    @Value("${yandex.client.id}")
    private String clientId;

    @Value("${yandex.client.secret}")
    private String clientSecret;


    @Autowired
    private CredentialManager credentialManager;

    @Autowired
    private RestTemplate restTemplate;


    private String normalizeSongName(String songName) {
        return songName.replaceAll("&#quote;", " ")
                .replaceAll("&#039;", " ")
                .replaceAll("[$#@&:;{}\\[\\]\\']", "");
    }

    @Override
    public void uploadToCloud(String songUrl, String songName) {
        logger.debug("Starting uploading songName:{}, songUrl:{}", songName, songUrl);
        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken())
                .build();

        String url = resourcesUrl + "/upload?path=SMusic/" + normalizeSongName(songName) + "&url=" + songUrl;

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                String.class);
        logger.debug("Uploading complete songName:{}, songUrl:{}, responce:{}", songName, songUrl, result.getBody());
    }


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
                .addHeaderValue("Authorization", credentialManager.getToken())
                .build();

        ResponseEntity<String> result = restTemplate.exchange(resourcesUrl + "/files", HttpMethod.GET, requestEntity,
                String.class);
        logger.debug("Received list of files for code:{} response:{}", code, result.getBody());
        return result.getBody();
    }


    public static RestClient getInstance(final Credentials credentials) {
        OkHttpClient client = OkHttpClientFactory.makeClient();
//        client.networkInterceptors().add(new StethoInterceptor());
        return new RestClient(credentials, client);
    }
}
