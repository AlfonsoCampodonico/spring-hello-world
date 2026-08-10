package com.alfonso.hello;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Body of {@code POST /greet}. The validation constraints below are read by
 * springdoc, so they show up in the OpenAPI schema as well as being enforced
 * at runtime.
 */
@Schema(description = "A request for a personalised greeting")
public record GreetRequest(

        @Schema(description = "Who to greet", example = "Alfonso", maxLength = 50)
        @NotBlank(message = "must not be blank")
        @Size(max = 50, message = "must be at most 50 characters")
        String name,

        @Schema(description = "Salutation to use. Defaults to \"Hello\".",
                example = "Howdy", maxLength = 30, nullable = true)
        @Size(max = 30, message = "must be at most 30 characters")
        String greeting) {
}
