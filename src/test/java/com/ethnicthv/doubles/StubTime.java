package com.ethnicthv.doubles;

import com.ethnicth.Time;

public class StubTime extends Time {
    public StubTime(long second) {
        if(second < 0) throw new IllegalArgumentException();
        switch ((int) second) {
            case 90061 :
                setStubAttributes(1,1,1,1, second);
            case 2000 :
                setStubAttributes(0,0,33,20, second);
            case 5000 :
                setStubAttributes(0,1,23,20, second);
            case 200000 :
                setStubAttributes(2,7,33,20, second);
            case 20000000 :
                setStubAttributes(231,11,33,20, second);
            case 0 :
            default:
        }
    }

    private void setStubAttributes(long day, long hour, long minute, long second, long os) {
        Class<Time> c = Time.class;
        try {
            c.getDeclaredField("day").set(this, day);
            c.getDeclaredField("hour").set(this, hour);
            c.getDeclaredField("minute").set(this, minute);
            c.getDeclaredField("minute").set(this, second);
            c.getDeclaredField("os").set(this, os);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
