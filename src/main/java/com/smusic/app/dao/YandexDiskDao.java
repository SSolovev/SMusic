package com.smusic.app.dao;

import com.smusic.app.CredentialManager;
import com.smusic.app.pojo.HttpEntityBuilder;
import com.smusic.app.pojo.Token;
import com.squareup.okhttp.OkHttpClient;
import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.OkHttpClientFactory;
import com.yandex.disk.rest.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.function.Function;


/**
 * Created by sergey on 28.05.17.
 */
@Repository
public class YandexDiskDao implements CloudDAO {

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

        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken())
                .build();

        String url = resourcesUrl + "/upload?path=SMusic/" + normalizeSongName(songName) + "&url=" + songUrl;

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                String.class);

        System.out.println("Body:" + result.getBody().replace("'#&$\"><{}[]", ""));
    }


    public String getToken(String code) {

        HttpEntity<MultiValueMap<String, String>> requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Content-type", "application/x-www-form-urlencoded")
                .addBodyValue("grant_type", "authorization_code")
                .addBodyValue("code", code)
                .addBodyValue("client_id", clientId)
                .addBodyValue("client_secret", clientSecret)
                .build();

        ResponseEntity<Token> result = restTemplate.postForEntity(tokenUrl, requestEntity, Token.class);
        return result.getBody().getAccess_token();
    }

    @Override
    public void updateToken(String code) {
        credentialManager.setToken(getToken(code));
    }


//    HttpHeaders getHeaders(String[][] pairs) {
//        HttpHeaders headers = new HttpHeaders();
//        Arrays.stream(pairs).forEach(pair -> headers.add(pair[0], pair[1]));
//        return headers;
//    }


    @Override
    public String getListOfFiles(String code) {
        HttpEntity requestEntity = HttpEntityBuilder.getInstance()
                .addHeaderValue("Authorization", credentialManager.getToken())
                .build();

        ResponseEntity<String> result = restTemplate.exchange(resourcesUrl + "/files", HttpMethod.GET, requestEntity,
                String.class);
        System.out.println("Body:" + result.getBody());
        return result.getBody();
    }


    public static RestClient getInstance(final Credentials credentials) {
        OkHttpClient client = OkHttpClientFactory.makeClient();
//        client.networkInterceptors().add(new StethoInterceptor());
        return new RestClient(credentials, client);
    }
//    public void uploadFile(final Credentials credentials, final String serverPath, final String localFile) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run () {
//                try {
//                    RestClient client = getInstance(credentials);
//                    Link link = client.getUploadLink(serverPath, true);
//                    client.uploadFile(link, true, new File(localFile), UploadFileRetainedFragment.this);
//                    uploadComplete();
//                } catch (CancelledUploadingException ex) {
//                    // cancelled by user
//                } catch (HttpCodeException ex) {
//                    Log.d(TAG, "uploadFile", ex);
//                    sendException(ex.getResponse().getDescription());
//                } catch (IOException | ServerException ex) {
//                    Log.d(TAG, "uploadFile", ex);
//                    sendException(ex);
//                }
//            }
//        }).start();
//    }
// https://cloud-api.yandex.net:443/v1/disk/resources/upload?path=%D0%9C%D1%83%D0%B7%D1%8B%D0%BA%D0%B0%2Ftst.mp3&url=http%3A%2F%2Fs2-3.pleerstorage.com%2F0042f8bee27d4fca6f27cee6cc04ac65d9aff50b70f6be90fa051aada356c319d46c813d2156e2971b7483a1655584435ff8013819de0c5ac1649146b51e992a3868ac7d1e3f514e57fc89188d1081%2Fa251fb9217.mp3

// public JsonObjPhoto uploadFileToCloud(String url, File file) throws IOException {
//        ResponseEntity<JsonObjPhoto> result;
//        try (InputStream in = new FileInputStream(file.getPath())) {
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            headers.setContentLength(file.length());
//            headers.set("Authorization", token);
//
//
//            HttpEntity<byte[]> requestEntity = new HttpEntity<>(IOUtils.toByteArray(in), headers);
//            result = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
//                    JsonObjPhoto.class);
//        }
//    }
}
