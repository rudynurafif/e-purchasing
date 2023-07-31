package com.rudyenigma.epurchasing.service;

import com.rudyenigma.epurchasing.entity.Vendor;
import com.rudyenigma.epurchasing.model.request.VendorRequest;

import java.util.List;

public interface VendorService {

    Vendor create(VendorRequest vendor);
    Vendor getById(String id);
    List<Vendor> getAll();
    Vendor update(VendorRequest vendor);
}
