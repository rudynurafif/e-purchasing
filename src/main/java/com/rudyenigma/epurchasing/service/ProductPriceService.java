package com.rudyenigma.epurchasing.service;

import com.rudyenigma.epurchasing.entity.ProductPrice;

public interface ProductPriceService {

    ProductPrice create(ProductPrice productPrice);
    ProductPrice getById(String id);
    ProductPrice findProductPriceActive(String productId, Boolean active);

}
