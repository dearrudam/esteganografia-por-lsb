package io.github.dearrudam.esteganografiaporlsb.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ByteUtilsTest {

    @ParameterizedTest
    @MethodSource("testToBitsArgs")
    void testToBits(int input, int[] bitsEsperados) {
        Assertions.assertThat(ByteUtils.toBits(input))
                .containsExactly(bitsEsperados);
    }

    static Stream<Arguments> testToBitsArgs() {
        return Stream.of(
                Arguments.arguments(
                        'A',
                        new int[]{0, 1, 0, 0, 0, 0, 0, 1}
                ),
                Arguments.arguments(
                        'B',
                        new int[]{0, 1, 0, 0, 0, 0, 1, 0}
                )
        );
    }

}