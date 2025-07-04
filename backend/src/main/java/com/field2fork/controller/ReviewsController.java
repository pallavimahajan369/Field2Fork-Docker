package com.field2fork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.ReviewsRequestDTO;
import com.field2fork.dtos.ReviewsRespDTO;
import com.field2fork.service.ReviewsService;


@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewsController {
	
	@Autowired
	private ReviewsService reviewsService;
	
	public ReviewsController() {
		System.out.println("in Review Controller");
	}

	
	/*1.
	 * Desc - get all Reviewws
	 * URL - http://host:port/reviews
	 * Method - GET
	 */
	@GetMapping
	public ResponseEntity<?> getAllReviews(){
		System.out.println("Inside getAllReviews");
		List<ReviewsRespDTO> reviews = 
				reviewsService.getAllReviews();
		if(reviews.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	     } else {
		return ResponseEntity.ok(reviews);
	}
	}
	
	/*
	 * Desc - get reviews by Product id
	 * URL - http://host:port/reviews/{id}
	 * Method - GET
	 */
	@GetMapping("/{product_id}")
	public ResponseEntity<?> getReviewByProductId(@PathVariable Long product_id)
	{
		System.out.println("Inside Get Review by Id");
		List<ReviewsRespDTO> review = 
				reviewsService.getReviewByProductId(product_id);
		if(review.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		else
		{
			return ResponseEntity.ok(review);
		}
	}
	
	/*
	 * Desc - delete the review based on review_id
	 * URL - http://host:port/reviews/{id}
	 * Method - DELETE
	 */
	@DeleteMapping("/{review_id}")
	public ResponseEntity<?> deleteReviewById(@PathVariable Long review_id)
	{
		System.out.println("Inside delete Review by Id");
		return ResponseEntity.ok(reviewsService.deleteReviewById(review_id));
		
	}
	
	/*
	 * Desc - add the new review for the product
	 * URL - http://host:port/reviews/
	 * Method - POST
	 */
	@PostMapping
	public ResponseEntity<?> addNewReview(@RequestBody ReviewsRequestDTO reviewsRequestDTO)
	{
		System.out.println("Received Review Request: " + reviewsRequestDTO);
		try {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(reviewsService.addNewReview(reviewsRequestDTO));
					
		} catch (RuntimeException e) {
			return ResponseEntity.
					status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage()));
		}
	}
	
	
	
}
