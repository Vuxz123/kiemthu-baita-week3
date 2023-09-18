package com.ethnicth;

public class DummyTel {
    private final Time timeStart;
    private final Time timeEnd;
    private final Time timeGap;
    private final double timeGapMinute;

    /**
     * Regular fee for 1 minute.
     */
    public static final double RFF1M = 0.5;

    public DummyTel(Time timeStart, Time timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        timeGap = timeEnd.subtractTime(timeStart);
        timeGapMinute = timeGap.toMinutes();
    }

    /**
     * calculate the phone changes the user have to pay.
     */
    public double calculatePhoneChanges() {
        /*
        Calculate Steps:
        1) calculate the real changes
        2) apply the discount during the discount period
        3) apply the discount from the lenght of the call
        4) apply tax
         */
        double realFee = calculateRealPhoneChanges();
        double feeStep2 = calculatePhoneChangesAfterDiscount1(realFee);
        double feeStep3 = calculatePhoneChangesAfterDiscount2(feeStep2);
        double finalFee = calculatePhoneChangesAfterTax(feeStep3);

        return finalFee;
    }

    /**
     * Calculating the phone changes before any other discount or tax applied.
     */
    private double calculateRealPhoneChanges() {
        return doRoundUp(timeGapMinute * RFF1M);
    }

    /**
     * Calculating the phone changes after the discount period's discount applied.
     * @param fee the phone changes from previous pass.
     * @return the phone changes after this pass.
     * <br>-if the phone was made in the period of 18:00-8:00, do 50% discount.
     * <br>-if the phone was made outside the period of 18:00-8:00, do nothing.
     */
    private double calculatePhoneChangesAfterDiscount1(double fee) {
        double output = fee;
        if(timeGap.compareTo(Time.DAY) < 0 && timeStart.getHour() >= 18 && timeGap.getHour() <= 14) {
            output /= 2;
            output = doRoundUp(output);
        }
        return output;
    }

    /**
     * Calculating the phone changes after the length's discount applied.
     * @param fee the phone changes from previous pass.
     * @return the phone changes after this pass.
     * <br>- if the phone's lenght greater than or equal to 60, do 5% discount.
     * <br>- if the phone's lenght smaller than 60, do nothing.
     */
    private double calculatePhoneChangesAfterDiscount2(double fee) {
        double output = fee;
        if(timeGapMinute >= 60) {
            output *= 0.85;
            output = doRoundUp(output);
        }
        return output;
    }

    /**
     * Calculating the phone changes after applying tax.
     * @param fee the phone changes from previous pass.
     * @return the phone changes after this pass.
     * -applying 5% tax to the overall fee.
     */
    private double calculatePhoneChangesAfterTax(double fee) {
        return doRoundUp(fee * 1.05);
    }

    /**
     * Custom Rounding function for DummyTel.
     * @param input the fee need to be rounded.
     * @return the input will be rounded by these rule:
     * <br>- If the decimal part is greater than or equal to .5, it will be rounded up.
     * <br>- If the decimal part is less than .5, it will be rounded down.
     */
    private double doRoundUp(double input) {
        double output;
        int n = (int) input;
        double c = input - n;
        output = n + (c>= 0.5 ? 1 : 0);
        return output;
    }
}
