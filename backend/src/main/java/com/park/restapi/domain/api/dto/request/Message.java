package com.park.restapi.domain.api.dto.request;

import lombok.Builder;

@Builder
public record Message(String role, String content) {
}
