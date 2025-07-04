package com.field2fork.service;

import java.util.List;

import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.ReviewsRequestDTO;
import com.field2fork.dtos.ReviewsRespDTO;




public interface ReviewsService {

	List<ReviewsRespDTO> getAllReviews();

	List<ReviewsRespDTO> getReviewByProductId(Long review_id);

    ApiResponse deleteReviewById(Long review_id);

    ApiResponse addNewReview(ReviewsRequestDTO reviewsRequestDTO);

}
