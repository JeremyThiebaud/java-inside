package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

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
}