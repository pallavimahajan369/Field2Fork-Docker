package com.field2fork.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import com.field2fork.custom_exception.ResourceNotFoundException;
import com.field2fork.dao.UserDao;
import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.BuyerDTO;
import com.field2fork.dtos.SellerDTO;
import com.field2fork.dtos.UserDTO;
import com.field2fork.pojos.Role;
import com.field2fork.pojos.User;

import java.math.BigDecimal;
import java.math.RoundingMode;



@Service
@Transactional

public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	   @Autowired
	   private PasswordEncoder passwordEncoder; 
	   @Autowired
		private ModelMapper modelMapper;
	   
	   


	   @Override
	   public UserDTO loginUser (String username, String password) {
	       Optional<User> user = userDao.findByUsername(username);
	       
	       if (user.isPresent()) {
	           System.out.println("User  found: " + username);
	           System.out.println("Active status: " + user.get().getActiveStatus());
	           
	           // Print the stored hashed password
	           String storedHashedPassword = user.get().getPassword();
	           System.out.println("Stored hashed password: " + storedHashedPassword);
	           
	           // Check if the password matches
	           if (user.get().getActiveStatus() && passwordEncoder.matches(password, storedHashedPassword)) {
	               System.out.println("Password matches!");
	               
	               // Create a UserDTO from the User object
	               UserDTO userDto = new UserDTO(user.get());
	               // Set the password to null before returning
	               userDto.setPassword(null);
	               
	               return userDto; // Return the UserDTO with password set to null
	           } else {
	               System.out.println("Password does not match or account is inactive.");
	               throw new RuntimeException("Invalid username, password, or account is inactive!");
	           }
	       } else {
	           System.out.println("User  not found: " + username);
	           throw new RuntimeException("Invalid username, password, or account is inactive!");
	       }
	   }


	
	@Override
    public List<User> getAllUsers() {
        return userDao.findAll()
                .stream()
                .filter(User::getActiveStatus) // Ensure only active users are returned
                .collect(Collectors.toList());
    }



	 /**
	     * Fetches 10 buyers after the given user ID using pagination.
	     */
	 @Override
	    public List<BuyerDTO> getBuyersAfterId(Long lastId) {
	        Pageable pageable = PageRequest.of(0, 10);
	        return userDao.findNextBuyers(lastId, pageable)
	                .stream()
	                .map(user -> new BuyerDTO(
	                        user.getId(),
	                        user.getUsername(),
	                        user.getEmail(),
	                        user.getContactNumber(),
	                        user.getAddress(),
	                        user.getActiveStatus()
	                ))
	                .collect(Collectors.toList());
	    }
	    
	    @Override
	    public List<BuyerDTO> getBuyersBeforeId(Long firstId) {
	        List<User> buyers = userDao.findPreviousBuyers(firstId, PageRequest.of(0, 10));
	        Collections.reverse(buyers); // Reverse to maintain ascending order
	        return buyers.stream()
	                .map(user -> new BuyerDTO(
	                        user.getId(),
	                        user.getUsername(),
	                        user.getEmail(),
	                        user.getContactNumber(),
	                        user.getAddress(),
	                        user.getActiveStatus()
	                       
	                ))
	                .collect(Collectors.toList());
	    }
	    
	    
	    /**
	     * Fetches 10 sellers after the given user ID using pagination.
	     */
	    @Override
	    public List<SellerDTO> getSellersAfterId(Long lastId) {
	        Pageable pageable = PageRequest.of(0, 10);
	        return userDao.findNextSellers(lastId, pageable)
	                .stream()
	                .map(user -> new SellerDTO(
	                        user.getId(),
	                        user.getUsername(),
	                        user.getEmail(),
	                        user.getContactNumber(),
	                        user.getLocation(),
	                        user.getRating(),
	                        user.getActiveStatus()
	                ))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public List<SellerDTO> getSellersBeforeId(Long firstId) {
	        List<User> sellers = userDao.findPreviousSellers(firstId, PageRequest.of(0, 10));
	        Collections.reverse(sellers); // Reverse to maintain ascending order
	        return sellers.stream()
	                .map(user -> new SellerDTO(
	                        user.getId(),
	                        user.getUsername(),
	                        user.getEmail(),
	                        user.getContactNumber(),
	                        user.getLocation(),
	                        user.getRating(),
	                        user.getActiveStatus()
	                ))
	                .collect(Collectors.toList());
	    }



	    @Override
	    public List<SellerDTO> getSellerById(Long sellerId) {
	        Optional<User> userOptional = userDao.findById(sellerId);
	        
	        if (userOptional.isPresent()) {
	            User user = userOptional.get();
	            
	            // Check if the user has the SELLER role 
	            if (user.getRole() == Role.SELLER) {
	                return List.of(modelMapper.map(user, SellerDTO.class));
	            }
	        }
	        
	        return Collections.emptyList(); // Return an empty list instead of null
	    }
	    
	    @Override
	    public List<BuyerDTO> getBuyerById(Long buyerId) {
	        Optional<User> userOptional = userDao.findById(buyerId);
	        
	        if (userOptional.isPresent()) {
	            User user = userOptional.get();
	            
	            // Check if the user has the BUYER role
	            if (user.getRole() == Role.BUYER) {
	                return List.of(modelMapper.map(user, BuyerDTO.class));
	            }
	        }
	        
	        return Collections.emptyList(); // Return an empty list instead of null
	    }



		@Override
		public ApiResponse deleteUser(Long user_id) {
			String mesg = "Invalid Id!";
			User userData = userDao.findById(user_id).orElseThrow(() -> new ResourceNotFoundException(mesg));
			userData.setActiveStatus(false);
			userDao.save(userData);

			return new ApiResponse("User has been deleted");
		}



		@Override
		public ApiResponse restoreUser(Long user_id) {
			
			String mesg = "Invalid Id!";
			User userData = userDao.findById(user_id).orElseThrow(() -> new ResourceNotFoundException(mesg));
			userData.setActiveStatus(true);
			userDao.save(userData);

			return new ApiResponse("User has been restored");
		}
		
		@Override
		public String updateSeller(Long sellerId, SellerDTO sellerDTO) {
		    User seller = userDao.findById(sellerId)
		            .orElseThrow(() -> new ResourceNotFoundException("Seller not found!"));

		    // Ensure the user is a seller
		    if (seller.getRole() != Role.SELLER) {
		        throw new ResourceNotFoundException("User is not a seller!");
		    }

		    // Update only allowed fields
		    seller.setEmail(sellerDTO.getEmail());
		    seller.setContactNumber(sellerDTO.getContactNumber());
		    seller.setLocation(sellerDTO.getLocation());

		    // Rating shouldn't be updated manually so not adding that

		    userDao.save(seller);
		    return "Seller updated successfully!";
		}



		@Override
		public String updateBuyer(Long buyerId, BuyerDTO buyerDTO) {
			User buyer = userDao.findById(buyerId)
		            .orElseThrow(() -> new ResourceNotFoundException("Buyer not found!"));

		    // Ensure the user is a buyer
		    if (buyer.getRole() != Role.BUYER) {
		        throw new ResourceNotFoundException("User is not a Buyer!");
		    }

		    // Update only allowed fields
		    buyer.setEmail(buyerDTO.getEmail());
		    buyer.setContactNumber(buyerDTO.getContactNumber());
		    buyer.setAddress(buyerDTO.getAddress());


		    userDao.save(buyer);
		    return "Buyer updated successfully!";
		}
		
		
		@Override
		public String registerBuyer(UserDTO buyerDTO) {
		    if (userDao.existsByUsername(buyerDTO.getUsername())) {
		        return "Username already taken!";
		    }

		    User buyer = new User();
		    buyer.setUsername(buyerDTO.getUsername());
		    buyer.setEmail(buyerDTO.getEmail());
		    buyer.setContactNumber(buyerDTO.getContactNumber());
		    buyer.setAddress(buyerDTO.getAddress());
		    buyer.setRole(Role.BUYER);
		    buyer.setActiveStatus(true); // Default to active

		    // Hash the password before saving
		    buyer.setPassword(passwordEncoder.encode(buyerDTO.getPassword()));

		    userDao.save(buyer);
		    return "Buyer registered successfully!";
		}

		@Override
		public String registerSeller(UserDTO sellerDTO) {
		    if (userDao.existsByUsername(sellerDTO.getUsername())) {
		        return "Username already taken!";
		    }

		    User seller = new User();
		    seller.setUsername(sellerDTO.getUsername());
		    seller.setEmail(sellerDTO.getEmail());
		    seller.setContactNumber(sellerDTO.getContactNumber());
		    seller.setLocation(sellerDTO.getLocation());
		    seller.setRole(Role.SELLER);
		    seller.setActiveStatus(true); // Default to active
		    seller.setRating(sellerDTO.getRating() != null ? sellerDTO.getRating() : 0.0f); // Default rating

		    // Hash the password before saving
		    seller.setPassword(passwordEncoder.encode(sellerDTO.getPassword()));

		    userDao.save(seller);
		    return "Seller registered successfully!";
		}

		
		

		@Override
		public String updateSellerRating(Long sellerId, Float newRating) {
		    User seller = userDao.findById(sellerId)
		            .orElseThrow(() -> new ResourceNotFoundException("Seller not found!"));

		    if (seller.getRole() != Role.SELLER) {
		        throw new IllegalArgumentException("User is not a seller!");
		    }

		    if (newRating < 1.0 || newRating > 5.0) {
		        throw new IllegalArgumentException("Rating must be between 1.0 and 5.0!");
		    }

		    // Weighted Average Formula
		    Float currentRating = seller.getRating();
		    Integer totalReviews = seller.getTotalReviews();

		    Float updatedRating = ((currentRating * totalReviews) + newRating) / (totalReviews + 1);

		    // Round to 1 decimal place
		    BigDecimal roundedRating = new BigDecimal(updatedRating).setScale(1, RoundingMode.HALF_UP);

		    seller.setRating(roundedRating.floatValue());
		    seller.setTotalReviews(totalReviews + 1);

		    userDao.save(seller);

		    return "Seller rating updated successfully!";
		}


		



	
	

}
