package fontys.messaging.core.model.bank;

import fontys.messaging.core.model.loan.LoanReply;
import fontys.messaging.core.model.loan.LoanRequest;

import java.io.Serializable;

/**
 *
 * This class stores all information about an request from a bank to offer
 * a loan to a specific client.
 */
public class BankInterestRequest implements Serializable {

    private LoanRequest baseRequest;

    public BankInterestRequest() {
        super();
    }

    public BankInterestRequest(LoanRequest baseRequest) {
        super();
        this.baseRequest = baseRequest;
    }

    public LoanRequest getBaseRequest() {
        return baseRequest;
    }

    public int getAmount() {
        return baseRequest.getAmount();
    }

    public int getTime() {
        return baseRequest.getTime();
    }

    @Override
    public String toString() {
        return " amount=" + baseRequest.getAmount() + " time=" + baseRequest.getTime();
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getClass() != obj.getClass()){
            return false;
        }
        var object = (BankInterestRequest)obj;
        if(this.baseRequest == object.baseRequest){
            return true;
        }
        return super.equals(obj);
    }
}
