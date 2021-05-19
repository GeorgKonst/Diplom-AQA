package ru.netology.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static String url = "jdbc:mysql://localhost:3306/app";
    private static String user = "app";
    private static String password = "pass";

    private SQLHelper() {}

    public static void cleanData() throws SQLException {
        QueryRunner runner = new QueryRunner();
        val cleanCreditRequest = "DELETE FROM credit_request_entity;";
        val cleanPayment = "DELETE FROM payment_entity;";
        val cleanOrder = "DELETE FROM order_entity;";

        try (
                val conn = DriverManager.getConnection(url, user, password);
        ) {
            runner.update(conn, cleanCreditRequest);
            runner.update(conn, cleanPayment);
            runner.update(conn, cleanOrder);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getDebitCardStatus() throws SQLException {
        QueryRunner runner = new QueryRunner();
        val reqStatus = "SELECT * FROM payment_entity";
        try (
                val conn = DriverManager.getConnection(url, user, password);
        ) {
            val debitCardStatus = runner.query(conn, reqStatus, new BeanHandler<>(PaymentEntity.class));
            return debitCardStatus.getStatus();
        }
    }


    public static String getTransactionId() throws SQLException {

        val selectStatus = "SELECT * FROM payment_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditCardStatus = runner.query(conn, selectStatus, new BeanHandler<>(PaymentEntity.class));
            return creditCardStatus.getTransaction_id();
        }
    }

    public static String getCreditCardStatus() throws SQLException {

        val selectStatus = "SELECT * FROM credit_request_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditCardStatus = runner.query(conn, selectStatus, new BeanHandler<>(CreditRequestEntity.class));
            return creditCardStatus.getStatus();
        }
    }

    public static String getPaymentId() throws SQLException {

        val selectStatus = "SELECT * FROM order_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditCardStatus = runner.query(conn, selectStatus, new BeanHandler<>(OrderEntity.class));
            return creditCardStatus.getPayment_id();
        }
    }




}
