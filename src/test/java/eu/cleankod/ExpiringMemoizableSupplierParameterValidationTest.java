package eu.cleankod;

import org.testng.annotations.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ExpiringMemoizableSupplierParameterValidationTest {
    @Test
    public void shouldNotAcceptNullSupplier() {
        //when
        Throwable thrown = catchThrowable(() ->
                new ExpiringMemoizableSupplier<>(null, 100L, MILLISECONDS)
        );

        //then
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Provided delegate supplier cannot be null.");
    }

    @Test
    public void shouldNotAcceptNegativeDuration() {
        //when
        Throwable thrown = catchThrowable(() ->
                new ExpiringMemoizableSupplier<>(() -> null, -1, MILLISECONDS)
        );

        //then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duration must be a positive number.");
    }

    @Test
    public void shouldNotAcceptZeroDuration() {
        //when
        Throwable thrown = catchThrowable(() ->
                new ExpiringMemoizableSupplier<>(() -> null, 0, MILLISECONDS)
        );

        //then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duration must be a positive number.");
    }

    @Test
    public void shouldNotAcceptNullTimeUnit() {
        //when
        Throwable thrown = catchThrowable(() ->
                new ExpiringMemoizableSupplier<>(() -> null, 1L, null)
        );

        //then
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Provided time unit cannot be null.");
    }
}
