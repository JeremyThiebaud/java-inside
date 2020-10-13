package fr.umlv.javainside;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class LoggerBenchMark {
    class Foo {}
    class DisabledFoo {}
    final static Logger logger = Logger.of(Foo.class, message -> {/* empty */});
    final static Logger loggerLambda = Logger.lambdaOf(Foo.class, message -> {/* empty */});
    final static Logger loggerRecord = Logger.recordOf(Foo.class, message -> {/* empty */});
    final static Logger loggerDisabled = Logger.lambdaOf(DisabledFoo.class, message -> {/* empty */});
    static {
        Logger.Impl.enable(DisabledFoo.class, false);
    }

    @Benchmark
    public void no_op() {
        // empty
    }

    @Benchmark
    public void simple_logger() {
        logger.log("hello");
    }

    @Benchmark
    public void lambda_logger() { loggerLambda.log("hello"); }

    @Benchmark
    public void record_logger() {
        loggerRecord.log("hello");
    }

    @Benchmark
    public void disabled_logger() {
        loggerDisabled.log("hello");
    }
}
