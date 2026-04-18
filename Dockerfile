# ── Stage 1: Build ────────────────────────────────────────────
# We start with a Maven + JDK image. This image already has Maven
# and Java installed, so we can compile your code inside it.
# "AS build" names this stage so we can reference it later.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory inside the container.
# All subsequent commands run from here.
WORKDIR /app

# Copy pom.xml first (before source code).
# Why: Docker caches each step. If pom.xml hasn't changed,
# Docker won't re-download your dependencies on the next build.
# This makes re-deploys much faster.
COPY pom.xml .

# Download all Maven dependencies declared in pom.xml.
# -B = batch mode (no interactive prompts)
# dependency:resolve = download without compiling yet
RUN mvn -B dependency:resolve

# Now copy the actual source code.
COPY src ./src

# Compile and package into a JAR.
# -DskipTests = don't run tests during the build (faster)
# The JAR ends up at /app/target/wedtask-1.0.0.jar
RUN mvn -B clean package -DskipTests

# ── Stage 2: Run ──────────────────────────────────────────────
# This is a SEPARATE stage. We start fresh with a minimal JRE image.
# Why: The Maven/JDK image from Stage 1 is ~500MB. We don't need
# Maven or compilation tools at runtime. This JRE image is ~180MB.
# The final container image is much smaller.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy ONLY the compiled JAR from Stage 1 into this lean Stage 2.
# We leave all Maven caches and source code behind.
COPY --from=build /app/target/wedtask-0.0.1-SNAPSHOT.jar app.jar

# Tell Docker which port the app listens on.
# This is documentation only — it doesn't actually open the port.
# Render handles port routing separately.
EXPOSE 8080

# The command that runs when the container starts.
# "java -jar app.jar" is the same thing you run locally.
ENTRYPOINT ["java", "-jar", "app.jar"]