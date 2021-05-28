package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    private DataHelper(){}

    @Value
    public static class CardNumber {
        private String cardNumber;
        private String status;
    }

    public static CardNumber approvedCardInfo() {
        return new CardNumber("4444444444444441", "APPROVED");
    }

    public static CardNumber declinedCardInfo() {
        return new CardNumber("4444444444444442", "DECLINED");
    }

    @Value
    public static class CardInfo {
        private String month;
        private String year;
        private String owner;
        private String cvc;
        private String pastMonth;
        private String pastYear;
        private String todayYear;
        private String rusOwner;
        final String letters = "тест";
        final String numbers = "1234567";
    }

    public static CardInfo getCardInfo() {
        LocalDate today = LocalDate.now();
        String month = today.plusMonths(2).format(DateTimeFormatter.ofPattern("MM"));
        String year = today.plusYears(2).format(DateTimeFormatter.ofPattern("yy"));
        String owner = getEngName();
        String cvc = getRandomCVC();
        String pastMonth = today.minusMonths(2).format(DateTimeFormatter.ofPattern("MM"));
        String pastYear = today.minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        String todayYear = today.format(DateTimeFormatter.ofPattern("yy"));
        String rusOwner = getRuName();

        return new CardInfo(month, year, owner, cvc, pastMonth, pastYear, todayYear, rusOwner);
    }

    private static String getRandomCVC() {
        int a = 0;
        int b = 10;

        int num1 = a + (int) (Math.random() * b);
        int num2 = a + (int) (Math.random() * b);
        int num3 = a + (int) (Math.random() * b);
        String cvc = Integer.toString(num1) + num2 + num3;
        return cvc;
    }

    private static String getRuName() {
        Faker faker = new Faker(new Locale("ru"));
        String name = faker.name().firstName().toUpperCase() + " " + faker.name().lastName().toUpperCase();
        return name;
    }

    private static String getEngName() {
        Faker faker = new Faker(new Locale("en"));
        String name = faker.name().firstName().toUpperCase() + " " + faker.name().lastName().toUpperCase();
        return name;
    }
}
