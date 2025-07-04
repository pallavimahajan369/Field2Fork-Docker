package com.field2fork.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartRequestDTO {
    private Long userId;
    private List<CartItemRequestDTO> cartItems; 

    public CartRequestDTO(Long userId, List<CartItemRequestDTO> cartItems) {  
        this.userId = userId;
        this.cartItems = cartItems;
    }

}
