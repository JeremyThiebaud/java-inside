package fr.umlv.javainside;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;

import static java.lang.invoke.MethodType.methodType;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

public class MethodHandleTests {
    @Test @Tag("Q2")
    public void findStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        assertNotEquals(methodType(int.class), methodHandle.type());
        assertEquals(methodType(int.class, String.class), methodHandle.type());
    }

    @Test @Tag("Q3")
    public void findVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        assertNotEquals(methodType(String.class), methodHandle.type());
        assertEquals(methodType(String.class, String.class), methodHandle.type());
    }

    @Test @Tag("Q4")
    public void invokeExactStaticTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        var result = (int) methodHandle.invokeExact("42");
        assertEquals(42, result);
    }

    @Test @Tag("Q4")
    public void invokeExactStaticWrongArgumentTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        assertThrows(WrongMethodTypeException.class, methodHandle::invokeExact);
    }


}