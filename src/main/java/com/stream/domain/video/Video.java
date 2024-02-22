package com.stream.domain.video;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videos")
@NoArgsConstructor
@Getter
public class Video {
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "file_tag", nullable = false)
	private String fileTag;

	@Column(name = "extension", nullable = false)
	private String extension;

	@Column(name = "path", nullable = false)
	private String path;

	@Column(name = "size", nullable = false)
	private long size;

	@Column(name = "description")
	private String description;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@Builder
	public Video(long id, String fileTag, String extension, String path, long size, String description) {
		this.id = id;
		this.fileTag = fileTag;
		this.extension = extension;
		this.path = path;
		this.size = size;
		this.description = description;
	}
}
