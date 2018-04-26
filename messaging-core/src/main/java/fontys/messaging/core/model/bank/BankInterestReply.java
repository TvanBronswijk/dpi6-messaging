package fontys.messaging.core.model.bank;

import java.io.Serializable;

/**
 * This class stores information about the bank reply
 *  to a loan request of the specific client
 * 
 */
public class BankInterestReply implements Serializable {

    private BankInterestRequest request;
    private double interest; // the loan interest
    private String bankId; // the nunique quote Id
    
    public BankInterestReply() {
        super();
        this.interest = 0;
        this.bankId = "";
    }
    
    public BankInterestReply(BankInterestRequest request, double interest, String quoteId) {
        super();
        this.request = request;
        this.interest = interest;
        this.bankId = quoteId;
    }

    public double getInterest() {
        return interest;
    }

    public String getQuoteId() {
        return bankId;
    }

    public BankInterestRequest getRequest() {
        return request;
    }

    public String toString() {
        return "quote=" + this.bankId + " interest=" + this.interest;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getClass() != obj.getClass()){
            return false;
        }
        var object = (BankInterestReply)obj;
        if(this.interest == object.interest && this.bankId == object.bankId){
            return true;
        }
        return super.equals(obj);
    }
}
