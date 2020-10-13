package fr.umlv.javainside;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;
import java.util.function.Consumer;

public interface Logger {
    class Impl {
        private static final MethodHandle ACCEPT;
        static {
            try {
                ACCEPT = MethodHandles.lookup().findVirtual(
                        Consumer.class,
                        "accept",
                        MethodType.methodType(void.class, Object.class)
                );
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }

        private static final ClassValue<MutableCallSite> ENABLE_CALLSITES = new ClassValue<>() {
            protected MutableCallSite computeValue(Class<?> type) {
                return new MutableCallSite(MethodHandles.constant(boolean.class, true));
            }
        };

        public static void enable(Class<?> declaringClass, boolean enable) {
            ENABLE_CALLSITES.get(declaringClass).setTarget(MethodHandles.constant(boolean.class, enable));
        }

    }

    public void log(String message);

    public static Logger of(Class<?> declaringClass, Consumer<? super String> consumer) {
        Objects.requireNonNull(declaringClass);
        Objects.requireNonNull(consumer);
        var mh = createLoggingMethodHandle(declaringClass, consumer);
        return new Logger() {
            @Override
            public void log(String message) {
                try {
                    mh.invokeExact(message);
                } catch(RuntimeException | Error e) {
                    throw e;
                } catch(Throwable t) {
                    throw new UndeclaredThrowableException(t);
                }
            }
        };
    }

    public static Logger lambdaOf(Class<?> declaringClass, Consumer<? super String> consumer) {
        Objects.requireNonNull(declaringClass);
        Objects.requireNonNull(consumer);
        var mh = createLoggingMethodHandle(declaringClass, consumer);
        return (String message) -> {
                try {
                    mh.invokeExact(message);
                } catch(RuntimeException | Error e) {
                    throw e;
                } catch(Throwable t) {
                    throw new UndeclaredThrowableException(t);
                }
            };
    }

    public static Logger recordOf(Class<?> declaringClass, Consumer<? super String> consumer) {
        Objects.requireNonNull(declaringClass);
        Objects.requireNonNull(consumer);
        var mh = createLoggingMethodHandle(declaringClass, consumer);
        record loggerRecord(MethodHandle mh) implements Logger{
            @Override
            public void log(String message) {
                try {
                    mh.invokeExact(message);
                } catch(RuntimeException | Error e) {
                    throw e;
                } catch(Throwable t) {
                    throw new UndeclaredThrowableException(t);
                }
            }
        }
        return new loggerRecord(mh);
    }

//    private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
//        var mh = Impl.ACCEPT.bindTo(consumer);
//        return mh.asType(MethodType.methodType(void.class, String.class));
//    }
    private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
        var mh = MethodHandles.guardWithTest(
                Impl.ENABLE_CALLSITES.get(declaringClass).dynamicInvoker(),
                Impl.ACCEPT.bindTo(consumer).asType(MethodType.methodType(void.class, String.class)),
                MethodHandles.empty(MethodType.methodType(void.class, String.class))
        );
        return mh;
    }
}
