package com.ethnicth;

import java.util.Objects;

public class Time implements Comparable<Time>, Cloneable {
    public static final Time YEAR = new Time(31556926);
    public static final Time MONTH = new Time(2629743);
    public static final Time WEEK = new Time(604800);
    public static final Time DAY = new Time(86400);
    public static final Time HOUR = new Time(3600);
    public static final Time MINUTE = new Time(60);
    public static final Time SECOND = new Time(1);
    private long day = 0;
    private long hour = 0;
    private long minute = 0;
    private long second = 0;
    private long os = 0;

    public Time(long day, long hour, long minute, long second) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        os = day * 86400 + hour * 3600 + minute * 60 + second;
    }

    public Time() {
    }

    public Time(Time cloneFrom) {
        this(cloneFrom.os);
    }

    public Time(long second) {
        if(second < 0) throw new IllegalArgumentException("second cannot be negative");
        this.os = second;
        this.second = second % 60;
        var om = (second - this.second) / 60;
        this.minute = om % 60;
        var oh = (om - this.minute)/ 60;
        this.hour = oh % 24;
        this.day = (oh - this.hour) / 24;
    }

    /**
     * A Method convert Time to Minutes
     * @return the minute of this Time, The number of seconds will be converted to a comma after the number of minutes
     */
    public double toMinutes() {
        return ((double) os) / 60;
    }

    /**
     * Subtract time
     * @param other the other Time be used in subtraction, can be null or greater than the current instance of Time
     * @return the Time gap between 2 Time.
     * <br> - if other is null, then make it as a Time.Zero and forward.
     * <br> - if other is greater, then flip the subtraction and forward (to assert that Time is always positive).
     */
    public Time subtractTime(Time other) {
        if(other == null) return new Time(this.os);
        var c = this.compareTo(other);
        return new Time(c * (this.os - other.os));
    }

    public long getDay() {
        return day;
    }

    public long getHour() {
        return hour;
    }

    public long getMinute() {
        return minute;
    }

    public long getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return (day == 0 ? "" : day + " days, ") +
                (hour == 0 ? "" : hour + " hours, " ) +
                ( minute + " minutes, ") +
                ( second + " seconds");
    }

    @Override
    public int compareTo(Time other) {
        if(other == null) throw new IllegalArgumentException("other is null!");
        return Long.compare(this.os, other.os);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return day == time.day && hour == time.hour && minute == time.minute && second == time.second && os == time.os;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, minute, second, os);
    }

    @Override
    public Time clone() {
        try {
            return (Time) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
