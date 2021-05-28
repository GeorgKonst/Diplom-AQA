package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Value;


import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Value
public class CashPayPage {
    private SelenideElement headEl = $(byText("Оплата по карте"));
    private SelenideElement cardNumberEl = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthEl = $("[placeholder='08']");
    private SelenideElement yearEl = $("[placeholder='22']");
    private SelenideElement ownerEl = $$(".input__control").get(3);
    private SelenideElement cvcEl = $("[placeholder='999']");
    private SelenideElement nextButton = $(byText("Продолжить"));

    private SelenideElement successNotification = $(byText("Операция одобрена Банком."));
    private SelenideElement errorMassage = $(byText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement wrongFormatMassage = $(byText("Неверный формат"));
    private SelenideElement wrongFormatForMonthMassage = $(byText("Неверно указан срок действия карты"));
    private SelenideElement invalidCardMassage = $(byText("Истёк срок действия карты"));
    private SelenideElement cardholderNameMassage = $(byText("Поле обязательно для заполнения"));

    public CashPayPage() {
        headEl.shouldBe(Condition.visible);
    }


    public void enterCardDetails(String cardNumber, String month, String year, String owner, String cvc){
        cardNumberEl.setValue(cardNumber);
        monthEl.setValue(month);
        yearEl.setValue(year);
        ownerEl.setValue(owner);
        cvcEl.setValue(cvc);
        nextButton.click();
    }

    public void clickNextButton(){
        nextButton.click();
    }
}
