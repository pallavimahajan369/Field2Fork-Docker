package com.field2fork.dtos;


import java.util.List;

import com.field2fork.pojos.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class CartResponseDTO {
	private Long id;
	private Long userId;
	private List<CartItemResponseDTO> cartItems;
	private UserDTO user; 
	public CartResponseDTO(Long id, Long userId, List<CartItemResponseDTO> cartItemDTOs) {
		super();
		this.id = id;
		this.userId = userId;
		this.cartItems = cartItemDTOs;
	}
	public CartResponseDTO(Long id, Long userId, List<CartItemResponseDTO> cartItemDTOs, UserDTO user) {
		this.id = id;
		this.userId = userId;
		this.cartItems = cartItemDTOs;
		this.user = user;
	}
}

