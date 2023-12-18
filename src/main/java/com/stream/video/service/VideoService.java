package com.stream.video.service;

import com.stream.video.dto.VideoDto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface VideoService {
    public VideoDto getVideoMetadata(String id);
    public void setVideoOutputStream(OutputStream outputStream, VideoDto videoDto) throws IOException;
}
