package MVC;

import java.math.BigInteger;

public class BookstoreModel {
    private BigInteger requestResult;

    protected BookstoreModel() {
        //
    }

    public void request(String operand) {
        //requestResult = ;
    }

    public void setRequestResult(String value) {
        requestResult = new BigInteger(value);
    }

    public String getRequestResult() {
        return requestResult.toString();
    }
}