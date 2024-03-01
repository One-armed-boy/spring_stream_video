package com.stream.domain.video.dto;

import java.util.Date;

import com.stream.domain.member.Member;
import com.stream.domain.video.Video;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO: VideoDto를 다른 도메인의 Dto와 유사하게 사용되도록 수정
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
	private Member member;

	@Builder
	public VideoDto(long id, String fileTag, String extension, long size, String description, Date createdAt,
		Member member) {
		this.id = id;
		this.fileTag = fileTag;
		this.extension = extension;
		this.size = size;
		this.description = description;
		this.createdAt = createdAt;
		this.member = member;
	}

	public static VideoDto convertDomainToDto(Video video) {
		return VideoDto.builder()
			.id(video.getId())
			.fileTag(video.getFileTag())
			.extension(video.getExtension())
			.size(video.getSize())
			.description(video.getDescription())
			.createdAt(video.getCreatedAt())
			.member(video.getMember())
			.build();
	}
}
