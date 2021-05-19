package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class StartPage {
    private String url = "http://localhost:8080/";

    private SelenideElement cashButton = $(byText("Купить"));
    private SelenideElement creditButton = $(byText("Купить в кредит"));

    public void openStartPage() {
        open(url);
    }

    public CashPayPage openCashPayPage() {
        openStartPage();
        cashButton.click();
        return new CashPayPage();
    }

    public void openCreditPayPage() {
        openStartPage();
        creditButton.click();
    }
}
