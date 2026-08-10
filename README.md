# spring-hello-world

A minimal Spring Boot web app built with Maven, intended as a deploy target for
Laravel Cloud.

- **Java** 21
- **Spring Boot** 4.1.0
- **Build** Maven via the wrapper (`./mvnw`) — no system Maven needed

## Endpoints

| Method | Path      | Response                        |
| ------ | --------- | ------------------------------- |
| `GET`  | `/`       | `{"message": "Hello, World!"}`  |
| `GET`  | `/?name=X`| `{"message": "Hello, X!"}`      |
| `GET`  | `/health` | `{"status": "ok"}`              |

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

Verify:

```bash
curl localhost:8080/            # {"message":"Hello, World!"}
curl "localhost:8080/?name=Ada" # {"message":"Hello, Ada!"}
curl localhost:8080/health      # {"status":"ok"}
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

## Layout

```
src/main/java/com/alfonso/hello/
  Application.java      @SpringBootApplication, registers Greeter as a @Bean
  HelloController.java  HTTP adapter — parses requests, delegates, no logic
  Greeter.java          the greeting logic, zero Spring imports
src/test/java/com/alfonso/hello/
  GreeterTest.java          plain JUnit, no application context
  HelloControllerTest.java  @WebMvcTest slice, asserts the JSON contract
```

`Greeter` is registered as a `@Bean` in `Application` rather than annotated
`@Component`. Same dependency injection, but the class stays framework-agnostic
and its tests need no Spring context.

## Note on Spring Boot 4

Boot 4 split `spring-boot-test-autoconfigure` into per-technology modules, and
`spring-boot-starter-test` no longer pulls in every test slice. `@WebMvcTest`
now requires the `spring-boot-webmvc-test` dependency and lives in
`org.springframework.boot.webmvc.test.autoconfigure` — Boot 3 imports will not
compile.
