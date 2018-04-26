package fontys.messaging.core.model.loan;

import java.io.Serializable;

/**
 *
 * This class stores all information about a
 * request that a client submits to get a loan.
 *
 */
public class LoanRequest implements Serializable {

    private int ssn;
    private int amount;
    private int time;

    public LoanRequest() {
        super();
        this.ssn = 0;
        this.amount = 0;
        this.time = 0;
    }

    public LoanRequest(int ssn, int amount, int time) {
        super();
        this.ssn = ssn;
        this.amount = amount;
        this.time = time;
    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ssn=" + String.valueOf(ssn) + " amount=" + String.valueOf(amount) + " time=" + String.valueOf(time);
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getClass() != obj.getClass()){
            return false;
        }
        var object = (LoanRequest)obj;
        if(this.ssn == object.ssn && this.amount == object.amount && this.time == object.time){
            return true;
        }
        return super.equals(obj);
    }
}
