package com.nammaexpo.controllers;

import com.nammaexpo.utils.FileUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "Video Controller")
public class VideoController {

    @Autowired
    private FileUtils fileUtils;

    @PostMapping("videos")
    public String uploadVideoFile(@RequestParam("file") MultipartFile file) {
        return "fileUploadView";
    }

    @GetMapping("videos")
    public String getVideoFile() {
        return "fileUploadView";
    }
}
