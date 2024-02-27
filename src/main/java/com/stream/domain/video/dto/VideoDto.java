package com.stream.domain.video.dto;

import java.util.Date;

import com.stream.domain.video.Video;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class VideoDto {
	private long id;
	private String fileTag;
	private String extension;
	private long size;
	private String description;
	private Date createdAt;

	@Builder
	public VideoDto(long id, String fileTag, String extension, long size, String description, Date createdAt) {
		this.id = id;
		this.fileTag = fileTag;
		this.extension = extension;
		this.size = size;
		this.description = description;
		this.createdAt = createdAt;
	}

	public static VideoDto convertDomainToDto(Video video) {
		return VideoDto.builder()
			.id(video.getId())
			.fileTag(video.getFileTag())
			.extension(video.getExtension())
			.size(video.getSize())
			.description(video.getDescription())
			.createdAt(video.getCreatedAt())
			.build();
	}
}
