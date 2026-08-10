package com.alfonso.hello;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A greeting")
public record GreetResponse(

        @Schema(description = "The greeting text", example = "Hello, Alfonso!")
        String message) {
}
