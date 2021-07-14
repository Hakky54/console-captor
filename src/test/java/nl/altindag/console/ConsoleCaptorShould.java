/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.altindag.console;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hakan Altindag
 */
class ConsoleCaptorShould {

    @Test
    void captureOutStatements() {
        try (ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {
            FooService fooService = new FooService();
            fooService.sayHello();

            List<String> standardOutput = consoleCaptor.getStandardOutput();
            List<String> errorOutput = consoleCaptor.getErrorOutput();

            assertThat(standardOutput).contains("Hello there friend!", "How are you doing?");
            assertThat(errorOutput).contains("Congratulations, you are pregnant!", "It's a boy!");
        }
    }

    @Test
    void clearStatements() {
        try (ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {
            FooService fooService = new FooService();
            fooService.sayHello();

            List<String> standardOutput = consoleCaptor.getStandardOutput();
            List<String> errorOutput = consoleCaptor.getErrorOutput();

            assertThat(standardOutput).contains("Hello there friend!", "How are you doing?");
            assertThat(errorOutput).contains("Congratulations, you are pregnant!", "It's a boy!");

            consoleCaptor.clearOutput();

            assertThat(consoleCaptor.getStandardOutput()).isEmpty();
            assertThat(consoleCaptor.getErrorOutput()).isEmpty();
        }
    }


    static class FooService {

        public void sayHello() {
            System.out.println("Hello there friend!");
            System.out.println("How are you doing?");

            System.err.println("Congratulations, you are pregnant!");
            System.err.println("It's a boy!");
        }

    }
}
