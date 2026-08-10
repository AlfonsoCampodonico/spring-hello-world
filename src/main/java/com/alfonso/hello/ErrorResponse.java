package com.alfonso.hello;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Returned when a request fails validation")
public record ErrorResponse(

        @Schema(description = "Machine-readable error code", example = "validation_failed")
        String error,

        @Schema(description = "Per-field messages, keyed by field name",
                example = "{\"name\": \"must not be blank\"}")
        Map<String, String> fields) {
}
