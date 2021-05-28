package ru.netology.data;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import ru.netology.data.entiny.CreditRequestEntity;
import ru.netology.data.entiny.OrderEntity;
import ru.netology.data.entiny.PaymentEntity;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;

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

    public static PaymentEntity payData() throws SQLException {
        QueryRunner runner = new QueryRunner();
        val reqStatus = "SELECT * FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (
                val conn = DriverManager.getConnection(url, user, password);
        ) {
            val payData = runner.query(conn, reqStatus, new BeanHandler<>(PaymentEntity.class));
            return payData;
        }
    }

    public static CreditRequestEntity creditData() throws SQLException {
        val selectStatus = "SELECT * FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditData = runner.query(conn, selectStatus, new BeanHandler<>(CreditRequestEntity.class));
            return creditData;
        }
    }

    public static OrderEntity orderData() throws SQLException {
        val selectStatus = "SELECT * FROM order_entity ORDER BY created DESC LIMIT 1";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val orderData = runner.query(conn, selectStatus, new BeanHandler<>(OrderEntity.class));
            return orderData;
        }
    }

    public static void checkEmptyOrderEntity() throws SQLException {
        val orderRequest = "SELECT * FROM order_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val orderBlock = runner.query(conn, orderRequest, new BeanHandler<>(OrderEntity.class));
            assertNull(orderBlock);
        }
    }
    public static void checkEmptyPaymentEntity() throws SQLException {
        val orderRequest = "SELECT * FROM payment_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val paymentBlock = runner.query(conn, orderRequest, new BeanHandler<>(OrderEntity.class));
            assertNull(paymentBlock);
        }
    }
    public static void checkEmptyCreditEntity() throws SQLException {
        val orderRequest = "SELECT * FROM credit_request_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditBlock = runner.query(conn, orderRequest, new BeanHandler<>(OrderEntity.class));
            assertNull(creditBlock);
        }
    }

}
