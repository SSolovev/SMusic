package com.smusic.app;

import com.smusic.app.dao.CloudDAO;
import com.smusic.app.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CloudStorageController {

    @Autowired
    private MusicService musicService;

    @Autowired
    @Qualifier("yandexDiskDao")
    private CloudDAO cloudDAO;

    @Autowired
    private CredentialManager credentialManager;

    @Value("${yandex.authorize.url}")
    private String authorizeUrl;

    @RequestMapping("/")
    public String root() {
        if (credentialManager.getToken() == null || credentialManager.getToken().isEmpty()) {
            return "redirect:" + authorizeUrl;
        } else {
            return "index.html";
        }
    }

    @RequestMapping("/callback")
    public String callback(@RequestParam String code) {
//        session.setAttribute("credentialManager", new CredentialManager("", cloudDAO.getToken(code)));
        cloudDAO.updateToken(code);
        return "index.html";
    }

    @ResponseBody
    @RequestMapping("/getSongList")
    public String authRequestCallback2(@RequestParam String code) {
        return musicService.getListOfSongs(code);
    }

    @ResponseBody
    @RequestMapping("/health")
    public String healthChecker() {
        return "Greetings from Spring Boot!";
    }

}
