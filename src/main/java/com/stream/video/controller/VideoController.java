package com.stream.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stream.video.dto.VideoDto;
import com.stream.video.service.VideoService;

import java.util.List;

@RestController
public class VideoController {
    @Autowired
    @Qualifier("LocalVideoService")
    private VideoService videoService;

    @GetMapping(path = "/videos")
    public ResponseEntity<List<VideoDto>> listVideo() {
        List<VideoDto> videoDtoList = videoService.listVideo();
        return ResponseEntity.ok().body(videoDtoList);
    }

    @GetMapping(path = "/videos", params = "id")
    public ResponseEntity<VideoDto> getVideo(@RequestParam long id) {
        try {
            VideoDto videoDto = videoService.getVideoMetadata(id);
            return ResponseEntity.ok().body(videoDto);
        } catch (final Exception err) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/videos/stream", params = "id")
    public ResponseEntity<Resource> getVideoStream(@RequestParam long id) {
        try {
            Resource videoStream = videoService.createVideoStream(id);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "video/mp4");

            return ResponseEntity.ok().headers(headers).body(videoStream);
        } catch (Exception err) {
            return ResponseEntity.notFound().build();
        }
    }
}
