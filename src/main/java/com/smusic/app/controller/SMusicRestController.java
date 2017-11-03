package com.smusic.app.controller;

import com.smusic.app.pojo.Song;
import com.smusic.app.service.cloudstorage.CloudAccessService;
import com.smusic.app.service.cloudstorage.yandex.pojo.Resource;
import com.smusic.app.service.musicsource.AbstractMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class SMusicRestController {

    @Autowired
    private AbstractMusicService musicService;

    @Autowired
    @Qualifier("yandexDiskAccessService")
    private CloudAccessService cloudAccessService;


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


    @RequestMapping("/getSongList")
    public String getSongList(@RequestParam String code) {
        return musicService.getListOfSongs(code);
    }

    @RequestMapping("/getFileInfo")
    public Resource getFileInfo(@RequestParam String file) {
        return cloudAccessService.getFileInfo(file);
    }

    @RequestMapping("/createDir")
    public String createNewDir(@RequestParam String dirName) {
        return cloudAccessService.createNewDir(dirName);
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public String getUserName(Principal user) {
        if (user != null) {
            return user.getName();
        } else {
            return null;
        }

    }

    @RequestMapping("/main/health")
    public String healthChecker(Principal user) {
        return "Greetings from Spring Boot!" + user;
    }

    @RequestMapping("/main/login")
    public String mainLogin() {
        return "NICE";
    }
}
