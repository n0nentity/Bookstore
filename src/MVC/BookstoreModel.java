package MVC;

import java.math.BigInteger;

public class BookstoreModel {
    private static final String INITIAL_VALUE = "0";

    private BigInteger total;

    protected BookstoreModel() {
        reset();
    }

    public void reset() {
        total = new BigInteger(INITIAL_VALUE);
    }

    public void multiplyBy(String operand) {
        total = total.multiply(new BigInteger(operand));
    }

    public void setTotal(String value) {
        total = new BigInteger(value);
    }

    public String getTotal() {
        return total.toString();
    }
}