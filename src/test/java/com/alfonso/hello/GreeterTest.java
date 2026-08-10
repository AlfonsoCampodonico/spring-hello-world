package com.alfonso.hello;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * No Spring context here, so these run in milliseconds.
 */
class GreeterTest {

    private final Greeter greeter = new Greeter();

    @Test
    void greetsTheGivenName() {
        assertThat(greeter.greet("Alfonso")).isEqualTo("Hello, Alfonso!");
    }

    @Test
    void fallsBackToWorldWhenNameIsNull() {
        assertThat(greeter.greet(null)).isEqualTo("Hello, World!");
    }

    @Test
    void fallsBackToWorldWhenNameIsBlank() {
        assertThat(greeter.greet("   ")).isEqualTo("Hello, World!");
    }

    @Test
    void trimsSurroundingWhitespace() {
        assertThat(greeter.greet("  Ada  ")).isEqualTo("Hello, Ada!");
    }

    @Test
    void usesACustomGreeting() {
        assertThat(greeter.greet("Alfonso", "Howdy")).isEqualTo("Howdy, Alfonso!");
    }

    @Test
    void fallsBackToHelloWhenGreetingIsBlank() {
        assertThat(greeter.greet("Alfonso", "  ")).isEqualTo("Hello, Alfonso!");
    }

    @Test
    void appliesBothFallbacksAtOnce() {
        assertThat(greeter.greet(null, null)).isEqualTo("Hello, World!");
    }
}
