package com.alfonso.hello;

/**
 * Builds greeting text. Deliberately free of Spring imports so it can be
 * unit tested without starting an application context.
 */
public class Greeter {

    static final String DEFAULT_NAME = "World";
    static final String DEFAULT_GREETING = "Hello";

    /**
     * Greets with the default salutation.
     *
     * @param name the name to greet; may be null, empty, or whitespace
     * @return the greeting text, never null
     */
    public String greet(String name) {
        return greet(name, null);
    }

    /**
     * @param name     the name to greet; may be null, empty, or whitespace
     * @param greeting the salutation to use; falls back to "Hello" when absent
     * @return the greeting text, never null
     */
    public String greet(String name, String greeting) {
        return blankTo(greeting, DEFAULT_GREETING) + ", " + blankTo(name, DEFAULT_NAME) + "!";
    }

    private static String blankTo(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value.strip();
    }
}
