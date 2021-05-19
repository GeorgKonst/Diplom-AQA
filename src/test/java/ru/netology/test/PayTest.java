package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.sleep;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PayTest {
    DataHelper.CardNumber approvedCard = DataHelper.approvedCardInfo();
    DataHelper.CardNumber declinedCard = DataHelper.declinedCardInfo();
    DataHelper.CardInfo cardInfo = DataHelper.getCardInfo();

    @AfterAll
    public static void cleanTables() throws SQLException {
        SQLHelper.cleanData();
    }

    @Test
    void shouldDebitCardPurchaseWithStatusApproved() throws SQLException {
        val cashPage = new StartPage().openCashPayPage();
        cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                cardInfo.getOwner(), cardInfo.getCvc());
        cashPage.getSuccessNotification().shouldBe(Condition.visible, ofSeconds(20));
        assertEquals(approvedCard.getStatus(), SQLHelper.getDebitCardStatus());
        assertEquals(SQLHelper.getTransactionId(), SQLHelper.getPaymentId());
    }

    @Test
    void shouldDebitCardPurchaseWithStatusDeclined() throws SQLException {
        val cashPage = new StartPage().openCashPayPage();
        cashPage.enterCardDetails(declinedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                cardInfo.getOwner(), cardInfo.getCvc());
        cashPage.getSuccessNotification().shouldBe(Condition.visible, ofSeconds(20));
        assertEquals(declinedCard.getStatus(), SQLHelper.getDebitCardStatus());
        assertEquals(SQLHelper.getTransactionId(), SQLHelper.getPaymentId());
    }
}
