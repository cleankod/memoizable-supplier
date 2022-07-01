package eu.cleankod;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class MemoizableSupplier<T> implements Supplier<T> {
    private final Supplier<T> delegate;
    private final AtomicReference<T> suppliedValue = new AtomicReference<>();

    public MemoizableSupplier(Supplier<T> delegate) {
        this.delegate = delegate;
    }

    public T get() {
        T currentValue = this.suppliedValue.get();
        if (currentValue == null) {
            synchronized(this.suppliedValue) {
                currentValue = this.suppliedValue.get();
                if (currentValue == null) {
                    currentValue = Objects.requireNonNull(delegate.get(), "Delegate supplier returned null.");
                    this.suppliedValue.set(currentValue);
                }
            }
        }
        return currentValue;
    }
}
