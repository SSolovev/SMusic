package com.smusic.app;

import com.smusic.app.pojo.Song;
import com.smusic.app.service.MusicService;
import com.smusic.app.service.PleerNetService;
import com.smusic.app.service.TorrentService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void downloadFronPleerNet(){
        MusicService service = new PleerNetService();
        List<Song> result = service.search("Beatles");

        System.out.println(result.size());
        for(Song song:result){
            System.out.println(song);
        }

        service.downloadSong(result.get(0));
    }

    public static void main(String[] args) throws IOException, ParseException {

        TorrentService ts = new TorrentService();
        ts.download();
    }
}
