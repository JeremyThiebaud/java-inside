package fr.umlv.javainside;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;

import static java.lang.invoke.MethodType.methodType;
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

    @Test @Tag("Q5")
    public void invokeExactVirtualTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        var result = (String) methodHandle.invokeExact("toto");
        assertEquals("TOTO", result);
    }

    @Test @Tag("Q5")
    public void invokeExactVirtualWrongArgumentTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        assertThrows(WrongMethodTypeException.class, methodHandle::invokeExact);
    }

    @Test @Tag("Q6")
    public void invokeStaticTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        assertAll(
                () -> assertEquals(13, (Integer) methodHandle.invoke("13")),
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    @SuppressWarnings("unused") var str = (String) methodHandle.invoke("13");
                })
        );
    }

    @Test @Tag("Q6")
    public void invokeVirtualTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        assertAll(
                () -> assertEquals("LAMBDA", (Object) methodHandle.invoke("lambda")),
                () -> assertThrows(ClassCastException.class, () -> {
                    @SuppressWarnings("unused") var number = (Double) methodHandle.invoke("lambda");
                })
        );
    }

    @Test @Tag("Q7")
    public void insertAndInvokeStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        var methodHandleEmpty = MethodHandles.insertArguments(methodHandle, 0, "123");
        assertAll(
                () -> assertEquals(123, methodHandleEmpty.invoke()),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleEmpty.invoke("123"))
        );
    }

    @Test @Tag("Q7")
    public void bindToAndInvokeVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        var methodHandleEmpty = methodHandle.bindTo("toto");
        assertAll(
                () -> assertEquals("TOTO", methodHandleEmpty.invoke()),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleEmpty.invoke("toto"))
        );
    }

    @Test @Tag("Q8")
    public void dropAndInvokeStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        var methodHandleMore = MethodHandles.dropArguments(methodHandle, 0, String.class);
        assertAll(
                () -> assertEquals(123, (int) methodHandleMore.invoke("789", "123")),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleMore.invoke("123"))
        );
    }

    @Test @Tag("Q8")
    public void dropAndInvokeVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        var methodHandleMore = MethodHandles.dropArguments(methodHandle, 0, String.class);
        assertAll(
                () -> assertEquals("TOTO", methodHandleMore.invoke("philip", "toto")),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleMore.invoke("toto"))
        );
    }

    @Test @Tag("Q9")
    public void asTypeAndInvokeVirtualTest() {

    }
}