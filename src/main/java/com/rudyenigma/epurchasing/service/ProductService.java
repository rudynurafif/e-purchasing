package com.rudyenigma.epurchasing.service;

import com.rudyenigma.epurchasing.model.request.ProductRequest;
import com.rudyenigma.epurchasing.model.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    List<ProductResponse> createBulk(List<ProductRequest> requests);

    ProductResponse getById(String id);

    Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size);

    ProductResponse update(ProductRequest request);

    void deleteById(String id);

}
