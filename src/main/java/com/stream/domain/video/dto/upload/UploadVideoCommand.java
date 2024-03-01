package com.stream.domain.video.dto.upload;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UploadVideoCommand {
	private String fileName;
	private String extension;
	private String description;
	private String member;

	@Builder
	public UploadVideoCommand(String fileName, String extension, String description, String member) {
		this.fileName = fileName;
		this.extension = extension;
		this.description = description;
		this.member = member;
	}
}
