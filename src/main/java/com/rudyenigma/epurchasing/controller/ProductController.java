package com.rudyenigma.epurchasing.controller;

import com.rudyenigma.epurchasing.model.request.ProductRequest;
import com.rudyenigma.epurchasing.model.response.CommonResponse;
import com.rudyenigma.epurchasing.model.response.PagingResponse;
import com.rudyenigma.epurchasing.model.response.ProductResponse;
import com.rudyenigma.epurchasing.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api-epurchasing/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> registerNewProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new product")
                        .data(productResponse)
                        .build());
    }

    @PostMapping(path = "/bulk")
    public ResponseEntity<?> registerNewBulkProduct(@RequestBody List<ProductRequest> products) {
        List<ProductResponse> productResponses = productService.createBulk(products);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<List<ProductResponse>>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create bulk customer")
                        .data(productResponses)
                        .build());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductById(Authentication authentication, @PathVariable String id) {
        ProductResponse productResponse = productService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get customer")
                        .data(productResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllProductOnWareHouse(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        Page<ProductResponse> productResponses = productService.getAllByNameOrPrice(name, maxPrice, page - 1, size);
        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(productResponses.getTotalPages())
                .size(size)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all products")
                        .data(productResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.update(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update customer")
                        .data(productResponse)
                        .build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") String id) {
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete customer")
                        .build());
    }

}

