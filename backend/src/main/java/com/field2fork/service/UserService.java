package com.field2fork.service;

import java.util.List;

import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.BuyerDTO;
import com.field2fork.dtos.SellerDTO;
import com.field2fork.dtos.UserDTO;
import com.field2fork.pojos.User;

public interface UserService {

	UserDTO loginUser(String username, String password);

	List<User> getAllUsers();

	/**
	     * Fetches 10 buyers after the given user ID using pagination.
	     */
	List<BuyerDTO> getBuyersAfterId(Long lastId);

	List<BuyerDTO> getBuyersBeforeId(Long firstId);

	List<SellerDTO> getSellersAfterId(Long lastId);

	List<SellerDTO> getSellersBeforeId(Long firstId);

	List<SellerDTO> getSellerById(Long seller_id);

	List<BuyerDTO> getBuyerById(Long buyer_id);

	ApiResponse deleteUser(Long user_id);

	ApiResponse restoreUser(Long user_id);

	String updateSeller(Long sellerId, SellerDTO sellerDTO);

	String updateBuyer(Long buyerId, BuyerDTO buyerDTO);

	String registerBuyer(UserDTO buyerDTO);

	String registerSeller(UserDTO sellerDTO);

	String updateSellerRating(Long sellerId, Float newRating);



}
