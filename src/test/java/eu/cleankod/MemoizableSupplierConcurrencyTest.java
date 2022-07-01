package eu.cleankod;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicLong;

public class MemoizableSupplierConcurrencyTest {
  private final AtomicLong initialValue = new AtomicLong(0L);
  private final MemoizableSupplier<Long> supplier = new MemoizableSupplier<>(initialValue::getAndIncrement);

  @Test(threadPoolSize = 100_000, invocationCount = 1_000)
  public void shouldNeverInvokeTheDelegateMoreThanOnce() {
    var value = supplier.get();

    assertThat(value).isZero();
  }
}
