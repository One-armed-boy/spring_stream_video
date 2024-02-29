package com.stream.domain.video;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.stream.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "videos")
@Getter
public class Video {
	@Id
	@Column(name = "video_id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@ManyToOne
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@Builder
	public Video(long id, String fileTag, String extension, String path, long size, String description, Member member) {
		this.id = id;
		this.fileTag = fileTag;
		this.extension = extension;
		this.path = path;
		this.size = size;
		this.description = description;
		this.setMember(member);
	}

	public void setMember(Member member) {
		if (this.member != null) {
			this.member.getVideoList().remove(this);
		}
		this.member = member;
		if (member == null) {
			return;
		}
		this.member.getVideoList().add(this);
	}
}

