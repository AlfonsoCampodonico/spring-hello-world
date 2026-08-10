# spring-hello-world

A minimal Spring Boot web app built with Maven, intended as a deploy target for
Laravel Cloud.

- **Java** 21
- **Spring Boot** 4.1.0
- **OpenAPI** springdoc 3.1.0 — spec generated from the controllers
- **Build** Maven via the wrapper (`./mvnw`) — no system Maven needed

## Endpoints

| Method | Path       | Description                                  |
| ------ | ---------- | -------------------------------------------- |
| `GET`  | `/`        | Greet via `?name=` query parameter           |
| `POST` | `/greet`   | Greet via a validated JSON body              |
| `GET`  | `/health`  | Liveness probe                               |

```bash
curl localhost:8080/
# {"message":"Hello, World!"}

curl "localhost:8080/?name=Ada"
# {"message":"Hello, Ada!"}

curl -X POST localhost:8080/greet \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alfonso","greeting":"Howdy"}'
# {"message":"Howdy, Alfonso!"}

curl -X POST localhost:8080/greet \
  -H 'Content-Type: application/json' -d '{"name":""}'
# 400 {"error":"validation_failed","fields":{"name":"must not be blank"}}

curl localhost:8080/health
# {"status":"ok"}
```

`name` is required and capped at 50 characters; `greeting` is optional, capped
at 30, and defaults to `Hello`.

## API docs

| Path              | What                                              |
| ----------------- | ------------------------------------------------- |
| `/swagger-ui.html`| Swagger UI — bundled as a webjar, works offline    |
| `/redoc`          | ReDoc — loads from CDN, pinned with an SRI hash    |
| `/v3/api-docs`    | Raw OpenAPI 3.1 JSON                              |

The spec is generated at runtime by reflecting over the controllers, so it
cannot drift from the code. The Jakarta Bean Validation annotations on
`GreetRequest` are the single source of truth for both the documented
constraints (`maxLength`, `required`) and the runtime enforcement.

`OpenApiDocsTest` asserts against the generated spec, so adding an undocumented
endpoint or dropping a constraint fails the build.

## Running locally

A JDK 21 is the only prerequisite. On macOS with Homebrew:

```bash
brew install openjdk@21
```

Homebrew keeps `openjdk@21` keg-only, so it is not on your `PATH`. Point
`JAVA_HOME` at it instead of editing your shell profile:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21
```

Then:

```bash
./mvnw test                     # run the test suite
./mvnw spring-boot:run          # run in dev mode on :8080
./mvnw package && java -jar target/app.jar
```

## Deploying

Build and start commands:

```bash
# build
./mvnw -B package -DskipTests

# start
java -jar target/app.jar
```

`<finalName>app</finalName>` in `pom.xml` pins the artifact to `target/app.jar`,
so the start command never needs a version glob.

Two settings in `src/main/resources/application.properties` matter for hosted
environments:

- `server.port=${PORT:8080}` — honours the port the platform injects, falling
  back to 8080 for local runs.
- `server.address=::` — binds the IPv6 any-address. Spring Boot otherwise binds
  `0.0.0.0` (IPv4 only), which leaves the app unreachable behind an IPv6-only or
  dual-stack router even though it appears to start correctly.

Note that `/redoc` fetches its JavaScript bundle from a CDN at view time. If the
deploy target blocks outbound requests from the browser, use `/swagger-ui.html`,
which is served entirely from the jar.

## Layout

```
src/main/java/com/alfonso/hello/
  Application.java          @SpringBootApplication, registers Greeter as a @Bean
  HelloController.java      HTTP adapter — parses requests, delegates, no logic
  Greeter.java              the greeting logic, zero Spring imports
  GreetRequest.java         POST body + validation constraints
  GreetResponse.java        \
  HealthResponse.java        > response schemas, named in the OpenAPI spec
  ErrorResponse.java        /
  ApiExceptionHandler.java  maps validation failures to the documented 400 body
  OpenApiConfig.java        spec title, version, description
  WebConfig.java            serves the ReDoc page at /redoc
src/main/resources/
  application.properties
  static/redoc.html
src/test/java/com/alfonso/hello/
  GreeterTest.java          plain JUnit, no application context
  HelloControllerTest.java  @WebMvcTest slice, asserts the JSON contract
  OpenApiDocsTest.java      asserts the generated spec stays complete
```

`Greeter` is registered as a `@Bean` in `Application` rather than annotated
`@Component`. Same dependency injection, but the class stays framework-agnostic
and its tests need no Spring context.

## Notes on Spring Boot 4

Two things that break Boot 3 habits:

- `spring-boot-test-autoconfigure` was split into per-technology modules and
  `spring-boot-starter-test` no longer pulls in every test slice. `@WebMvcTest`
  now needs the `spring-boot-webmvc-test` dependency and lives in
  `org.springframework.boot.webmvc.test.autoconfigure`.
- springdoc 2.x is compiled against Spring Framework 6 and does not work here.
  Boot 4 requires the springdoc **3.x** line, which is built against
  `spring-boot-starter-parent` 4.1.0.
