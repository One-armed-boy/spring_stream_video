package com.stream.controller.dto.video;

import java.util.Date;
import java.util.List;

import com.stream.domain.video.dto.VideoDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ListVideoResponse {
	private List<Element> videos;

	private ListVideoResponse(List<Element> videos) {
		this.videos = videos;
	}

	public static ListVideoResponse create(List<VideoDto> videos) {
		return new ListVideoResponse(videos.stream().map(videoDto -> Element.builder()
			.id(videoDto.getId())
			.fileTag(
				videoDto.getFileTag())
			.extension(videoDto.getExtension())
			.size(videoDto.getSize())
			.member(videoDto.getMember().getEmail())
			.createdAt(videoDto.getCreatedAt())
			.build()).toList());
	}

	@Getter
	@ToString
	@NoArgsConstructor
	public static class Element {
		private long id;
		private String fileTag;
		private String extension;
		private long size;
		private Date createdAt;
		private String member;

		@Builder
		public Element(long id, String fileTag, String extension, String member, long size, Date createdAt) {
			this.id = id;
			this.fileTag = fileTag;
			this.extension = extension;
			this.size = size;
			this.createdAt = createdAt;
			this.member = member;
		}
	}
}
