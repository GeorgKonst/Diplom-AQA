package ru.netology.data.entiny;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderEntity {
    private String id;
    private String created;
    private String credit_id;
    private String payment_id;
}