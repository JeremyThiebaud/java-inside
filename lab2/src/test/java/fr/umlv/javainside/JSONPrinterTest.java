package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

public class JSONPrinterTest {
    @Test
    public void toJSON() {
        var person = new Person("John", "Doe");
        assertEquals(
                IncompleteJSONParser.parse("{\"java.lang.String firstName\" : \"John\", " +
                "\"java.lang.String lastName\" : \"Doe\"}"),
                IncompleteJSONParser.parse(JSONPrinter.toJSON(person)));
        var alien = new Alien(100, "Saturn");
        assertEquals(
                IncompleteJSONParser.parse("{\"int age\" : 100, " +
                "\"java.lang.String planet\" : \"Saturn\"}"),
                IncompleteJSONParser.parse(JSONPrinter.toJSON(alien)));
    }

    public static record Alien(int age, String planet) {
        public Alien {
            if (age < 0) {
                throw new IllegalArgumentException("negative age");
            }
            requireNonNull(planet);
        }
    }

    public static record Person(String firstName, String lastName) {
        public Person {
            requireNonNull(firstName);
            requireNonNull(lastName);
        }
    }
}