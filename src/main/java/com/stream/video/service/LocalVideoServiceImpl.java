package com.stream.video.service;

import com.stream.video.dto.VideoDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Qualifier("LocalVideoService")
public class LocalVideoServiceImpl implements VideoService {
    @Value("${video.dir}")
    String videoDir;

    @Override
    public VideoDto getVideoMetadata(String id) {
        Path videoPath = getVideoPath(id);
        File file = new File(videoPath.toString());
        return VideoDto.builder().id(id).length(file.length()).build();
    }

    @Override
    public void setVideoOutputStream(OutputStream outputStream, VideoDto videoDto) {
        Path videoPath = getVideoPath(videoDto.id);
        File file = new File(videoPath.toString());
        try {
            final InputStream inputStream = new FileInputStream(file);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) >=0) {
                outputStream.write(bytes,0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception err) {
            System.out.println(err);
        }
    }

    private Path getVideoPath(String id) {
        return Paths.get(videoDir, id, ".MOV");
    }
}
