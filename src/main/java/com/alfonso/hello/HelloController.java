package com.alfonso.hello;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP adapter. Parses the request and delegates; holds no logic of its own.
 */
@RestController
@Tag(name = "Greetings", description = "Say hello")
public class HelloController {

    private final Greeter greeter;

    HelloController(Greeter greeter) {
        this.greeter = greeter;
    }

    @GetMapping("/")
    @Operation(summary = "Greet by query parameter",
            description = "Returns a greeting. Omit `name` to greet the world.")
    @ApiResponse(responseCode = "200", description = "The greeting")
    GreetResponse hello(
            @Parameter(description = "Who to greet", example = "Alfonso")
            @RequestParam(required = false) String name) {
        return new GreetResponse(greeter.greet(name));
    }

    @PostMapping("/greet")
    @Operation(summary = "Greet by request body",
            description = "Returns a greeting built from a validated JSON body, "
                    + "optionally with a custom salutation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The greeting"),
            @ApiResponse(responseCode = "400", description = "The body failed validation",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GreetResponse greet(@Valid @RequestBody GreetRequest request) {
        return new GreetResponse(greeter.greet(request.name(), request.greeting()));
    }

    @GetMapping("/health")
    @Operation(summary = "Liveness probe")
    @ApiResponse(responseCode = "200", description = "The app is serving requests")
    HealthResponse health() {
        return new HealthResponse("ok");
    }
}
