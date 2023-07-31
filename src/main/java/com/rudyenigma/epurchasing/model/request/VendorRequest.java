package com.rudyenigma.epurchasing.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VendorRequest {

    private String vendorId;

    @NotBlank(message = "vendor name is required")
    private String vendorName;

    @NotBlank(message = "vendor phone number is required")
    private String phoneNumber;

}