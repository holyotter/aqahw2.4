package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.VerificationPage;

public class MoneyTransferTest {
    private LoginPage loginPage = new LoginPage();
    private DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    private VerificationPage verificationPage = loginPage.validLogin(authInfo);
    private DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    private DashboardPage dashboardPage = verificationPage.validVerify(authInfo, verificationCode);
    private long totalAmount = dashboardPage.getFirstCardSum() + dashboardPage.getSecondCardSum();

    @Test
    void transferMoneyToFirstFromSecond() {
        val moneyTransferPage = dashboardPage.refillOneFromTwo();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getSecondCardNumber(),
                dashboardPage.getSecondCardSum() / 10);
        moneyTransferPage.refillAction(moneyTransferInfo);
        dashboardPage.updateAmounts();
        Assertions.assertEquals(moneyTransferPage.getRecipientAmount(), dashboardPage.getFirstCardSum());
        Assertions.assertEquals(moneyTransferPage.getSenderAmount(), dashboardPage.getSecondCardSum());
        Assertions.assertEquals(totalAmount, dashboardPage.getFirstCardSum() + dashboardPage.getSecondCardSum());
    }

    @Test
    void transferMoneyToSecondFromFirst() {
        val moneyTransferPage = dashboardPage.refillTwoFromOne();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getFirstCardNumber(),
                dashboardPage.getFirstCardSum() / 10);
        moneyTransferPage.refillAction(moneyTransferInfo);
        dashboardPage.updateAmounts();
        Assertions.assertEquals(moneyTransferPage.getRecipientAmount(), dashboardPage.getSecondCardSum());
        Assertions.assertEquals(moneyTransferPage.getSenderAmount(), dashboardPage.getFirstCardSum());
        Assertions.assertEquals(totalAmount, dashboardPage.getFirstCardSum() + dashboardPage.getSecondCardSum());
    }

    @Test
    void zeroTransferMoneySecondFromFirst() {
        val moneyTransferPage = dashboardPage.refillTwoFromOne();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getFirstCardNumber(),
                dashboardPage.getFirstCardSum());
        moneyTransferPage.refillAction(moneyTransferInfo);
        dashboardPage.updateAmounts();
        Assertions.assertEquals(totalAmount, dashboardPage.getSecondCardSum());
        Assertions.assertEquals(0, dashboardPage.getFirstCardSum());
    }

    @Test
    void cancelTransferMoney() {
        val moneyTransferPage = dashboardPage.refillOneFromTwo();
        val moneyTransferInfo = DataHelper.getMoneyTransferInfo(dashboardPage.getSecondCardNumber(),
                dashboardPage.getSecondCardSum() / 10);
        moneyTransferPage.cancelAction(moneyTransferInfo);
        dashboardPage.updateAmounts();
        Assertions.assertEquals(moneyTransferPage.getStartRecipientAmount(), dashboardPage.getFirstCardSum());
        Assertions.assertEquals(moneyTransferPage.getStartSenderAmount(), dashboardPage.getSecondCardSum());
        Assertions.assertEquals(totalAmount, dashboardPage.getFirstCardSum() + dashboardPage.getSecondCardSum());
    }
}
