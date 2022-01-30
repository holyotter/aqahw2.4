package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.visible;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement firstBalanceButton = $$("[data-test-id='action-deposit']").first();
    private SelenideElement secondBalanceButton = $$("[data-test-id='action-deposit']").last();
    private SelenideElement reloadButton = $("[data-test-id=action-reload]");
    private long firstCardSum;
    private long secondCardSum;
    private DataHelper.AuthInfo authInfo;
    private DataHelper.CardInfo firstCardInfo;
    private DataHelper.CardInfo secondCardInfo;
    private final Pattern pattern = Pattern.compile(", баланс:\\s*(\\-?\\d+)\\s*");

    public DashboardPage(DataHelper.AuthInfo authInfo) {
        heading.shouldBe(visible);
        this.authInfo = authInfo;
        firstCardInfo = DataHelper.getFirstCardInfo(authInfo);
        secondCardInfo = DataHelper.getSecondCardInfo(authInfo);
        updateAmounts();
    }

    public void updateAmounts() {
        String firstCardDescription = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']").getText();
        Matcher m = pattern.matcher(firstCardDescription);
        m.find();
        String str = m.group(1);
        firstCardSum = Long.parseLong(str);
        String secondCardDescription = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']").getText();
        m = pattern.matcher(secondCardDescription);
        m.find();
        str = m.group(1);
        secondCardSum = Long.parseLong(str);
    }

    public long getFirstCardSum() {
        return firstCardSum;
    }

    public long getSecondCardSum() {
        return secondCardSum;
    }

    public String getFirstCardNumber() {
        return firstCardInfo.getCardNumber();
    }

    public String getSecondCardNumber() {
        return secondCardInfo.getCardNumber();
    }

    public void isVisible() {
        firstBalanceButton.shouldBe(visible);
        secondBalanceButton.shouldBe(visible);
    }

    public MoneyTransferPage refillOneFromTwo() {
        firstBalanceButton.click();
        return new MoneyTransferPage(firstCardInfo.getCardNumber(), firstCardSum, secondCardSum);
    }

    public MoneyTransferPage refillTwoFromOne() {
        secondBalanceButton.click();
        return new MoneyTransferPage(secondCardInfo.getCardNumber(), secondCardSum, firstCardSum);
    }
}
