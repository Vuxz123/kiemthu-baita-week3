package com.ethnicthv.unit;

import com.ethnicth.DummyTel;
import com.ethnicth.Time;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * Test 4 Steps of Calculating DummyTel Phone Changes:
 * <br> - TestRealPhoneChanges: test the method calculate real changes.
 * <br> - TestDiscount1PhoneChanges: test the method calculate fee after applying discount during discount period.
 * <br> - TestDiscount2PhoneChanges: test the method calculate fee after applying discount from the phone call's lenght.
 * <br> - TestTaxPhoneChanges: test the method calculate fee after applying tex.
 */
public class DummyTelTests {
    /**
     * TestSuite:
     * <br>-Testcase 1: test if the method provide the correct changes from time provided
     */
    @Nested
    @DisplayName("real changes")
    class TestRealPhoneChanges {
        private static Stream<Arguments> testRealPhoneChangesDataProvider(){
            return Stream.of(
                    Arguments.of(
                            new Time(0,18,0,0),
                            new Time(1,8,0,0),
                            420.0
                    ),
                    Arguments.of(
                            new Time(0,8,0,0),
                            new Time(0,9,0,0),
                            30
                    ),
                    Arguments.of(
                            new Time(0,10,0,0),
                            new Time(0,12,0,0),
                            60
                    ),
                    Arguments.of(
                            new Time(0, 16, 0, 0),
                            new Time(0,20,0,0),
                            120
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 10),
                            new Time(0,18,40,30),
                            10
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(0,18,40,30),
                            10
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(1,2,59,19),
                            259
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("testRealPhoneChangesDataProvider")
        public void testRealPhoneChanges(Time start, Time end, double expectedFee) {
            try {
                DummyTel dummyTel = new DummyTel(start, end);
                double actualFee;
                Class<DummyTel> dummyTelClass = DummyTel.class;
                var method = dummyTelClass.getDeclaredMethod("calculateRealPhoneChanges");
                method.setAccessible(true);
                actualFee = (double) method.invoke(dummyTel);
                Assertions.assertEquals(expectedFee, actualFee);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * TestSuite:
     * <br>-Testcase 1: If the Time is in the 18:00-08:00 period, has it been applied the discount ?</br>
     * <br>-Testcase 2: If the Time is out of the discount period, was the discount applied to the fee ?
     */
    @Nested
    @DisplayName("discount 1 changes")
    class TestDiscount1PhoneChanges {
        private static Stream<Arguments> testcase2DataProvider(){
            return Stream.of(
                    Arguments.of(
                            new Time(0,18,0,0),
                            new Time(1,9,0,0),
                            210.0,
                            210.0
                    ),
                    Arguments.of(
                            new Time(0,8,0,0),
                            new Time(0,9,0,0),
                            30,
                            30
                    ),
                    Arguments.of(
                            new Time(0,10,0,0),
                            new Time(0,12,0,0),
                            60,
                            60
                    ),
                    Arguments.of(
                            new Time(0, 16, 0, 0),
                            new Time(0,20,0,0),
                            120,
                            120
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 10),
                            new Time(1,10,40,30),
                            10,
                            10
                    ),
                    Arguments.of(
                            new Time(0, 10, 20, 59),
                            new Time(0,18,40,30),
                            20,
                            20
                    ),
                    Arguments.of(
                            new Time(0, 9, 20, 59),
                            new Time(1,2,59,19),
                            259,
                            259
                    )
            );
        }

        private static Stream<Arguments> testcase1DataProvider(){
            return Stream.of(
                    Arguments.of(
                            new Time(0,18,0,0),
                            new Time(1,8,0,0),
                            210.0,
                            105.0
                    ),
                    Arguments.of(
                            new Time(0,19,0,0),
                            new Time(1,2,0,0),
                            210,
                            105
                    ),
                    Arguments.of(
                            new Time(0,20,0,0),
                            new Time(1,3,0,0),
                            60,
                            30
                            ),
                    Arguments.of(
                            new Time(0, 19, 0, 0),
                            new Time(0,20,0,0),
                            120,
                            60
                            ),
                    Arguments.of(
                            new Time(0, 18, 20, 10),
                            new Time(0,18,40,30),
                            10,
                            5
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(0,18,40,30),
                            20,
                            10
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(1,2,59,19),
                            200,
                            100
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("testcase1DataProvider")
        @DisplayName("branch 'In Discount Period")
        public void testcase1(Time start, Time end,double input, double expectedFee) {
            try {
                DummyTel dummyTel = new DummyTel(start, end);
                double actualFee;
                Class<DummyTel> dummyTelClass = DummyTel.class;
                var methods = dummyTelClass.getDeclaredMethods();
                Method method = null;
                for(Method m : methods) {
                    if(m.getName().equals("calculatePhoneChangesAfterDiscount1")) {
                        method = m;
                        break;
                    }
                }
                if(method == null) throw new NoSuchMethodException();
                method.setAccessible(true);
                actualFee = (double) method.invoke(dummyTel, input);
                Assertions.assertEquals(expectedFee, actualFee);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @ParameterizedTest
        @MethodSource("testcase2DataProvider")
        @DisplayName("branch 'Outside Discount Period")
        public void testcase2(Time start, Time end,double input, double expectedFee) {
            try {
                DummyTel dummyTel = new DummyTel(start, end);
                double actualFee;
                Class<DummyTel> dummyTelClass = DummyTel.class;
                var methods = dummyTelClass.getDeclaredMethods();
                Method method = null;
                for(Method m : methods) {
                    if(m.getName().equals("calculatePhoneChangesAfterDiscount1")) {
                        method = m;
                        break;
                    }
                }
                if(method == null) throw new NoSuchMethodException();
                method.setAccessible(true);
                actualFee = (double) method.invoke(dummyTel, input);
                Assertions.assertEquals(expectedFee, actualFee);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * TestSuite:
     * <br>-Testcase 1: if the phone length is above 60 min, did is discount.
     * <br>-Testcase 2: if the phone length is below 60 min, did is discount.
     */
    @Nested
    @DisplayName("discount 2 changes")
    class TestDiscount2PhoneChanges {
        private static Stream<Arguments> testcase1DataProvider(){
            return Stream.of(
                    Arguments.of(
                            new Time(0,18,0,0),
                            new Time(1,9,0,0),
                            210.0,
                            179.0
                    ),
                    Arguments.of(
                            new Time(0,8,0,0),
                            new Time(0,9,0,0),
                            30,
                            26
                    ),
                    Arguments.of(
                            new Time(0,10,0,0),
                            new Time(0,12,0,0),
                            60,
                            51
                    ),
                    Arguments.of(
                            new Time(0, 16, 0, 0),
                            new Time(0,20,0,0),
                            120,
                            102
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 10),
                            new Time(1,10,40,30),
                            10,
                            9
                    ),
                    Arguments.of(
                            new Time(0, 10, 20, 59),
                            new Time(0,18,40,30),
                            20,
                            17
                    ),
                    Arguments.of(
                            new Time(0, 9, 20, 59),
                            new Time(1,2,59,19),
                            259,
                            220
                    )
            );
        }

        private static Stream<Arguments> testcase2DataProvider(){
            return Stream.of(
                    Arguments.of(
                            new Time(0,18,0,0),
                            new Time(0,18,20,0),
                            210.0,
                            210.0
                    ),
                    Arguments.of(
                            new Time(0,19,0,0),
                            new Time(0,19,59,59),
                            210,
                            210
                    ),
                    Arguments.of(
                            new Time(0,20,0,0),
                            new Time(0,20,40,0),
                            60,
                            60
                    ),
                    Arguments.of(
                            new Time(0, 19, 0, 0),
                            new Time(0,19,30,22),
                            120,
                            120
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 10),
                            new Time(0,18,40,30),
                            10,
                            10
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(0,18,40,30),
                            20,
                            20
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(0,18,59,19),
                            200,
                            200
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("testcase1DataProvider")
        @DisplayName("branch 'Above 60 min'")
        public void testcase1(Time start, Time end,double input, double expectedFee) {
            try {
                DummyTel dummyTel = new DummyTel(start, end);
                double actualFee;
                Class<DummyTel> dummyTelClass = DummyTel.class;
                var methods = dummyTelClass.getDeclaredMethods();
                Method method = null;
                for(Method m : methods) {
                    if(m.getName().equals("calculatePhoneChangesAfterDiscount2")) {
                        method = m;
                        break;
                    }
                }
                if(method == null) throw new NoSuchMethodException();
                method.setAccessible(true);
                actualFee = (double) method.invoke(dummyTel, input);
                Assertions.assertEquals(expectedFee, actualFee);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @ParameterizedTest
        @MethodSource("testcase2DataProvider")
        @DisplayName("branch 'Below 60 min'")
        public void testcase2(Time start, Time end,double input, double expectedFee) {
            try {
                DummyTel dummyTel = new DummyTel(start, end);
                double actualFee;
                Class<DummyTel> dummyTelClass = DummyTel.class;
                var methods = dummyTelClass.getDeclaredMethods();
                Method method = null;
                for(Method m : methods) {
                    if(m.getName().equals("calculatePhoneChangesAfterDiscount2")) {
                        method = m;
                        break;
                    }
                }
                if(method == null) throw new NoSuchMethodException();
                method.setAccessible(true);
                actualFee = (double) method.invoke(dummyTel, input);
                Assertions.assertEquals(expectedFee, actualFee);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * TestSuite:
     * <br>-Testcase 1: if the tax was applied correctly
     */
    @Nested
    @DisplayName("tax changes")
    class TestTaxPhoneChanges {
        private static Stream<Arguments> testcase1PhoneChangesDataProvider(){
            return Stream.of(
                    Arguments.of(
                            new Time(0,18,0,0),
                            new Time(1,8,0,0),
                            210.0,
                            221.0
                    ),
                    Arguments.of(
                            new Time(0,8,0,0),
                            new Time(0,9,0,0),
                            30,
                            32
                    ),
                    Arguments.of(
                            new Time(0,10,0,0),
                            new Time(0,12,0,0),
                            60,
                            63
                    ),
                    Arguments.of(
                            new Time(0, 16, 0, 0),
                            new Time(0,20,0,0),
                            120,
                            126
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 10),
                            new Time(0,18,40,30),
                            10,
                            11
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(0,18,40,30),
                            20,
                            21
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(1,2,59,19),
                            259,
                            272
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("testcase1PhoneChangesDataProvider")
        public void testPhoneChangesAfterTax(Time start, Time end, double input, double expectedFee) {
            try {
                DummyTel dummyTel = new DummyTel(start, end);
                double actualFee;
                Class<DummyTel> dummyTelClass = DummyTel.class;
                var methods = dummyTelClass.getDeclaredMethods();
                Method method = null;
                for(Method m : methods) {
                    if(m.getName().equals("calculatePhoneChangesAfterTax")) {
                        method = m;
                        break;
                    }
                }
                if(method == null) throw new NoSuchMethodException();
                method.setAccessible(true);
                actualFee = (double) method.invoke(dummyTel, input);
                Assertions.assertEquals(expectedFee, actualFee);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * TestSuite:
     * <br>-Testcase 1: if the phone changes pass all pass correctly
     */
    @Nested
    @DisplayName("phone changes")
    class TestPhoneChanges {
        private static Stream<Arguments> testPhoneChangesDataProvider(){
            return Stream.of(
                    Arguments.of(
                            new Time(0,18,0,0),
                            new Time(1,8,0,0),
                            //420.0
                            188.0
                    ),
                    Arguments.of(
                            new Time(0,8,0,0),
                            new Time(0,9,0,0),
                            //30
                            27.0
                    ),
                    Arguments.of(
                            new Time(0,10,0,0),
                            new Time(0,12,0,0),
                            //60
                            54.0
                    ),
                    Arguments.of(
                            new Time(0, 16, 0, 0),
                            new Time(0,20,0,0),
                            //120
                            107.0
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 10),
                            new Time(0,18,40,30),
                            //10
                            5.0
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(0,18,40,30),
                            //10
                            5.0
                    ),
                    Arguments.of(
                            new Time(0, 18, 20, 59),
                            new Time(1,2,59,19),
                            //259
                            117
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("testPhoneChangesDataProvider")
        public void testPhoneChanges(Time start, Time end, double expectedFee) {
            DummyTel dummyTel = new DummyTel(start,end);
            Assertions.assertEquals(expectedFee,dummyTel.calculatePhoneChanges());
        }
    }
}
