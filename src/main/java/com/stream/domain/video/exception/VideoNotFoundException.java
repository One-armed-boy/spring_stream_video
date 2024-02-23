package com.stream.domain.video.exception;

import com.stream.exception.BaseException;
import com.stream.exception.code.VideoExceptionCode;

public class VideoNotFoundException extends BaseException {
	public VideoNotFoundException(long id) {
		super(VideoExceptionCode.VIDEO_NOT_FOUND, "Can't find video in DB (key: id, value: " + id + " )");
	}
}
