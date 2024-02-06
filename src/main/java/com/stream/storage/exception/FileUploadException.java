package com.stream.storage.exception;

public class FileUploadException extends RuntimeException {
	public FileUploadException(Exception cause) {
		super("Occur Exception while file uploaded.", cause);
	}
}
