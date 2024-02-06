package com.stream.domain.video;

import java.util.List;

import com.stream.domain.video.dto.VideoDto;

import jakarta.persistence.EntityNotFoundException;

public interface VideoService {
	List<VideoDto> listVideo();

	VideoDto getVideoMetadata(long id) throws EntityNotFoundException;

	String getVideoFilePath(long id) throws EntityNotFoundException;

	void createVideo(Video... video);
}
