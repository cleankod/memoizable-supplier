package eu.cleankod;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class MemoizableSupplier<T> implements Supplier<T> {
  private final Supplier<T> delegate;
  private final AtomicReference<T> value = new AtomicReference<>();

  public MemoizableSupplier(Supplier<T> delegate) {
    this.delegate = delegate;
  }

  public T get() {
    if (this.value.get() == null) {
      synchronized (this) {
        T newValue = this.delegate.get();
        if (this.value.compareAndSet(null, newValue)) {
          return newValue;
        } else {
          return this.value.get();
        }
      }
    }
    return this.value.get();
  }
}
