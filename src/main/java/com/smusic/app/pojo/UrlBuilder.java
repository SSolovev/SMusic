package com.smusic.app.pojo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sergey on 23.08.17.
 */
public class UrlBuilder {
    private static final String DEF_FILENAME = "defaultName";

    private List<String> params;
    private String url;

    private UrlBuilder(String url) {
        this.params = new ArrayList<>();
        this.url = url;
    }

    public static UrlBuilder getInstance(String url) {
        return new UrlBuilder(url);
    }

    public UrlBuilder addPath(String... path) {
        params.add("path=" + getEncodedUrl(path));
        return this;
    }


    public UrlBuilder addSongUrl(String url) {
        String encodedUrl;
//        try {
//            encodedUrl = URLEncoder.encode(url, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
            encodedUrl = url;
//        }
        params.add("url=" + encodedUrl);
        return this;
    }

    public UrlBuilder addLimit(int limit) {
        params.add("limit=" + limit);
        return this;
    }

    public String build() {
        return url + "?" + params.stream().collect(Collectors.joining("&"));
    }

    private String getEncodedUrl(String... paths) {
        String fullPath = Arrays.stream(paths).map(this::normalizeFileName).collect(Collectors.joining("/"));
//        try {
//            return URLEncoder.encode(fullPath, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            logger.error("Encoding exception:", e.getMessage());
            return fullPath;
//        }
    }

    private String normalizeFileName(String filename) {
        String filenameNorm = filename.replaceAll("&#quote;", " ")
                .replaceAll("&#039;", " ")
                .replaceAll("[$#@&;{}\\[\\]\\']", "");
        if (!filenameNorm.isEmpty()) {
            return filenameNorm;
        } else {
            return DEF_FILENAME;
        }
    }
}
