package com.stream.domain.video.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateVideoCommand {
	private String fileTag;
	private String extension;
	private String path;
	private long size;
	private String description;
	private String memberEmail;

	@Builder
	public CreateVideoCommand(String fileTag, String extension, String path, long size, String description,
		String memberEmail) {
		this.fileTag = fileTag;
		this.extension = extension;
		this.path = path;
		this.size = size;
		this.description = description;
		this.memberEmail = memberEmail;
	}
}
