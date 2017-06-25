package com.smusic.app;

import com.smusic.app.pojo.Song;
import com.smusic.app.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RemoteMusicServicesController {

    @Autowired
    private MusicService musicService;

    @RequestMapping(value = "/searchService", method = RequestMethod.GET)
    public List<Song> searchSong(@RequestParam String songName) {
        return musicService.search(songName);
    }

    @RequestMapping(value = "/downloadService", method = RequestMethod.POST)
    public String downloadSong(@RequestBody Song songJson, @RequestParam String saveFolder) {
        musicService.downloadSong(songJson, saveFolder);
        return "OK";
    }

    @RequestMapping(value = "/getSongUrl", method = RequestMethod.GET)
    public String getSongUrl(@RequestParam String songLink) {
        return musicService.getSongUrl(songLink);
    }
}
