package ru.netology.data.entiny;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentEntity {
    private String id;
    private String amount;
    private String created;
    private String status;
    private String transaction_id;
}