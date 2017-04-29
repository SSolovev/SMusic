package com.smusic.app.service;

import com.smusic.app.pojo.Song;
import com.smusic.app.pojo.SongFields;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sergey on 28.04.17.
 */
public class PleerNetService implements MusicService {
    private static String OS_MEDIA_LIBRARY_PATH = "/Users/sergey/Downloads/smusic/";
    private static String SERVICE_URL = "http://pleer.net";
    private static String SEARCH_POSTFIX = "/search?q=";
    private static String DOWNLOAD_POSTFIX = "/site_api/files/get_url?action=download&id=";

    private static Pattern SONG_SEARCH_PATTERN = Pattern.compile("<li duration=.+?>");
    private static Pattern SONG_FIELDS_PATTERN = Pattern.compile("(\\w+)=(\"[^\"\']*\"|\'[ ^\"\']*\')");


    private BufferedReader getConnectionStreamReader(String connectionStringPostfix, String requestMethod) throws IOException {
        InputStream stream = getConnectionBinaryStream(connectionStringPostfix, requestMethod);
        return new BufferedReader(new InputStreamReader(stream));
    }

    private InputStream getConnectionBinaryStream(String connectionStringPostfix, String requestMethod) throws IOException {
        URL url = new URL(connectionStringPostfix);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestMethod);
        return con.getInputStream();
    }

    private List<Song> searchResultParser(String searchResultString) {
        List<Song> result = new ArrayList<>();
        Map<SongFields, String> columnMap = new HashMap<>();
        Matcher m = SONG_SEARCH_PATTERN.matcher(searchResultString);
        while (m.find()) {
            columnMap.clear();
            String songString = m.group();

            Matcher fieldsMatcher = SONG_FIELDS_PATTERN.matcher(songString);
            while (fieldsMatcher.find()) {
                String[] keyValue = fieldsMatcher.group().split("=");
                SongFields key;
                if (keyValue.length == 2 && (key = SongFields.getValueFrom(keyValue[0])) != null) {
                    columnMap.put(key, keyValue[1].replaceAll("[\"\']", ""));
                }
            }
            result.add(new Song(
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
        try (BufferedReader br = getConnectionStreamReader(url, method)) {

            if (br != null) {
                String response;

                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    public List<Song> search(String songName) {
        String resultString = getResponse(SERVICE_URL + SEARCH_POSTFIX + songName, "GET");
        return searchResultParser(resultString);
    }

    public void downloadSong(Song song) {
        String resultString = getResponse(SERVICE_URL + DOWNLOAD_POSTFIX + song.getLink(), "POST");
        System.out.println(resultString);
        JSONParser parser = new JSONParser();
        try {
            Object response = parser.parse(resultString);
            String songUrl = (String) ((JSONObject) response).get("track_link");
            String songPath = OS_MEDIA_LIBRARY_PATH + song.getSinger() + "-" + song.getSongName() + ".mp3";

            try (InputStream in = getConnectionBinaryStream(songUrl, "GET");
                 OutputStream out = new FileOutputStream(new File(songPath))
            ) {

                writeSongToFile(in, out);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeSongToFile(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[1024];
        int length;
        while ((length = in.read(buff)) != -1) {
            out.write(buff, 0, length);
        }
    }
}
