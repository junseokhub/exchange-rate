package com.dev.forex.domain.order.controller;

import com.dev.forex.common.response.ApiResponse;
import com.dev.forex.domain.order.dto.OrderListResponse;
import com.dev.forex.domain.order.dto.OrderRequest;
import com.dev.forex.domain.order.dto.OrderResponse;
import com.dev.forex.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(OrderResponse.from(
                        orderService.createOrder(request.forexAmount(), request.fromCurrency(), request.toCurrency()))));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrderList() {
        return ResponseEntity.ok(
                ApiResponse.success(OrderListResponse.from(orderService.getOrderList())));
    }
}