package com.stream.domain.video.exception;

public class VideoNotFoundException extends RuntimeException {
	public VideoNotFoundException(long id) {
		super("Can't find video in DB (key: id, value: " + id + " )");
	}
}
