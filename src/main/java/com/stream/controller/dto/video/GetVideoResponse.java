package com.stream.controller.dto.video;

import java.util.Date;

import com.stream.domain.video.dto.VideoDto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class GetVideoResponse {
	private long id;
	private String fileTag;
	private String extension;
	private long size;
	private String description;
	private Date createdAt;
	private String member;

	@Builder
	public GetVideoResponse(long id, String fileTag, String extension, String member, long size, String description,
		Date createdAt) {
		this.id = id;
		this.fileTag = fileTag;
		this.extension = extension;
		this.size = size;
		this.description = description;
		this.createdAt = createdAt;
		this.member = member;
	}

	public static GetVideoResponse convertDtoToResponse(VideoDto videoDto) {
		var member = videoDto.getMember();
		var memberEmail = member != null ? member.getEmail() : null;
		return GetVideoResponse.builder()
			.id(videoDto.getId())
			.fileTag(videoDto.getFileTag())
			.extension(videoDto.getExtension())
			.size(videoDto.getSize())
			.description(videoDto.getDescription())
			.createdAt(videoDto.getCreatedAt())
			.member(memberEmail)
			.build();
	}
}
