package com.stream.controller.dto.video;

import java.util.Date;

import com.stream.domain.video.dto.VideoDto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetVideoResponse {
	private long id;
	private String fileTag;
	private String extension;
	private long size;
	private String description;
	private Date createdAt;

	@Builder
	public GetVideoResponse(long id, String fileTag, String extension, long size, String description, Date createdAt) {
		this.id = id;
		this.fileTag = fileTag;
		this.extension = extension;
		this.size = size;
		this.description = description;
		this.createdAt = createdAt;
	}

	public static GetVideoResponse convertDtoToResponse(VideoDto videoDto) {
		return GetVideoResponse.builder()
			.id(videoDto.getId())
			.fileTag(videoDto.getFileTag())
			.extension(videoDto.getExtension())
			.size(videoDto.getSize())
			.description(videoDto.getDescription())
			.createdAt(videoDto.getCreatedAt())
			.build();
	}
}
