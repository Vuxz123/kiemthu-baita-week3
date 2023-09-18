package com.ethnicthv.unit;

import com.ethnicth.Time;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.stream.Stream;

public class TimeTests {

    @Nested
    @DisplayName("constructorTest")
    @Order(1)
    class ConstructorTest {
        //Failure at test input 5000
        /*
        org.opentest4j.AssertionFailedError: input: 5000 ==>
        Expected :1 hours, 23 minutes, 20 seconds
        Actual   :50 minutes, 0 seconds
        */
        //Updated: Fixed
        @ParameterizedTest
        @CsvFileSource(
                nullValues = "null",
                emptyValue = "empty",
                resources = "/time_test_from_second.csv",
                delimiter = ':',
                numLinesToSkip = 1
        )
        public void testTimeFromSecond(long input, String expected) {
            var actual = new Time(input).toString();
            Assertions.assertEquals(actual , expected, "input: " + input);
        }

        @Test
        public void testTimeFromSecondNegativeTestcase() {
            Assertions.assertThrows(IllegalArgumentException.class,() -> new Time(-1000));
        }

        @Test
        @DisplayName("Test for Debug the specific-5000-testcase")
        public void testTimeFromSecondDebugTestcase5000()  {
            Time time = new Time(5000);
            Class<? extends Time> timeClass = Time.class;
            try {
                Field second = timeClass.getDeclaredField("second");
                second.setAccessible(true);
                long vs = (long) second.get(time);
                Assertions.assertEquals(20, vs);

                Field minute = timeClass.getDeclaredField("minute");
                minute.setAccessible(true);
                long vm = (long) minute.get(time);
                Assertions.assertEquals(23, vm);

                Field hour = timeClass.getDeclaredField("hour");
                hour.setAccessible(true);
                long vh = (long) hour.get(time);
                Assertions.assertEquals(1, vh);

                Field day = timeClass.getDeclaredField("day");
                day.setAccessible(true);
                long vd = (long) day.get(time);
                Assertions.assertEquals(0, vd);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Nested
    @DisplayName("comparatorTest")
    @Order(2)
    class ComparatorTest {
        public static Stream<Arguments> providerTimeForTestTimeCompareOperation() {
            return Stream.of(
                    Arguments.of(
                            new Time(5000),
                            new Time(2000),
                            1
                    ),
                    Arguments.of(
                            new Time(1,2,3,0),
                            new Time(0,1,3,0),
                            1
                    ),
                    Arguments.of(
                            new Time(100),
                            new Time(1000),
                            -1
                    ),
                    Arguments.of(
                            new Time(1000),
                            new Time(1000),
                            0
                    ),
                    Arguments.of(
                            new Time(2000),
                            new Time(0,0,33,20),
                            0
                    ),
                    Arguments.of(
                            new Time(0,0,1,2),
                            new Time(0,0,1,2),
                            0
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("providerTimeForTestTimeCompareOperation")
        public void testTimeCompareOperation(Time input1, Time input2, int comparator) {
            Assertions.assertEquals(input1.compareTo(input2), comparator);
        }

        @Test
        public void testTimeCompareOperationNullTestcase() {
            Assertions.assertAll(
                    ()-> Assertions.assertThrows(IllegalArgumentException.class, () -> new Time(200).compareTo(null)),
                    ()-> Assertions.assertThrows(NullPointerException.class, () -> {
                        Time time = null;
                        time.subtractTime(new Time(2000));
                    })
                    );
        }
    }

    @Nested
    @DisplayName("cloneTest")
    @Order(3)
    class CloneTest {
        @Test
        public void testTimeCloneOperator1() {
            Time time1 = new Time(2000);
            Time time2 = time1.clone();
            Assertions.assertNotNull(time2);
            Assertions.assertNotSame(time1, time2);
        }

        @Test
        public void testTimeCloneOperator2() {
            Time time1 = new Time(2000);
            Time time2 = time1.clone();
            Assertions.assertNotNull(time2);
            Assertions.assertNotSame(time1, time2);
        }
    }

    @Nested
    @DisplayName("subtractionTest")
    @Order(4)
    class SubtractionTest {
        @ParameterizedTest
        @MethodSource("providerTimeForTestTimeSubtraction")
        public void testTimeSubtraction(Time input1, Time input2, Time expected) {
            Assertions.assertEquals(expected, input1.subtractTime(input2));
        }

        private static Stream<Arguments> providerTimeForTestTimeSubtraction() {
            return Stream.of(
                    Arguments.of(
                            new Time(1,1,1,1),
                            new Time(1,1,1,1),
                            new Time(0,0,0,0)
                    ),
                    Arguments.of(
                            new Time(1,2,3,4),
                            new Time(0,23,4,0),
                            new Time(0,2,59,4)
                    ),
                    Arguments.of(
                            new Time(1,2,3,4),
                            null,
                            new Time(1,2,3,4)
                    )
            );
        }

        @Test
        public void testTimeSubtractionNullTestcase() {
            Time time1 = new Time(2000);
            Time subtract = time1.subtractTime(null);
            Assertions.assertNotNull(subtract);
            Assertions.assertNotSame(time1, subtract);
        }
    }
}
