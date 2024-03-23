package com.example.demo.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaleResponse {
    private int todaySales;
    private int weekSales;
    private int monthSales;
}