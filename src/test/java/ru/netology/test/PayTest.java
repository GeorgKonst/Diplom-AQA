package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.netology.data.SQLHelper.*;

public class PayTest {
    DataHelper.CardNumber approvedCard = DataHelper.approvedCardInfo();
    DataHelper.CardNumber declinedCard = DataHelper.declinedCardInfo();
    DataHelper.CardInfo cardInfo = DataHelper.getCardInfo();

    @AfterEach
    public void cleanTables() throws SQLException {
        SQLHelper.cleanData();
    }

    @Nested
    public class ValidTest {

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithStatusApproved() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getSuccessNotification().shouldBe(Condition.visible, ofSeconds(20));
            assertEquals(approvedCard.getStatus(), payData().getStatus());
            assertEquals(payData().getTransaction_id(), orderData().getPayment_id());
        }

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithStatusDeclined() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails(declinedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getErrorMassage().shouldBe(Condition.visible, ofSeconds(20));
            assertEquals(declinedCard.getStatus(), payData().getStatus());
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithStatusApproved() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getSuccessNotification().shouldBe(Condition.visible, ofSeconds(20));
            assertEquals(approvedCard.getStatus(), creditData().getStatus());
            assertEquals(creditData().getBank_id(), orderData().getPayment_id());
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithStatusDeclined() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails(declinedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getErrorMassage().shouldBe(Condition.visible, ofSeconds(20));
            assertEquals(declinedCard.getStatus(), creditData().getStatus());
            checkEmptyOrderEntity();
        }
    }

    @Nested
    public class NotValidTest {

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithStatusUnknown() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails("4444444444444444", cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getErrorMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithStatusUnknown() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails("4444444444444444", cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getErrorMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithStatusNotValid() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails("444444444444444", cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getWrongFormatMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithStatusNotValid() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails("444444444444444", cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getWrongFormatMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithStatusExpiredTodayYear() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getPastMonth(), cardInfo.getTodayYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getInvalidCardMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithStatusExpiredTodayYear() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getPastMonth(), cardInfo.getTodayYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getInvalidCardMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithStatusExpiredLastYear() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getPastYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getInvalidCardMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithStatusExpiredLastYear() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getPastYear(),
                    cardInfo.getOwner(), cardInfo.getCvc());
            cashPage.getInvalidCardMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithNameInCyrillic() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getRusOwner(), cardInfo.getCvc());
            cashPage.getWrongFormatMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithNameInCyrillic() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getRusOwner(), cardInfo.getCvc());
            cashPage.getWrongFormatMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithEmptyFields() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.clickNextButton();
            cashPage.getSuccessNotification().shouldNot(Condition.visible, ofSeconds(20));
            cashPage.getErrorMassage().shouldNot(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithEmptyFields() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.clickNextButton();
            cashPage.getSuccessNotification().shouldNot(Condition.visible, ofSeconds(20));
            cashPage.getErrorMassage().shouldNot(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithFullLetters() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails(cardInfo.getLetters(), cardInfo.getLetters(), cardInfo.getLetters(),
                    cardInfo.getOwner(), cardInfo.getLetters());
            cashPage.getCardNumberEl().shouldNotHave(exactValue(cardInfo.getLetters()));
            cashPage.getMonthEl().shouldNotHave(exactValue(cardInfo.getLetters()));
            cashPage.getYearEl().shouldNotHave(exactValue(cardInfo.getLetters()));
            cashPage.getCvcEl().shouldNotHave(exactValue(cardInfo.getLetters()));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithFullLetters() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails(cardInfo.getLetters(), cardInfo.getLetters(), cardInfo.getLetters(),
                    cardInfo.getOwner(), cardInfo.getLetters());
            cashPage.getCardNumberEl().shouldNotHave(exactValue(cardInfo.getLetters()));
            cashPage.getMonthEl().shouldNotHave(exactValue(cardInfo.getLetters()));
            cashPage.getYearEl().shouldNotHave(exactValue(cardInfo.getLetters()));
            cashPage.getCvcEl().shouldNotHave(exactValue(cardInfo.getLetters()));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }
        @SneakyThrows
        @Test
        void shouldDebitCardPurchaseWithFieldOwnerInNumbers() {
            val cashPage = new StartPage().openCashPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getNumbers(), cardInfo.getCvc());
            cashPage.getOwnerEl().shouldNotHave(exactValue(cardInfo.getNumbers()));
            cashPage.getWrongFormatMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

        @SneakyThrows
        @Test
        void shouldCreditCardPurchaseWithFieldOwnerInNumbers() {
            val cashPage = new StartPage().openCreditPayPage();
            cashPage.enterCardDetails(approvedCard.getCardNumber(), cardInfo.getMonth(), cardInfo.getYear(),
                    cardInfo.getNumbers(), cardInfo.getCvc());
            cashPage.getOwnerEl().shouldNotHave(exactValue(cardInfo.getNumbers()));
            cashPage.getWrongFormatMassage().shouldBe(Condition.visible, ofSeconds(20));
            checkEmptyPaymentEntity();
            checkEmptyOrderEntity();
        }

    }
}
