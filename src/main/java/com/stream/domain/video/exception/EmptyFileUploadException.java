package com.stream.domain.video.exception;

import com.stream.exception.BaseException;
import com.stream.exception.code.VideoExceptionCode;

public class EmptyFileUploadException extends BaseException {
	public EmptyFileUploadException() {
		super(VideoExceptionCode.UPLOAD_EMPTY_FILE, "video file in upload request maybe empty");
	}
}
