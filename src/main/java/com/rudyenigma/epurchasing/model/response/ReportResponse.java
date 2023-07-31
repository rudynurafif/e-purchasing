package com.rudyenigma.epurchasing.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ReportResponse {

    private String productCode;
    private LocalDateTime date;
    private String vendorName;
    private String productName;
    private String productCategory;
    private Long productPrice;
    private Integer quantity;
    private Integer total;

}
