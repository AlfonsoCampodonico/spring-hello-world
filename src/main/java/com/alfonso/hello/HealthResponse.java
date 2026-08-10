package com.alfonso.hello;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Liveness report")
public record HealthResponse(

        @Schema(description = "Always \"ok\" when the app is serving requests", example = "ok")
        String status) {
}
