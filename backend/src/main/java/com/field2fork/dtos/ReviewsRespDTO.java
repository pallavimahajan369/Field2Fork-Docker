package com.field2fork.dtos;

import java.sql.Timestamp;

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
public class ReviewsRespDTO {
	private Long review_id;
    private Timestamp review_date;
    private Integer rating;
    private String reviewText;
    private Long productId;
    private String productName;
    private Long userId;
    private String userName;

    public ReviewsRespDTO(Long review_id, Timestamp review_date, Integer rating, String reviewText, 
                          Long productId, String productName, Long userId, String userName) {
    	this.review_id = review_id;
        this.review_date = review_date;
        this.rating = rating;
        this.reviewText = reviewText;
        this.productId = productId;
        this.productName = productName;
        this.userId = userId;
        this.userName = userName;
    }
}