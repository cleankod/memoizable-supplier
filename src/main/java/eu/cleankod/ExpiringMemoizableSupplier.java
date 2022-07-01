package eu.cleankod;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ExpiringMemoizableSupplier<T> implements Supplier<T> {
    private final Supplier<T> delegate;
    private final long durationNanos;
    private long expirationNanos;

    private final AtomicReference<T> suppliedValue = new AtomicReference<>();

    public ExpiringMemoizableSupplier(Supplier<T> delegate, long duration, TimeUnit timeUnit) {
        durationNanos = convertToNanos(duration, timeUnit);
        this.delegate = Objects.requireNonNull(delegate, "Provided delegate supplier cannot be null.");
    }

    @Override
    public T get() {
        long nanos = expirationNanos;
        long now = System.nanoTime();
        if (this.suppliedValue.get() == null || now - nanos >= 0) {
            synchronized (this.suppliedValue) {
                if (nanos == expirationNanos) {
                    T actualValue = Objects.requireNonNull(delegate.get(), "Delegate supplier returned null.");
                    this.suppliedValue.set(actualValue);
                    nanos = now + durationNanos;
                    expirationNanos = nanos;
                    return actualValue;
                }
            }
        }
        return this.suppliedValue.get();
    }

    private long convertToNanos(long duration, TimeUnit timeUnit) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be a positive number.");
        }
        TimeUnit givenTimeUnit = Objects.requireNonNull(timeUnit, "Provided time unit cannot be null.");
        return givenTimeUnit.toNanos(duration);
    }
}