package io.luke.downchecker.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.luke.downchecker.model.Url;

@Controller
@RequestMapping("/check")
public class UrlController {

    private final String SITE_UP = "siteUp";
    private final String SITE_DOWN = "siteDown";
    private final String INCORRECT_URL = "urlIncorrect";

    @GetMapping
    public String getCheckPage(Model model) {
        model.addAttribute("url", new Url());
        return "check";
    }

    @PostMapping
    public String postUrl(@ModelAttribute("Url") Url url) {
        String message = "";
        String urlText = url.getUrlText();
        if(!urlText.contains("://")) {
            urlText = "https://" + urlText;
        }
        try {
            URL urlObj = new URL(urlText);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCodeCategory = conn.getResponseCode() / 100;
            if(responseCodeCategory != 2 || responseCodeCategory != 3) {
                message = SITE_DOWN;
            } else {
                message = SITE_UP;
            }
            message = SITE_UP;
        } catch (MalformedURLException e) {
            message = INCORRECT_URL;
        } catch (IOException e) {
            message = SITE_DOWN;
            e.printStackTrace();
        }
        return message;
    }
    
}
