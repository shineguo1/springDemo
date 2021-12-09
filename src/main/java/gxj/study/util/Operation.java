package gxj.study.util;

import java.util.function.DoubleBinaryOperator;

/**
 * Created by xinjie_guo on 2020/1/17.
 */
public enum Operation {
    PLUS  ("+", (double x, double y) -> x + y),
    MINUS ("-", (x, y) -> x - y),
    TIMES ("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);

    private final String symbol;
    private final DoubleBinaryOperator op;

    Operation(String symbol, DoubleBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    @Override
    public String toString() { return symbol; }

    public double apply(double x, double y) {
        return op.applyAsDouble(x, y);
    }

    public static void main(String[] args) {
        System.out.println(PLUS.apply(2.0,2.0));
    }
}