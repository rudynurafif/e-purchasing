package com.rudyenigma.epurchasing.controller;

import com.rudyenigma.epurchasing.entity.Vendor;
import com.rudyenigma.epurchasing.model.request.VendorRequest;
import com.rudyenigma.epurchasing.model.response.CommonResponse;
import com.rudyenigma.epurchasing.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/vendors")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<?> registerNewVendor(@RequestBody VendorRequest request) {
        Vendor vendor = vendorService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<Vendor>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new vendor data")
                        .data(vendor)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllVendor() {
        List<Vendor> vendors = vendorService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all vendor data")
                        .data(vendors)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateVendor(@RequestBody VendorRequest vendor) {
        Vendor vendorUpdate = vendorService.update(vendor);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<Vendor>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully update vendor data")
                        .data(vendorUpdate)
                        .build());

    }

}
