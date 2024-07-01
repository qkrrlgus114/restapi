package com.park.restapi.util.slack;

import lombok.Builder;

@Builder
public record RequestInfo(
        String requestURI,
        String method
) {
}
