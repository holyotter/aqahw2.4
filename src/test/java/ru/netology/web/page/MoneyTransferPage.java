package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromCardField = $("[data-test-id=from] input");
    private SelenideElement refillButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private String recipientCardNumber;
    private String senderCardNumber;
    private long startRecipientAmount;
    private long startSenderAmount;
    private long recipientAmount;
    private long senderAmount;
    private long refillAmount;

    public MoneyTransferPage(String recipientCardNumber, long startRecipientAmount, long startSenderAmount) {
        amountField.shouldBe(visible);
        this.recipientCardNumber = recipientCardNumber;
        this.startRecipientAmount = startRecipientAmount;
        this.startSenderAmount = startSenderAmount;
    }

    public long getRecipientAmount() {
        return recipientAmount;
    }

    public long getSenderAmount() {
        return senderAmount;
    }

    public long getStartRecipientAmount() {
        return startRecipientAmount;
    }

    public long getStartSenderAmount() {
        return startSenderAmount;
    }

    public void refillAction(DataHelper.MoneyTransferInfo moneyTransferInfo) {
        amountField.setValue(moneyTransferInfo.getRefillAmount());
        fromCardField.setValue(moneyTransferInfo.getSenderCardNumber());
        refillAmount = Long.parseLong(moneyTransferInfo.getRefillAmount());
        recipientAmount = startRecipientAmount + refillAmount;
        senderAmount = startSenderAmount - refillAmount;
        refillButton.click();
    }

    public void refillActionError(DataHelper.MoneyTransferInfo moneyTransferInfo) {
        amountField.setValue(moneyTransferInfo.getRefillAmount());
        fromCardField.setValue(moneyTransferInfo.getSenderCardNumber());
        refillButton.click();
        $("[data-test-id='error-notification']").shouldBe(visible);
        cancelButton.click();
    }

    public void cancelAction(DataHelper.MoneyTransferInfo moneyTransferInfo) {
        amountField.setValue(moneyTransferInfo.getRefillAmount());
        fromCardField.setValue(moneyTransferInfo.getSenderCardNumber());
        cancelButton.click();
    }
}
