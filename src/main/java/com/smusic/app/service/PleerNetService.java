package com.smusic.app.service;

import com.smusic.app.dao.CloudDAO;
import com.smusic.app.pojo.Song;
import com.smusic.app.pojo.SongFields;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PleerNetService implements MusicService {

    private final Logger logger = LoggerFactory.getLogger(PleerNetService.class);

    private static String SERVICE_URL = "http://pleer.net";
    private static String SEARCH_POSTFIX = "/search?q=";
    private static String DOWNLOAD_POSTFIX = "/site_api/files/get_url?action=download&id=";

    private static Pattern SONG_SEARCH_PATTERN = Pattern.compile("<li duration=.+?>");
    private static Pattern SONG_FIELDS_PATTERN = Pattern.compile("(\\w+)=(\"[^\"\']*\"|\'[ ^\"\']*\')");

    @Autowired
    @Qualifier("yandexDiskDao")
    private CloudDAO cloudDAO;

    @Autowired
    private SourceConnectionProvider connectionProvider;

    private List<Song> searchResultParser(String searchResultString) {
        logger.debug("Start parsing resultString...");
        List<Song> result = new ArrayList<>();
        Map<SongFields, String> columnMap = new HashMap<>();
        Matcher m = SONG_SEARCH_PATTERN.matcher(searchResultString);
        while (m.find()) {
            columnMap.clear();
            String songString = m.group();

            logger.debug("Song found: {}", songString);

            Matcher fieldsMatcher = SONG_FIELDS_PATTERN.matcher(songString);
            while (fieldsMatcher.find()) {
                String[] keyValue = fieldsMatcher.group().split("=");
                SongFields key;
                if (keyValue.length == 2 && (key = SongFields.getValueFrom(keyValue[0])) != null) {
                    columnMap.put(key, keyValue[1].replaceAll("[\"\']", ""));
                }
            }
            result.add(
                    new Song(
                            columnMap.get(SongFields.FILE_ID),
                            Integer.parseInt(columnMap.get(SongFields.DURATION)),
                            columnMap.get(SongFields.SINGER),
                            columnMap.get(SongFields.SONG_NAME),
                            columnMap.get(SongFields.LINK),
                            columnMap.get(SongFields.RATE),
                            columnMap.get(SongFields.SIZE)
                    ));
        }
        return result;

    }

    private String getResponse(String url, String method) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = connectionProvider.getConnectionStreamReader(url, method)) {

            if (br != null) {
                String response;

                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }

            }
            logger.debug("Received response for url:{} method:{} resp:{}", url, method, sb.toString());
        } catch (IOException e) {
            logger.error("Exception while getting response url:{} method:{}", url, method, e);
        }

        return sb.toString();
    }


    public List<Song> search(String songName) {
        String resultString = getResponse(SERVICE_URL + SEARCH_POSTFIX + songName, "GET");
        return searchResultParser(resultString);
    }

    public String getSongUrl(String songLink) {
        logger.debug("Get song from url: {}", songLink);
        String resultString = getResponse(SERVICE_URL + DOWNLOAD_POSTFIX + songLink, "POST");
        JSONParser parser = new JSONParser();
        Object response = null;
        try {
            response = parser.parse(resultString);
        } catch (ParseException e) {
            logger.error("Exception while parsing result from songLink:{} response:{}", songLink, response);
        }
        String songUrl = (String) ((JSONObject) response).get("track_link");
        logger.debug("Received track link: {}", songUrl);
        return songUrl;
    }

    @Override
    public String getListOfSongs(String token) {
        return cloudDAO.getListOfFiles(token);
    }

    public void downloadSong(Song song) {
        logger.debug("Downloading song: {}", song);
        String resultString = getResponse(SERVICE_URL + DOWNLOAD_POSTFIX + song.getLink(), "POST");
        JSONParser parser = new JSONParser();
        try {
            Object response = parser.parse(resultString);
            String songUrl = (String) ((JSONObject) response).get("track_link");
            String songName = song.getSinger() + "-" + song.getSongName() + ".mp3";
            cloudDAO.uploadToCloud(songUrl, songName);

        } catch (Exception e) {
            logger.error("Exception while downloading song to cloud, song:{}", song, e);
        }
    }

}
