package com.park.restapi.domain.board.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TargetPostInfo {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private int likeCount;
    private int viewCount;
}
