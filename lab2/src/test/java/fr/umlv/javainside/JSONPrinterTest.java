package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

public class JSONPrinterTest {
    @Test
    public void toJSON() {
        var person = new Person("John", "Doe");
        assertEquals("java.lang.String firstName : John,\n" +
                "java.lang.String lastName : Doe", JSONPrinter.toJSON(person));
        var alien = new Alien(100, "Saturn");
        assertEquals("int age : 100,\n" +
                "java.lang.String planet : Saturn", JSONPrinter.toJSON(alien));
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