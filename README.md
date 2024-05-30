[![Actions Status](https://github.com/Hakky54/console-captor/workflows/Build/badge.svg)](https://github.com/Hakky54/console-captor/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Aconsolecaptor&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Aconsolecaptor)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Aconsolecaptor&metric=coverage)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Aconsolecaptor)
[![JDK Compatibility](https://img.shields.io/badge/JDK_Compatibility-8+-blue.svg)](#)
[![Kotlin Compatibility](https://img.shields.io/badge/Kotlin_Compatibility-1.5+-blue.svg)](#)
[![Scala Compatibility](https://img.shields.io/badge/Scala_Compatibility-2.11+-blue.svg)](#)
[![Android API Compatibility](https://img.shields.io/badge/Android_API_Compatibility-24+-blue.svg)](#)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Aconsolecaptor&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Aconsolecaptor)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Aconsolecaptor&metric=security_rating)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Aconsolecaptor)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Aconsolecaptor&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Aconsolecaptor)
[![Apache2 license](https://img.shields.io/badge/license-Aache2.0-blue.svg)](https://github.com/Hakky54/console-captor/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.hakky54/consolecaptor/badge.svg)](https://mvnrepository.com/artifact/io.github.hakky54/consolecaptor)
[![javadoc](https://javadoc.io/badge2/io.github.hakky54/consolecaptor/javadoc.svg)](https://javadoc.io/doc/io.github.hakky54/consolecaptor)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FHakky54%2Fconsole-captor.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FHakky54%2Fconsole-captor?ref=badge_shield)
[![Join the chat at https://gitter.im/hakky54/consolecaptor](https://badges.gitter.im/hakky54/consolecaptor.svg)](https://gitter.im/hakky54/consolecaptor?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Aconsolecaptor)

# ConsoleCaptor

# Install library with:
### Install with [maven](https://mvnrepository.com/artifact/io.github.hakky54/consolecaptor)
```xml
<dependency>
    <groupId>io.github.hakky54</groupId>
    <artifactId>consolecaptor</artifactId>
    <version>1.0.3</version>
    <scope>test</scope>
</dependency>
```
### Install with Gradle
```groovy
testImplementation 'io.github.hakky54:consolecaptor:1.0.3'
```
### Install with Scala SBT
```
libraryDependencies += "io.github.hakky54" % "consolecaptor" % "1.0.3" % Test
```
### Install with Apache Ivy
```xml
<dependency org="io.github.hakky54" name="consolecaptor" rev="1.0.3" />
```

## Table of contents
1. [Introduction](#introduction)
    - [Advantages](#advantages)
    - [Tested Java versions](#tested-java-versions)
2. [Usage](#usage)
    - [Capture Console Output](#capture-console-output)
    - [Reuse ConsoleCaptor for multiple tests](#initialize-consolecaptor-once-and-reuse-it-during-multiple-tests-with-clearoutput-method-within-the-aftereach-method)
3. [Contributing](#contributing)
4. [License](#license)

# Introduction
Hey, hello there 👋 Welcome. I hope you will like this library ❤️

ConsoleCaptor is a library which will enable you to easily capture the output of the console for unit testing purposes.

Do you want to capture logs? Please have a look at [LogCaptor](https://github.com/Hakky54/log-captor).

### Advantages
- No mocking required
- No custom JUnit extension required
- Plug & play
- Zero transitive dependencies

### Tested Java versions
- Java 8
- Java 11+

See the unit test [ConsoleCaptorShould](src/test/java/nl/altindag/console/ConsoleCaptorShould.java) for all the scenario's.

# Usage
##### Capture console output
```java
public class FooService {

    public void sayHello() {
        System.out.println("Keyboard not responding. Press any key to continue...");
        System.err.println("Congratulations, you are pregnant!");
    }

}
```
###### Unit test:

```java
import static org.assertj.core.api.Assertions.assertThat;

import nl.altindag.console.ConsoleCaptor;
import org.junit.jupiter.api.Test;

public class FooServiceShould {

    @Test
    public void captureStandardAndErrorOutput() {
        ConsoleCaptor consoleCaptor = new ConsoleCaptor();

        FooService fooService = new FooService();
        fooService.sayHello();

        assertThat(consoleCaptor.getStandardOutput()).contains("Keyboard not responding. Press any key to continue...");
        assertThat(consoleCaptor.getErrorOutput()).contains("Congratulations, you are pregnant!");
        
        consoleCaptor.close();
    }
}
```

##### Initialize ConsoleCaptor once and reuse it during multiple tests with clearOutput() method within the afterEach method:
```java
import nl.altindag.console.ConsoleCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public class FooServiceShould {

    private static ConsoleCaptor consoleCaptor;
    
    @BeforeAll
    public static void setupConsoleCaptor() {
        consoleCaptor = new ConsoleCaptor();
    }

    @AfterEach
    public void clearOutput() {
        consoleCaptor.clearOutput();
    }
    
    @AfterAll
    public static void tearDown() {
        consoleCaptor.close();
    }

    @Test
    public void captureStandardOutput() {
        FooService service = new FooService();
        service.sayHello();

        assertThat(consoleCaptor.getStandardOutput()).contains("Keyboard not responding. Press any key to continue...");
    }

    @Test
    public void captureErrorOutput() {
        FooService service = new FooService();
        service.sayHello();

        assertThat(consoleCaptor.getErrorOutput()).contains("Congratulations, you are pregnant!");
    }

}
```

# Contributing

There are plenty of ways to contribute to this project:

* Give it a star
* Join the [Gitter room](https://gitter.im/hakky54/consolecaptor) and leave a feedback or help with answering users questions
* Submit a PR

## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FHakky54%2Fconsole-captor.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FHakky54%2Fconsole-captor?ref=badge_large)
