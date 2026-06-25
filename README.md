# 🥒 Picklement

Picklement is a lightweight Java library that simplifies mapping data from Gherkin test scenarios to Java objects. It uses reflection and custom annotations to automatically map Gherkin data (like scenario names, steps, and tags) into a `TestCaseRecord` object, streamlining the process of BDD testing with tools like Cucumber.

## Features

- **Automatic Mapping:** Automatically maps Gherkin feature data to corresponding fields in your Java objects.
- **Annotation-Driven:** Use the simple `@GherkinMap` annotation to define mappings.
- **Reduces Boilerplate:** Eliminates the need for manual data extraction and mapping code in your step definitions.
- **Highly Customizable:** Easily extendable to support more complex mapping scenarios.

## How It Works

The core of Picklement is the `ReflectionMapper` class. It initializes by scanning a target class (e.g., `TestCaseRecord`) for fields annotated with `@GherkinMap`. When you pass Gherkin data to the `mapData` method, it uses this pre-built mapping to populate the object's fields.

### Example

Here's a quick look at how Picklement works.

**1. Gherkin Scenario (`login.feature`)**

```gherkin
Feature: Login Module
  As a user
  I want to log in
  So that I can access the dashboard

  @WEB @P0 @Smoke
  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters username "admin" and password "secret123"
    Then the dashboard should be displayed
    And the welcome message should say "Hello, admin"
```

**2. Java Data Object (`TestCaseRecord.java`)**

Your Java object uses `@GherkinMap` to specify which Gherkin data element maps to which field.

```java
import com.picklement.core.annotation.GherkinMap;
import lombok.Data;

@Data
public class TestCaseRecord {

    @GherkinMap(sourceKey = "SCENARIO_NAME", required = true)
    private String title;

    @GherkinMap(sourceKey = "FEATURE_NAME", required = true)
    private String feature;

    @GherkinMap(sourceKey = "GIVEN_STEPS")
    private String preconditions;

    @GherkinMap(sourceKey = "WHEN_STEPS")
    private String steps;

    @GherkinMap(sourceKey = "THEN_STEPS")
    private String expectedResults;

    @GherkinMap(sourceKey = "TAG_1")
    private String tag1; // e.g., @WEB

    @GherkinMap(sourceKey = "TAG_2")
    private String tag2; // e.g., @P0
}
```

**3. Mapping Logic**

Your test runner or a custom utility would extract the Gherkin data into a `Map<String, Object>` and then use `ReflectionMapper` to populate the `TestCaseRecord`.

```java
// Simplified example of how you might use it
public void processGherkinData() {
    // 1. Gherkin data is extracted into a Map
    Map<String, Object> gherkinData = new HashMap<>();
    gherkinData.put("SCENARIO_NAME", "Successful login with valid credentials");
    gherkinData.put("FEATURE_NAME", "Login Module");
    gherkinData.put("GIVEN_STEPS", "Given the user is on the login page");
    // ... and so on for other keys

    // 2. Create a target object and mapper
    TestCaseRecord testCase = new TestCaseRecord();
    ReflectionMapper mapper = new ReflectionMapper();

    // 3. Map the data
    mapper.mapData(testCase, gherkinData);

    // Now, testCase object is populated!
    // testCase.getTitle() -> "Successful login with valid credentials"
}
```

## Getting Started

To use Picklement in your project:

1.  **Include the Library:** Add the Picklement JAR to your project's classpath or install it from your Maven repository.
2.  **Annotate Your Model:** Annotate your test case data object with `@GherkinMap` as shown in the example above.
3.  **Integrate with Test Runner:** In your test execution logic, create an instance of `ReflectionMapper` and use it to map your Gherkin data.

## Building from Source

This project uses Apache Maven. To build the project, run:

```bash
mvn clean install
```

This will compile the source code, run tests, and package the library into a JAR file in the `target/` directory.
```