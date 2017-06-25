package com.smusic.app;

import com.smusic.app.dao.CloudDAO;
import com.smusic.app.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CloudStorageController {

    private final Logger logger = LoggerFactory.getLogger(CloudStorageController.class);

    @Autowired
    private MusicService musicService;

    @Autowired
    @Qualifier("yandexDiskDao")
    private CloudDAO cloudDAO;

    @Autowired
    private CredentialManager credentialManager;

    @RequestMapping("/")
    public String root() {
        return "index.html";
    }

    @RequestMapping("/callback")
    public String callback(@RequestParam String code) {
        logger.debug("Received callback code={}", code);
        cloudDAO.updateToken(code);
        return "redirect:index.html";
    }

    @ResponseBody
    @RequestMapping("/getSongList")
    public String getSongList(@RequestParam String code) {
        return musicService.getListOfSongs(code);
    }

    @ResponseBody
    @RequestMapping("/health")
    public String healthChecker() {
        return "Greetings from Spring Boot!";
    }

}
