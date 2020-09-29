package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

public class JSONPrinterTest {
    @Test
    public void toJSON() {
        var person = new Person("John", "Doe");
        assertEquals(
                IncompleteJSONParser.parse("{\"first-name\" : \"John\", " +
                "\"last-name\" : \"Doe\"}"),
                IncompleteJSONParser.parse(JSONPrinter.toJSON(person)));
        var alien = new Alien(100, "Saturn");
        assertEquals(
                IncompleteJSONParser.parse("{\"int age\" : 100, \"planet\" : \"Saturn\"}"),
                IncompleteJSONParser.parse(JSONPrinter.toJSON(alien)));
    }

    public static record Alien(int age, @JSONProperty String planet) {
        public Alien {
            if (age < 0) {
                throw new IllegalArgumentException("negative age");
            }
            requireNonNull(planet);
        }
    }

    public static record Person(@JSONProperty String first_name,
                                @JSONProperty String last_name) {
        public Person {
            requireNonNull(first_name);
            requireNonNull(last_name);
        }
    }
}