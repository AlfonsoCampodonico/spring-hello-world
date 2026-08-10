package com.alfonso.hello;

/**
 * Builds greeting text. Deliberately free of Spring imports so it can be
 * unit tested without starting an application context.
 */
public class Greeter {

    private static final String DEFAULT_NAME = "World";

    /**
     * @param name the name to greet; may be null, empty, or whitespace
     * @return the greeting text, never null
     */
    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, " + DEFAULT_NAME + "!";
        }
        return "Hello, " + name.strip() + "!";
    }
}
