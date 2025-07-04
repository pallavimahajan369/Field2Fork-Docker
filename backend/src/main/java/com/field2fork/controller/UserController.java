package com.field2fork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.field2fork.custom_exception.ResourceNotFoundException;
import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.BuyerDTO;
import com.field2fork.dtos.DashboardStatsDTO;
import com.field2fork.dtos.SellerDTO;
import com.field2fork.dtos.UserDTO;
import com.field2fork.pojos.User;
import com.field2fork.service.DashboardService;
import com.field2fork.service.UserService;

@RestController
@RequestMapping("/users")

@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
	
	
	@Autowired
	private UserService userService;
	@Autowired
    private DashboardService dashboardService;
	
	
	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDto) {
	    try {
	        UserDTO responseDto = userService.loginUser(userDto.getUsername(), userDto.getPassword());
	        return ResponseEntity.ok(responseDto);
	    } catch (Exception e) {
	        return ResponseEntity.status(401).body(null); // Return null if credentials are invalid
	    }
	}
		 
	 
	 @GetMapping("/")
	    public ResponseEntity<List<User>> getAllUsers() {
	        return ResponseEntity.ok(userService.getAllUsers());
	    }
	 
	 
	 /**
	     * Endpoint to fetch paginated buyers after a given ID.
	     */
	 @GetMapping("/buyers/after/{lastId}")
	 public List<BuyerDTO> getBuyersAfter(@PathVariable Long lastId) {
	     return userService.getBuyersAfterId(lastId);
	 }

	 @GetMapping("/buyers/before/{firstId}")
	 public List<BuyerDTO> getBuyersBefore(@PathVariable Long firstId) {
	     return userService.getBuyersBeforeId(firstId);
	 }

	 
	 /**
	     * Endpoint to fetch paginated sellers after a given ID.
	     */
	 @GetMapping("/sellers/after/{lastId}")
	 public List<SellerDTO> getSellersAfter(@PathVariable Long lastId) {
	     return userService.getSellersAfterId(lastId);
	 }

	 @GetMapping("/sellers/before/{firstId}")
	 public List<SellerDTO> getSellerBefore(@PathVariable Long firstId) {
	     return userService.getSellersBeforeId(firstId);
	 }
	 
	 
	 /*
		 * Desc - Get Seller by id
		 * URL - http://host:port/users/sellers/{id}
		 * Method - GET
		 */
		@GetMapping("/sellers/{seller_id}")
		public ResponseEntity<?> getSellersById(@PathVariable Long seller_id)
		{
			System.out.println("in get seller by id");
			List<SellerDTO> seller = userService.getSellerById(seller_id);
			if (seller.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                .body("Seller not found or user is not a seller.");
			} else {
				return ResponseEntity.ok(seller);
			}
			
		}
		
		/*
		 * Desc - Get Buyer by id
		 * URL - http://host:port/users/buyers/{id}
		 * Method - GET
		 */
		@GetMapping("/buyers/{buyer_id}")
		public ResponseEntity<?> getBuyersById(@PathVariable Long buyer_id)
		{
			System.out.println("in get buyer by id");
			List<BuyerDTO> seller = userService.getBuyerById(buyer_id);
			if (seller.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                .body("Buyer not found or user is not a buyer.");
			} else {
				return ResponseEntity.ok(seller);
			}
			
		}
		
		
		/*
		 * Desc - Delete user by id 
		 * URL - http://host:port/users/{id}
		 * Method - PATCH Payload
		 */
		@PatchMapping("/{user_id}")
		public ResponseEntity<?> deleteUser(@PathVariable Long user_id) {
			System.out.println("in delete user " + user_id);
			return ResponseEntity.ok(
					userService.deleteUser(user_id));
		}
		
		/*
		 * Desc - Restore user by id 
		 * URL - http://host:port/users/{id}/restore
		 * Method - PATCH Payload
		 */
		@PatchMapping("/{user_id}/restore")
		public ResponseEntity<?> restoreUser(@PathVariable Long user_id) {
			System.out.println("in restore user " + user_id);
			return ResponseEntity.ok(
					userService.restoreUser(user_id));
		}
		
		/*
		 * Desc - update seller info
		 * URL - http://host:port/users/sellers/id
		 * Method - PUT Payload
		 */
		@PutMapping("/sellers/{sellerId}")
	    public ResponseEntity<?> updateSeller(@PathVariable Long sellerId, @RequestBody SellerDTO sellerDTO) {
	        try {
	            String response = userService.updateSeller(sellerId, sellerDTO);
	            return ResponseEntity.ok(new ApiResponse(response));
	        } catch (ResourceNotFoundException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Seller not found!"));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to update seller info."));
	        }
	    }
		
		/*
		 * Desc - update buyer info
		 * URL - http://host:port/users/buyers/id
		 * Method - PUT Payload
		 */
		@PutMapping("/buyers/{buyerId}")
	    public ResponseEntity<?> updateBuyer(@PathVariable Long buyerId, @RequestBody BuyerDTO BuyerDTO) {
	        try {
	            String response = userService.updateBuyer(buyerId, BuyerDTO);
	            return ResponseEntity.ok(new ApiResponse(response));
	        } catch (ResourceNotFoundException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Buyer not found!"));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to update Buyer info."));
	        }
	    }
		
		@PostMapping("/buyers/register")
	    public ResponseEntity<?> registerBuyer(@RequestBody UserDTO buyerDTO) {
	        try {
	            String response = userService.registerBuyer(buyerDTO);
	            return ResponseEntity.ok(new ApiResponse(response));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to register buyer."));
	        }
	    }

	    @PostMapping("/sellers/register")
	    public ResponseEntity<?> registerSeller(@RequestBody UserDTO sellerDTO) {
	        try {
	            String response = userService.registerSeller(sellerDTO);
	            return ResponseEntity.ok(new ApiResponse(response));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to register seller."));
	        }
	    }
	    
	    @PatchMapping("/sellers/{sellerId}/rate")
	    public ResponseEntity<?> updateSellerRating(@PathVariable Long sellerId, @RequestParam Float newRating) {
	        try {
	            String response = userService.updateSellerRating(sellerId, newRating);
	            return ResponseEntity.ok(new ApiResponse(response));
	        } catch (ResourceNotFoundException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage()));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to update rating."));
	        }
	    }
	    
	    @GetMapping("/dashboard-stats")
	    public DashboardStatsDTO getDashboardStats() {
	        return dashboardService.getDashboardStats();
	    }

}
