package com.smusic.app;

import com.smusic.app.dao.CloudDAO;
import com.smusic.app.pojo.Song;
import com.smusic.app.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RemoteMusicServicesController {

    @Autowired
    private MusicService musicService;

//    @Autowired
//    @Qualifier("yandexDiskDao")
//    private CloudDAO cloudDAO;


    @RequestMapping(value = "/searchService", method = RequestMethod.GET)
    public List<Song> searchSong(@RequestParam String songName) {
        return musicService.search(songName);
    }

    @RequestMapping(value = "/downloadService", method = RequestMethod.POST)
    public String downloadSong(@RequestBody Song songJson) {
        musicService.downloadSong(songJson);
        return "OK";
    }

    @RequestMapping(value = "/getSongUrl", method = RequestMethod.GET)
    public String getSongUrl(@RequestParam String songLink) {
        return musicService.getSongUrl(songLink);
    }
}
