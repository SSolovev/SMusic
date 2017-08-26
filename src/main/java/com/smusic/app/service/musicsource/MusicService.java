package com.smusic.app.service.musicsource;

import com.smusic.app.pojo.Song;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by sergey on 28.04.17.
 */
public interface MusicService {
    List<Song> search(String songName);

    void downloadSong(Song song, String saveFolder);

    String getSongUrl(String songLink);

    String getListOfSongs(String token);
}
