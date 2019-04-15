package com.gongzelong.iflixtask

import java.math.BigDecimal

import java.math.BigDecimal.ONE
import java.math.BigDecimal.ROUND_HALF_UP

internal class Pi {
    private var mNumber: String? = null
    fun pi(scale: Int) {
        var a = ONE
        var b = ONE.divide(sqrt(TWO, scale), scale, ROUND_HALF_UP)
        var t = BigDecimal(0.25)
        var x = ONE
        var y: BigDecimal

        while (a != b) {
            y = a
            a = a.add(b).divide(TWO, scale, ROUND_HALF_UP)
            b = sqrt(b.multiply(y), scale)
            t = t.subtract(x.multiply(y.subtract(a).multiply(y.subtract(a))))
            x = x.multiply(TWO)
        }

        val pi = a.add(b).multiply(a.add(b)).divide(t.multiply(FOUR), scale, ROUND_HALF_UP)
        number(pi)
    }

    private fun number(a: BigDecimal) {
        mNumber = a.toString()
    }

    fun returnNum(): String? {
        return mNumber
    }

    companion object {

        private val TWO = BigDecimal(2)
        private val FOUR = BigDecimal(4)

        private fun sqrt(A: BigDecimal, SCALE: Int): BigDecimal {
            var x0 = BigDecimal("0")
            var x1 = BigDecimal(Math.sqrt(A.toDouble()))

            while (x0 != x1) {
                x0 = x1
                x1 = A.divide(x0, SCALE, ROUND_HALF_UP)
                x1 = x1.add(x0)
                x1 = x1.divide(TWO, SCALE, ROUND_HALF_UP)
            }

            return x1
        }
    }
}