
package com.crio.order_management.repository;

import com.crio.order_management.entity.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryItemRepository
        extends JpaRepository<GroceryItem, Long> {
}
