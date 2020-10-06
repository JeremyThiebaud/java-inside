package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodHandleTests {
    @Test
    public static void findStaticTest(){
        var lookup = MethodHandles.lookup();
    }
}