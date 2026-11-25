
package com.crio.order_management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "grocery_items")
@Data
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private BigDecimal price;

    private Integer quantity;
}
