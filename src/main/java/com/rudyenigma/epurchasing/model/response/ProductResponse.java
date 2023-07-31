package com.rudyenigma.epurchasing.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {

    private String productId;
    private String productName;
    private Long price;
    private Integer stock;
    private String category;
    private VendorResponse vendor;

}
