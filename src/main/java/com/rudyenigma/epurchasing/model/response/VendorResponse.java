package com.rudyenigma.epurchasing.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VendorResponse {

    private String vendorId;
    private String vendorName;
    private String vendorPhoneNumber;

}
