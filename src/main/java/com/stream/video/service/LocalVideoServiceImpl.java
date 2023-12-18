package com.stream.video.service;

import com.stream.video.dto.VideoDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Qualifier("LocalVideoService")
public class LocalVideoServiceImpl implements VideoService {
    @Value("${video.dir}")
    String videoDir;

    @Override
    public VideoDto getVideoMetadata(String id) throws FileNotFoundException {
        Path videoPath = getVideoPath(id);
        File file = new File(videoPath.toString());
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return VideoDto.builder().id(id).length(file.length()).build();
    }

    @Override
    public Resource createVideoStream(VideoDto videoDto) throws FileNotFoundException {
        Path videoPath = getVideoPath(videoDto.id);
        return new InputStreamResource(new FileInputStream(videoPath.toString()));
    }

    private Path getVideoPath(String id) {
        return Paths.get(videoDir, id + ".MOV");
    }
}
