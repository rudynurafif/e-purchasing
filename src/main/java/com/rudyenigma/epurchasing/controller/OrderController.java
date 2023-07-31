package com.rudyenigma.epurchasing.controller;

import com.rudyenigma.epurchasing.model.request.OrderRequest;
import com.rudyenigma.epurchasing.model.response.CommonResponse;
import com.rudyenigma.epurchasing.model.response.OrderResponse;
import com.rudyenigma.epurchasing.model.response.ReportResponse;
import com.rudyenigma.epurchasing.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api-epurchasing/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createNewOrder(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createNewTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new transaction")
                        .data(orderResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getTransactions() {
        List<OrderResponse> orderResponses = orderService.getAllByDate();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all transaction")
                        .data(orderResponses)
                        .build());
    }

    @GetMapping(path = "/reports")
    public List<ReportResponse> getReports() {
        return orderService.getReports();
    }

    @GetMapping(path = "/download-reports")
    public ResponseEntity<?> getCvReport(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        orderService.getCsvFile(servletResponse.getWriter());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .build();
    }

}
