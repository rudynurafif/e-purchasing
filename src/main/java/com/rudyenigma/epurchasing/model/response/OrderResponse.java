package com.rudyenigma.epurchasing.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderResponse {

    private String orderId;
    private LocalDateTime transDate;
    private VendorResponse vendor;
    private List<OrderDetailResponse> orderDetails;


}
