package eu.cleankod;

import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiringMemoizableSupplierConcurrencyTest {
    private Long initialValue = 0L;
    private final ExpiringMemoizableSupplier<Long> supplier =
            new ExpiringMemoizableSupplier<>(() -> initialValue++, 100L, TimeUnit.SECONDS);

    @Test(threadPoolSize = 1000, invocationCount = 1001)
    public void shouldNotInvokeTheDelegateMoreThanOnce() {
        //when
        var value = supplier.get();

        //then
        assertThat(value).isZero();
        assertThat(initialValue).isOne();
    }
}
