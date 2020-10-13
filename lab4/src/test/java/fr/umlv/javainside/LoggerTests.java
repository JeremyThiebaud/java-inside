package fr.umlv.javainside;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTests {
    @Test
    void of() {
        class Foo {}
        var logger = Logger.of(Foo.class, __ -> {});
        assertNotNull(logger);
    }

    @Test
    void ofError() {
        class Foo {}
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> {
                    var logger = Logger.of(null, __ -> {});
                }),
                () -> assertThrows(NullPointerException.class, () -> {
                    var logger = Logger.of(Foo.class, null);
                })
        );
    }

    @Test
    void log() {
        class Foo {}
        var logger = Logger.of(Foo.class, msg ->
                assertEquals("Hello", msg)
        );
        logger.log("Hello");
    }

    @Test
    void logWithNull() {
        class Foo {}
        var logger = Logger.of(Foo.class, Assertions::assertNull);
        logger.log(null);
    }
}