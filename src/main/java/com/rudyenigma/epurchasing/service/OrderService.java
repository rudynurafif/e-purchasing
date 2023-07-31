package com.rudyenigma.epurchasing.service;

import com.rudyenigma.epurchasing.model.request.OrderRequest;
import com.rudyenigma.epurchasing.model.response.OrderResponse;
import com.rudyenigma.epurchasing.model.response.ReportResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface OrderService {

    OrderResponse createNewTransaction(OrderRequest request);

    List<OrderResponse> getAllByDate();

    List<ReportResponse> getReports();

    void getCsvFile(Writer writer) throws IOException;


}
