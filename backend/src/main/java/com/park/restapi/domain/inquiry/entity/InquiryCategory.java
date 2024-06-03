package com.park.restapi.domain.inquiry.entity;

import lombok.Getter;

@Getter
public enum InquiryCategory {
	ACCOUNT("계정 관련 문의"),
	TECHNICAL("기술 관련 문의"),
	FEEDBACK("피드백 및 제안"),
	OTHER("기타 문의 사항");

	private final String description;

	InquiryCategory(String description) {
		this.description = description;
	}

}
