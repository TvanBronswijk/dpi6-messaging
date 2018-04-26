package fontys.messaging.core.model.loan;

import fontys.messaging.core.model.bank.BankInterestReply;

import java.io.Serializable;

/**
 * This class stores all information about a bank offer
 * as a response to a client loan request.
 */
public class LoanReply implements Serializable {

    private LoanRequest request;
    private BankInterestReply baseReply;

    public LoanReply() {
        super();
    }

    public LoanReply(BankInterestReply baseReply) {
        super();
        this.request = baseReply.getRequest().getBaseRequest();
        this.baseReply = baseReply;
    }

    public double getInterest() {
        return baseReply.getInterest();
    }

    public String getQuoteID() {
        return baseReply.getQuoteId();
    }

    public LoanRequest getRequest() {
        return request;
    }

    public BankInterestReply getBaseReply() {
        return baseReply;
    }

    @Override
    public String toString() {
        return " interest=" + String.valueOf(baseReply.getInterest()) + " quoteID=" + String.valueOf(baseReply.getQuoteId());
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getClass() != obj.getClass()){
            return false;
        }
        var object = (LoanReply)obj;
        if(this.baseReply == object.baseReply){
            return true;
        }
        return super.equals(obj);
    }
}
