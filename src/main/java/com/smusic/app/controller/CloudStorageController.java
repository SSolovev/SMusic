package com.smusic.app.controller;

import com.smusic.app.CredentialManager;
import com.smusic.app.service.cloudstorage.yandex.pojo.Resource;
import com.smusic.app.repository.SongDataDao;
import com.smusic.app.repository.dto.SongData;
import com.smusic.app.service.musicsource.MusicService;
import com.smusic.app.service.cloudstorage.CloudAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class CloudStorageController {

    private final Logger logger = LoggerFactory.getLogger(CloudStorageController.class);

    @Autowired
    private MusicService musicService;

    @Autowired
    @Qualifier("yandexDiskAccessService")
    private CloudAccessService cloudAccessService;

    @Autowired
    private CredentialManager credentialManager;


//    @Autowired
//    private SongDataDao songDao;

    @RequestMapping("/")
    public String root() {
//        Iterable<SongData> iter = songDao.findAll(new PageRequest(1, 5, Sort.Direction.ASC, "uploadDate"));
//        System.out.println(StreamSupport.stream(iter.spliterator(), false).map(SongData::toString)
//                .collect(Collectors.joining("\n")));
        return "index.html";
    }

    @RequestMapping("/callback")
    public String callback(@RequestParam String code) {
        logger.debug("Received callback code={}", code);
//        cloudAccessService.updateToken(code);
        return "redirect:index.html";
    }

    @ResponseBody
    @RequestMapping("/getSongList")
    public String getSongList(@RequestParam String code) {
        return musicService.getListOfSongs(code);
    }

    @ResponseBody
    @RequestMapping("/getFileInfo")
    public Resource getFileInfo(@RequestParam String file) {
        return cloudAccessService.getFileInfo(file);
    }

    @ResponseBody
    @RequestMapping("/createDir")
    public String createNewDir(@RequestParam String dirName) {
        return cloudAccessService.createNewDir(dirName);
    }

}
