package fontys.messaging.broker.gui;


import fontys.messaging.core.model.bank.BankInterestReply;
import fontys.messaging.core.model.bank.BankInterestRequest;
import fontys.messaging.core.model.loan.LoanRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class represents one line in the JList in Loan Broker.
 * This class stores all objects that belong to one LoanRequest:
 * - LoanRequest,
 * - BankInterestRequest, and
 * - BankInterestReply.
 * Use objects of this class to add them to the JList.
 *
 * @author 884294
 */
class JListLine {

    private LoanRequest loanRequest;
    private List<BankInterestReply> bankReplies;
    private int requestsSent;

    public JListLine(LoanRequest loanRequest, int requestsSent) {
        this.bankReplies = new ArrayList<>();
        this.loanRequest = loanRequest;
        this.requestsSent = requestsSent;
    }

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    public int getRequestsSent() {
        return requestsSent;
    }

    public List<BankInterestReply> getBankReplies() {
        return bankReplies;
    }

    public void addBankReply(BankInterestReply bankReply) {
        this.bankReplies.add(bankReply);
    }

    @Override
    public String toString() {
        return String.format("%s || %s/%s requests received", loanRequest, bankReplies.size(), requestsSent);
    }

}
