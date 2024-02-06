package com.stream.storage.exception;

public class InvalidStorageConfigException extends RuntimeException {
	public InvalidStorageConfigException(String targetStorage) {
		super("Invalid configuration {my.storage} in application.yml" + " ( my.storage: " + targetStorage + " )");
	}
}
