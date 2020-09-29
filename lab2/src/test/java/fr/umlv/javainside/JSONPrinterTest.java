package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JSONPrinterTest {
    @Test
    public void toJSON() {
        var person = new Person("John", "Doe");
        assertEquals("java.lang.String firstName,java.lang.String lastName", JSONPrinter.toJSON(person));
        var alien = new Alien(100, "Saturn");
        assertEquals("int age,java.lang.String planet", JSONPrinter.toJSON(alien));
    }
}