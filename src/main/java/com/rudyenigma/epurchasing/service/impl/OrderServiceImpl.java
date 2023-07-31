package com.rudyenigma.epurchasing.service.impl;

import com.rudyenigma.epurchasing.entity.Order;
import com.rudyenigma.epurchasing.entity.OrderDetail;
import com.rudyenigma.epurchasing.entity.ProductPrice;
import com.rudyenigma.epurchasing.entity.Vendor;
import com.rudyenigma.epurchasing.model.request.OrderRequest;
import com.rudyenigma.epurchasing.model.response.*;
import com.rudyenigma.epurchasing.repository.OrderRepository;
import com.rudyenigma.epurchasing.service.OrderService;
import com.rudyenigma.epurchasing.service.ProductPriceService;
import com.rudyenigma.epurchasing.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final VendorService vendorService;
    private final ProductPriceService productPriceService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse createNewTransaction(OrderRequest request) {
        // TODO 1: Validate vendor
        Vendor vendor = vendorService.getById(request.getVendorId());

        // TODO 2: Convert orderDetailRequest to orderDetail
        List<OrderDetail> orderDetails = request.getOrderDetails().stream().map(orderDetailRequest -> {
            // TODO 3: Validate Product Price
            ProductPrice productPrice = productPriceService.getById(orderDetailRequest.getProductPriceId());

            return OrderDetail.builder()
                    .productPrice(productPrice)
                    .quantity(orderDetailRequest.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        // TODO 4: Create new order
        Order order = Order.builder()
                .vendor(vendor)
                .transDate(LocalDateTime.now())
                .orderDetails(orderDetails)
                .build();
        orderRepository.saveAndFlush(order);

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
            // TODO 5: Set order from orderDetail after creating new order
            orderDetail.setOrder(order);

            // TODO 6: Change the stock from the purchased quantity
            ProductPrice currentProductPrice = orderDetail.getProductPrice();
            currentProductPrice.setStock(currentProductPrice.getStock() + orderDetail.getQuantity());

            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    // TODO 7: Convert product to productResponse (from productPrice)
                    .product(ProductResponse.builder()
                            .productId(currentProductPrice.getProduct().getId())
                            .productName(currentProductPrice.getProduct().getName())
                            .price(currentProductPrice.getPrice())
                            .stock(currentProductPrice.getStock())
                            .category(currentProductPrice.getProduct().getCategory())
                            // TODO 8: Convert Store to storeResponse (from productPrice)
                            .vendor(VendorResponse.builder()
                                    .vendorId(currentProductPrice.getVendor().getId())
                                    .vendorName(currentProductPrice.getVendor().getName())
                                    .vendorPhoneNumber(currentProductPrice.getVendor().getPhoneNumber())
                                    .build())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        // TODO 9: Convert vendor to vendorResponse
        VendorResponse vendorResponse = VendorResponse.builder()
                .vendorId(vendor.getId())
                .vendorName(vendor.getName())
                .vendorPhoneNumber(vendor.getPhoneNumber())
                .build();

        // TODO 10: Convert orderDetail to orderDetailResponse
        return OrderResponse.builder()
                .orderId(order.getId())
                .vendor(vendorResponse)
                .transDate(order.getTransDate())
                .orderDetails(orderDetailResponses)
                .build();
    }

    @Override
    public List<OrderResponse> getAllByDate() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> {
            List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
                orderDetail.setOrder(order);
                ProductPrice currentProductPrice = orderDetail.getProductPrice();

                return OrderDetailResponse.builder()
                        .orderDetailId(orderDetail.getId())
                        .quantity(orderDetail.getQuantity())
                        .product(ProductResponse.builder()
                                .productId(currentProductPrice.getProduct().getId())
                                .productName(currentProductPrice.getProduct().getName())
                                .price(currentProductPrice.getPrice())
                                .stock(currentProductPrice.getStock())
                                .category(currentProductPrice.getProduct().getCategory())
                                .vendor(VendorResponse.builder()
                                        .vendorId(currentProductPrice.getVendor().getId())
                                        .vendorName(currentProductPrice.getVendor().getName())
                                        .vendorPhoneNumber(currentProductPrice.getVendor().getPhoneNumber())
                                        .build())
                                .build())
                        .build();
            }).collect(Collectors.toList());

            Vendor vendor = order.getVendor();
            VendorResponse vendorResponse = VendorResponse.builder()
                    .vendorId(vendor.getId())
                    .vendorName(vendor.getName())
                    .vendorPhoneNumber(vendor.getPhoneNumber())
                    .build();

            return OrderResponse.builder()
                    .orderId(order.getId())
                    .vendor(vendorResponse)
                    .transDate(order.getTransDate())
                    .orderDetails(orderDetailResponses)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getReports() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .flatMap(order -> order.getOrderDetails().stream()
                        .map(orderDetail -> {
                            orderDetail.setOrder(order);
                            ProductPrice currentProductPrice = orderDetail.getProductPrice();

                            Long productPrice = currentProductPrice.getPrice();
                            Integer quantity = orderDetail.getQuantity();

                            return ReportResponse.builder()
                                    .productCode(currentProductPrice.getProduct().getId())
                                    .date(orderDetail.getOrder().getTransDate())
                                    .vendorName(currentProductPrice.getVendor().getName())
                                    .productName(currentProductPrice.getProduct().getName())
                                    .productCategory(currentProductPrice.getProduct().getCategory())
                                    .productPrice(productPrice)
                                    .quantity(quantity)
                                    .total((int) (productPrice * quantity))
                                    .build();
                        }))
                .collect(Collectors.toList());
    }

    public void getCsvFile(Writer writer) throws IOException {
        String csvFilePath = "report-rudy-eprocurement.csv";

        List<ReportResponse> products = getReports();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Kode Barang", "Tanggal", "Nama Vendor", "Nama Barang", "Kategori", "Harga Barang", "Qty", "Total"))) {
            for (ReportResponse product : products) {
                csvPrinter.printRecord(
                        product.getProductCode(),
                        product.getDate(),
                        product.getVendorName(),
                        product.getProductName(),
                        product.getProductCategory(),
                        product.getProductPrice(),
                        product.getQuantity(),
                        product.getTotal()
                );
            }
        }
    }

}
