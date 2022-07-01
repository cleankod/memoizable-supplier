package eu.cleankod;

import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiringMemoizableSupplierExpirationTest {
    public static final long DURATION = 100L;
    private Long initialValue = 0L;
    private final ExpiringMemoizableSupplier<Long> supplier =
            new ExpiringMemoizableSupplier<>(() -> initialValue++, DURATION, TimeUnit.MILLISECONDS);

    @Test
    public void shouldInvokeTheDelegateAfterATimeout() throws Exception {
        //when
        var value1 = supplier.get();
        var value2 = supplier.get();

        //and
        TimeUnit.MILLISECONDS.sleep(DURATION);

        //and
        var value3 = supplier.get();
        var value4 = supplier.get();

        //then
        assertThat(value1).isZero();
        assertThat(value2).isZero();
        assertThat(value3).isOne();
        assertThat(value4).isOne();

        assertThat(initialValue).isEqualTo(2L);
    }
}
