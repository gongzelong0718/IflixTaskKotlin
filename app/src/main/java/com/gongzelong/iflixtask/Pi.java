package com.gongzelong.iflixtask;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_HALF_UP;

class Pi {
    private OnPiChangedInterface mOnPiChangedInterface;

    interface OnPiChangedInterface {
        void onPiChanged(String pi);
    }

    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal FOUR = new BigDecimal(4);
    private String mNumber;

    void pi(final int scale) {
        BigDecimal a = ONE;
        BigDecimal b = ONE.divide(sqrt(TWO, scale), scale, ROUND_HALF_UP);
        BigDecimal t = new BigDecimal(0.25);
        BigDecimal x = ONE;
        BigDecimal y;

        while (!a.equals(b)) {
            y = a;
            a = a.add(b).divide(TWO, scale, ROUND_HALF_UP);
            b = sqrt(b.multiply(y), scale);
            t = t.subtract(x.multiply(y.subtract(a).multiply(y.subtract(a))));
            x = x.multiply(TWO);
        }

        BigDecimal pi = a.add(b).multiply(a.add(b)).divide(t.multiply(FOUR), scale, ROUND_HALF_UP);
        number(pi);

        // print the output of pi result
        if (mOnPiChangedInterface != null) {
            mOnPiChangedInterface.onPiChanged(pi.toString());
        }
    }

    void setOnPiChangedInterface(OnPiChangedInterface onPiChangedInterface) {
        mOnPiChangedInterface = onPiChangedInterface;
    }

    private void number(BigDecimal a) {
        mNumber = a.toString();
    }

    String returnNum() {
        return mNumber;
    }

    private static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, ROUND_HALF_UP);
        }

        return x1;
    }
}