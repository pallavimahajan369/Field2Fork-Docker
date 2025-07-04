package com.field2fork.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageRequestDTO {
    private Long productId;
    private byte[] imageData;
}