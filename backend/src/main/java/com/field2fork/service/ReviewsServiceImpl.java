package com.field2fork.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.field2fork.custom_exception.ResourceNotFoundException;
import com.field2fork.dao.ProductDao;
import com.field2fork.dao.ReviewsDao;
import com.field2fork.dao.UserDao;
import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.ReviewsRequestDTO;
import com.field2fork.dtos.ReviewsRespDTO;
import com.field2fork.pojos.Product;
import com.field2fork.pojos.Reviews;
import com.field2fork.pojos.User;

@Service
@Transactional

public class ReviewsServiceImpl implements ReviewsService {

	@Autowired
	private ReviewsDao reviewsDao;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<ReviewsRespDTO> getAllReviews() {
		
		return reviewsDao.findAllReviews();
	}

	@Override
	public List<ReviewsRespDTO> getReviewByProductId(Long product_id) {
		return reviewsDao.findReviewsByProductId(product_id);
	}

	@Override
	public ApiResponse deleteReviewById(Long review_id) {
		String mesg = "Invalid Review Id!";
		if(reviewsDao.existsById(review_id))
		{
			reviewsDao.deleteById(review_id);
			mesg = "Review Deleted!";
			return new ApiResponse(mesg);
		}
		return new ApiResponse(mesg);
	}

	@Override
	public ApiResponse addNewReview(ReviewsRequestDTO reviewsRequestDTO) {
		
		Reviews transientReview = 
				modelMapper.map(reviewsRequestDTO, Reviews.class);
		
		User user = userDao.findById(reviewsRequestDTO.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not Found with Id "+reviewsRequestDTO.getUserId()));
		
		Product product = productDao.findById(reviewsRequestDTO.getProductId()).
				orElseThrow(() -> new ResourceNotFoundException("Product not found with Id "+reviewsRequestDTO.getProductId()));
		
		transientReview.setProduct(product);
		transientReview.setUser(user);
		transientReview.setRating(reviewsRequestDTO.getRating());
		transientReview.setReviewText(reviewsRequestDTO.getReviewText());
		
		Reviews persistentReview = reviewsDao.save(transientReview);
		return new ApiResponse("Added new Review with Id "+ persistentReview.getId());
	}

}
