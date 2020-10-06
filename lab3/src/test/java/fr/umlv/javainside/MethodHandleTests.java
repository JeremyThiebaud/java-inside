package fr.umlv.javainside;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;
import java.util.List;

import static java.lang.invoke.MethodType.methodType;
import static org.junit.jupiter.api.Assertions.*;

public class MethodHandleTests {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    @Test @Tag("Q2")
    public void findStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var methodHandle = LOOKUP.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        assertNotEquals(methodType(int.class), methodHandle.type());
        assertEquals(methodType(int.class, String.class), methodHandle.type());
    }

    @Test @Tag("Q3")
    public void findVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var methodHandle = LOOKUP.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        assertNotEquals(methodType(String.class), methodHandle.type());
        assertEquals(methodType(String.class, String.class), methodHandle.type());
    }

    @Test @Tag("Q4")
    public void invokeExactStaticTest() throws Throwable {
        var methodHandle = LOOKUP.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        var result = (int) methodHandle.invokeExact("42");
        assertEquals(42, result);
    }

    @Test @Tag("Q4")
    public void invokeExactStaticWrongArgumentTest() throws Throwable {
        var methodHandle = LOOKUP.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        assertThrows(WrongMethodTypeException.class, methodHandle::invokeExact);
    }

    @Test @Tag("Q5")
    public void invokeExactVirtualTest() throws Throwable {
        var methodHandle = LOOKUP.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        var result = (String) methodHandle.invokeExact("toto");
        assertEquals("TOTO", result);
    }

    @Test @Tag("Q5")
    public void invokeExactVirtualWrongArgumentTest() throws Throwable {
        var methodHandle = LOOKUP.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        assertThrows(WrongMethodTypeException.class, methodHandle::invokeExact);
    }

    @Test @Tag("Q6")
    public void invokeStaticTest() throws Throwable {
        var methodHandle = LOOKUP.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        assertAll(
                () -> assertEquals(13, Integer.valueOf((int)methodHandle.invokeExact("13"))),
                () -> assertThrows(WrongMethodTypeException.class, () -> {
                    @SuppressWarnings("unused") var str = (String) methodHandle.invokeExact("13");
                })
        );
    }

    @Test @Tag("Q6")
    public void invokeVirtualTest() throws Throwable {
        var methodHandle = LOOKUP.findVirtual(
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
        var methodHandle = LOOKUP.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        var methodHandleEmpty = MethodHandles.insertArguments(methodHandle, 0, "123");
        assertAll(
                () -> assertEquals(123, (int) methodHandleEmpty.invokeExact()),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleEmpty.invokeExact("123"))
        );
    }

    @Test @Tag("Q7")
    public void bindToAndInvokeVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var methodHandle = LOOKUP.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        var methodHandleEmpty = methodHandle.bindTo("toto");
        assertAll(
                () -> assertEquals("TOTO", (String) methodHandleEmpty.invokeExact()),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleEmpty.invokeExact("toto"))
        );
    }

    @Test @Tag("Q8")
    public void dropAndInvokeStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var methodHandle = LOOKUP.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        var methodHandleMore = MethodHandles.dropArguments(methodHandle, 0, String.class);
        assertAll(
                () -> assertEquals(123, (int) methodHandleMore.invokeExact("789", "123")),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleMore.invokeExact("123"))
        );
    }

    @Test @Tag("Q8")
    public void dropAndInvokeVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var methodHandle = LOOKUP.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));
        var methodHandleMore = MethodHandles.dropArguments(methodHandle, 0, String.class);
        assertAll(
                () -> assertEquals("TOTO", (String) methodHandleMore.invokeExact("philip", "toto")),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleMore.invokeExact("toto"))
        );
    }

    @Test @Tag("Q9")
    public void asTypeAndInvokeVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var methodHandle = LOOKUP.findStatic(
                Integer.class,
                "parseInt",
                methodType(int.class, String.class));
        var methodHandleInteger = methodHandle.asType(methodType(Integer.class, String.class));
        assertAll(
                () -> assertEquals(Integer.valueOf(123), (Integer) methodHandleInteger.invokeExact("123")),
                () -> assertThrows(WrongMethodTypeException.class, () -> methodHandleInteger.invokeExact("123"))
        );
    }

    @Test @Tag("Q10")
    public void invokeExactConstantTest() {
        var answerToLife = MethodHandles.constant(int.class, 42);
        assertAll(
                () -> assertEquals(42, (int) answerToLife.invokeExact()),
                () -> assertThrows(WrongMethodTypeException.class, () -> answerToLife.invokeExact("42"))
        );
    }

    private MethodHandle match(String str) throws NoSuchMethodException, IllegalAccessException {
        var equals = LOOKUP.findVirtual(
                String.class,
                "equals",
                methodType(boolean.class, Object.class));
        return MethodHandles.guardWithTest(
                MethodHandles.insertArguments(equals, 1, str),
                MethodHandles.dropArguments(MethodHandles.constant(int.class, 1), 0, String.class),
                MethodHandles.dropArguments(MethodHandles.constant(int.class, -1), 0, String.class)
        );
    }

    @Test @Tag("Q11")
    public void matchTest(){
        assertAll(
                () -> assertEquals(1, (int) match("key").invokeExact("key")),
                () -> assertEquals(-1, (int) match("kEy").invokeExact("Key"))
        );
    }

    private MethodHandle matchAll(List<String> texts) throws NoSuchMethodException, IllegalAccessException {
        var target = MethodHandles.dropArguments(MethodHandles.constant(int.class, -1), 0, String.class);
        var equals = LOOKUP.findVirtual(String.class, "equals", methodType(boolean.class, Object.class));
        int index = 0;
        for(var text:texts){
            var test = MethodHandles.insertArguments(equals, 1, text);
            var ok = MethodHandles.dropArguments(MethodHandles.constant(int.class, index), 0, String.class);
            target = MethodHandles.guardWithTest(test, ok, target);
            index++;
        }
        return target;
    }

    @Test @Tag("Q12")
    public void matchAllTest() {
        var list = List.of("hello", "toto", "lambda", "Delta", "tata");
        assertAll(
                () -> assertEquals(0, (int) matchAll(list).invokeExact("hello")),
                () -> assertEquals(1, (int) matchAll(list).invokeExact("toto")),
                () -> assertEquals(2, (int) matchAll(list).invokeExact("lambda")),
                () -> assertEquals(3, (int) matchAll(list).invokeExact("Delta")),
                () -> assertEquals(4, (int) matchAll(list).invokeExact("tata")),
                () -> assertEquals(-1, (int) matchAll(list).invokeExact("noKey"))
        );
    }
}