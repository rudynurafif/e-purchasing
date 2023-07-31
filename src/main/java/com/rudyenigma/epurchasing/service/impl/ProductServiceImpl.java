package com.rudyenigma.epurchasing.service.impl;

import com.rudyenigma.epurchasing.entity.Product;
import com.rudyenigma.epurchasing.entity.ProductPrice;
import com.rudyenigma.epurchasing.entity.Vendor;
import com.rudyenigma.epurchasing.model.request.ProductRequest;
import com.rudyenigma.epurchasing.model.response.ProductResponse;
import com.rudyenigma.epurchasing.model.response.VendorResponse;
import com.rudyenigma.epurchasing.repository.ProductRepository;
import com.rudyenigma.epurchasing.service.ProductPriceService;
import com.rudyenigma.epurchasing.service.ProductService;
import com.rudyenigma.epurchasing.service.VendorService;
import com.rudyenigma.epurchasing.specification.ProductSpecification;
import com.rudyenigma.epurchasing.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VendorService vendorService;
    private final ProductPriceService productPriceService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse create(ProductRequest request) {
        validationUtil.validate(request);
        Vendor vendor = vendorService.getById(request.getVendorId());

        Product product = Product.builder()
                .name(request.getProductName())
                .category(request.getCategory())
                .build();
        productRepository.saveAndFlush(product);

        ProductPrice productPrice = ProductPrice.builder()
                .price(request.getPrice())
                .stock(request.getStock())
                .vendor(vendor)
                .product(product)
                .isActive(true)
                .build();
        productPriceService.create(productPrice);
        return toProductResponse(product, productPrice, vendor);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<ProductResponse> createBulk(List<ProductRequest> requests) {
        return requests.stream().map(this::create).collect(Collectors.toList());
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        Optional<ProductPrice> productPrice = product.getProductPrices().stream().filter(ProductPrice::getIsActive).findFirst();

        if (productPrice.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        Vendor vendor = productPrice.get().getVendor();

        return toProductResponse(product, productPrice.get(), vendor);
    }

    @Override
    public Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size) {
        Specification<Product> specification = ProductSpecification.getSpecification(name, maxPrice);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products.getContent()) {
            Optional<ProductPrice> productPrice = product.getProductPrices()
                    .stream()
                    .filter(ProductPrice::getIsActive).findFirst();

            if (productPrice.isEmpty()) continue;
            Vendor vendor = productPrice.get().getVendor();

            productResponses.add(toProductResponse(product, productPrice.get(), vendor));
        }

        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse update(ProductRequest request) {
        Product currentProduct = findByIdOrThrowNotFound(request.getProductId());
        currentProduct.setName(request.getProductName());

        ProductPrice productPriceActive = productPriceService.findProductPriceActive(request.getProductId(), true);

        if (!productPriceActive.getVendor().getId().equals(request.getVendorId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "data tidak boleh diubah");

        // TODO: If price different create new product price
        if (!request.getPrice().equals(productPriceActive.getPrice())) {
            productPriceActive.setIsActive(false);
            ProductPrice productPrice = productPriceService.create(ProductPrice.builder()
                    .price(request.getPrice())
                    .stock(request.getStock())
                    .product(currentProduct)
                    .vendor(productPriceActive.getVendor())
                    .isActive(true)
                    .build());
            currentProduct.addProductPrice(productPrice);
            return toProductResponse(currentProduct, productPrice, productPrice.getVendor());
        }

        productPriceActive.setStock(request.getStock());

        return toProductResponse(currentProduct, productPriceActive, productPriceActive.getVendor());
    }

    @Override
    public void deleteById(String id) {
        Product product = findByIdOrThrowNotFound(id);
        productRepository.delete(product);
    }

    private Product findByIdOrThrowNotFound(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    private static ProductResponse toProductResponse(Product product, ProductPrice productPrice, Vendor vendor) {
        return ProductResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .category(product.getCategory())
                .vendor(VendorResponse.builder()
                        .vendorId(vendor.getId())
                        .vendorName(vendor.getName())
                        .vendorPhoneNumber(vendor.getPhoneNumber())
                        .build())
                .build();
    }
}
