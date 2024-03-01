package com.stream.domain.video;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stream.domain.member.Member;
import com.stream.domain.member.MemberService;
import com.stream.domain.video.dto.CreateVideoCommand;
import com.stream.domain.video.dto.VideoDto;
import com.stream.domain.video.exception.VideoNotFoundException;

@Service
@Transactional(readOnly = true)
public class VideoService {
	private final VideoRepository videoRepository;

	private final MemberService memberService;

	@Autowired
	public VideoService(VideoRepository videoRepository, MemberService memberService) {
		this.videoRepository = videoRepository;
		this.memberService = memberService;
	}

	public List<VideoDto> listVideo() {
		List<Video> videoList = videoRepository.findAll();
		return videoList.stream()
			.map(VideoDto::convertDomainToDto)
			.toList();
	}

	public VideoDto getVideoMetadata(long id) {
		Video video = getVideoById(id);
		return VideoDto.convertDomainToDto(video);
	}

	public String getVideoFilePath(long id) {
		Video video = getVideoById(id);
		return video.getPath();
	}

	private Video getVideoById(long id) {
		return videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException(id));
	}

	@Transactional
	public Video createVideo(CreateVideoCommand command) {
		var email = command.getMemberEmail();
		Member member =
			email == null ? null : memberService.getMemberByEmail(email);
		return videoRepository.save(Video.builder()
			.member(member)
			.fileTag(command.getFileTag())
			.extension(command.getExtension())
			.path(command.getPath())
			.size(command.getSize())
			.description(command.getDescription())
			.build());
	}
}
