package com.stream.video.controller;

import java.io.*;

import com.stream.video.dto.VideoDto;
import com.stream.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class VideoController {
    @Autowired
    @Qualifier("LocalVideoService")
    private VideoService videoService;

    @GetMapping(path = "/video")
    public ResponseEntity<StreamingResponseBody> getVideoStream(@RequestParam String id) {
        VideoDto videoDto = videoService.getVideoMetadata(id);

        StreamingResponseBody streamingResponseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                try {
                    videoService.setVideoOutputStream(outputStream, videoDto);
                } catch (final Exception err) {
                    System.out.println(err);
                }
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "video/mp4");
        headers.add("Content-Length", Long.toString(videoDto.length));

        return ResponseEntity.ok().headers(headers).body(streamingResponseBody);
    }
}
