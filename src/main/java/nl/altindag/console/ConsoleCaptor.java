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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Hakan Altindag
 */
public final class ConsoleCaptor implements AutoCloseable {

    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    private ByteArrayOutputStream outputStreamForOut;
    private ByteArrayOutputStream outputStreamForErr;
    private PrintStream consoleCaptorForOut;
    private PrintStream consoleCaptorForErr;

    public ConsoleCaptor() {
        createStreams();

        System.setOut(consoleCaptorForOut);
        System.setErr(consoleCaptorForErr);
    }

    private void createStreams() {
        outputStreamForOut = new ByteArrayOutputStream();
        outputStreamForErr = new ByteArrayOutputStream();

        consoleCaptorForOut = new PrintStream(outputStreamForOut);
        consoleCaptorForErr = new PrintStream(outputStreamForErr);
    }

    public List<String> getStandardOutput() {
        return getContent(outputStreamForOut);
    }

    public List<String> getErrorOutput() {
        return getContent(outputStreamForErr);
    }

    private List<String> getContent(ByteArrayOutputStream outputStream) {
        return Stream.of(outputStream.toString().split(System.lineSeparator()))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    /**
     * Clears all existing captured output
     */
    public void clearOutput() {
        closeExistingStreams();
        createStreams();
    }

    private void closeExistingStreams() {
        try {
            outputStreamForOut.flush();
            outputStreamForErr.flush();
            consoleCaptorForOut.flush();
            consoleCaptorForErr.flush();

            outputStreamForOut.close();
            outputStreamForErr.close();

            consoleCaptorForOut.close();
            consoleCaptorForErr.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        rollBackConfiguration();
        closeExistingStreams();
    }

    private void rollBackConfiguration() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

}
