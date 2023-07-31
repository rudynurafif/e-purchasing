package com.rudyenigma.epurchasing.repository;

import com.rudyenigma.epurchasing.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, String>, JpaSpecificationExecutor<ProductPrice> {
    Optional<ProductPrice> findByProduct_IdAndIsActive(String productId, Boolean active);
}
