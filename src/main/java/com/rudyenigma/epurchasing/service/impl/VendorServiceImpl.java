package com.rudyenigma.epurchasing.service.impl;

import com.rudyenigma.epurchasing.entity.Vendor;
import com.rudyenigma.epurchasing.model.request.VendorRequest;
import com.rudyenigma.epurchasing.repository.VendorRepository;
import com.rudyenigma.epurchasing.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public Vendor create(VendorRequest vendor) {
        Vendor vendorToCreate = Vendor.builder()
                .name(vendor.getVendorName())
                .phoneNumber(vendor.getPhoneNumber())
                .build();
        return vendorRepository.saveAndFlush(vendorToCreate);
    }

    @Override
    public Vendor getById(String id) {
        return vendorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
    }

    @Override
    public List<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor update(VendorRequest vendor) {
        getById(vendor.getVendorId());

        Vendor vendorToUpdate = Vendor.builder()
                .id(vendor.getVendorId())
                .name(vendor.getVendorName())
                .phoneNumber(vendor.getPhoneNumber())
                .build();
        return vendorRepository.save(vendorToUpdate);
    }
}
