package com.field2fork.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.field2fork.dtos.ReviewsRespDTO;
import com.field2fork.pojos.Reviews;

public interface ReviewsDao extends JpaRepository<Reviews, Long>{

	@Query("SELECT new com.field2fork.dtos.ReviewsRespDTO(r.id,r.review_date, r.rating, r.reviewText, " +
		       "r.product.id, r.product.name, r.user.id, r.user.username) FROM Reviews r")
		List<ReviewsRespDTO> findAllReviews();
	
	@Query("SELECT new com.field2fork.dtos.ReviewsRespDTO(r.id, r.review_date, r.rating, r.reviewText, " +
		       "r.product.id, r.product.name, r.user.id, r.user.username) " +
		       "FROM Reviews r WHERE r.product.id = :productId")
		List<ReviewsRespDTO> findReviewsByProductId(@Param("productId") Long productId);


}
