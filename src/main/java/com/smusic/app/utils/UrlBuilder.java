package com.smusic.app.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sergey on 23.08.17.
 */
public class UrlBuilder {

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

    public UrlBuilder addFields(String... fields) {
        params.add("fields=" + String.join(",", fields));
        return this;
    }

    public String build() {
        return url + "?" + params.stream().collect(Collectors.joining("&"));
    }

    private String getEncodedUrl(String... paths) {
        String fullPath = Arrays.stream(paths).map(SongUtils::normalizeFileName).collect(Collectors.joining("/"));
//        try {
//            return URLEncoder.encode(fullPath, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            logger.error("Encoding exception:", e.getMessage());
        return fullPath;
//        }
    }


}
