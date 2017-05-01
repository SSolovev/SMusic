package com.smusic.app;


import com.smusic.app.pojo.Song;
import com.smusic.app.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    @Autowired
    private MusicService pleerNetService;

    @RequestMapping("/test_req")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/searchService", method = RequestMethod.GET)
    public List<Song> searchSong(@RequestParam String songName) {
        return pleerNetService.search(songName);
    }

    @RequestMapping(value = "/downloadService", method = RequestMethod.POST)
    public String downloadSong(@RequestBody Song songJson) {
        System.out.println(songJson);
        pleerNetService.downloadSong(songJson);
        return "OK";
    }

}