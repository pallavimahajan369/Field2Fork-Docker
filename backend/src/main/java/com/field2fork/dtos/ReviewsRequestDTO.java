package com.field2fork.dtos;

import com.field2fork.pojos.Product;
import com.field2fork.pojos.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReviewsRequestDTO {
	
	private String reviewText;
	private Integer rating;
	private Long productId;
	private Long userId;
	 

}
