package eu.cleankod;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoizableSupplierConcurrencyTest {
    private Long initialValue = 0L;
    private final MemoizableSupplier<Long> supplier = new MemoizableSupplier<>(() -> initialValue++);

    @Test(threadPoolSize = 1000, invocationCount = 1001)
    public void shouldNeverInvokeTheDelegateMoreThanOnce() {
        //when
        var value = supplier.get();

        //then
        assertThat(value).isZero();
        assertThat(initialValue).isOne();
    }
}
