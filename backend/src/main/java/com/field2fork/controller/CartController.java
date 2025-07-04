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
import com.field2fork.dtos.CartRequestDTO;
import com.field2fork.dtos.CartResponseDTO;
import com.field2fork.pojos.Order;
import com.field2fork.service.CartService;



@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
	@Autowired
	private CartService cartService;

	public CartController() {
		System.out.println("in ctor " + getClass());
	}
	
	/*1.
	 * Desc - get all cart details 
	 * URL - http://host:port/cart 
	 * Method - GET
	 */
	@GetMapping
	public ResponseEntity<?> getAllcartDetails() {
		System.out.println("in get all cart details");
		List<CartResponseDTO> carts = 
				cartService.getAllcartDetails();
		if (carts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok(carts);
		}
	}
	
	/*2.
	 * Desc - get all cart item details by cart id
	 * URL - http://host:port/cart/2 
	 * Method - GET
	 */
	 @GetMapping("/{id}")
	 public CartResponseDTO getCartDetailsById(@PathVariable Long id) {
		 
	    return cartService.getCartDetailsById(id);
	 }
	 
	 /* 3. 
	  * Desc - delete cart item by cart ID and cart item ID 
	  * URL - http://host:port/cart/{cartId}/items/{cartItemId} 
	  * Method - DELETE
	  */
	 
	  @DeleteMapping("/{cart_id}/items/{cart_item_id}")
	  public ResponseEntity<?> deleteCartItem(@PathVariable Long cart_id, @PathVariable Long cart_item_id) {
		  try { 
	         return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cartService.deleteCartItemByCartId(cart_id, cart_item_id));
	      }catch(RuntimeException e) {
	    	  return ResponseEntity.
	    				status(HttpStatus.NOT_FOUND)
	    				.body(new ApiResponse(e.getMessage()));
	         }
	      }
	  
	  
	//4.  New endpoint to fetch cart details using userId
	  
	  @GetMapping("/user/{userId}")
	  public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) {
	      try {
	          CartResponseDTO cartResponse = cartService.getCartByUserId(userId);
	          return ResponseEntity.ok(cartResponse);
	      } catch (RuntimeException e) {
	          return ResponseEntity
	                  .status(HttpStatus.NOT_FOUND)
	                  .body(new ApiResponse(e.getMessage()));
	      }
	  }

	  
	  
	  @PostMapping("/add")
	    public ResponseEntity<?> addItemsToCart(@RequestBody CartRequestDTO request) {
	        try {
	    		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemsToCart(request));
	    	}
	    	catch(RuntimeException e) {
	    		return ResponseEntity.
	    				status(HttpStatus.BAD_REQUEST)
	    				.body(new ApiResponse(e.getMessage()));
	    	}
	    }
	  
	  @PostMapping("/checkout/{cartId}")
	    public ResponseEntity<?> checkoutCart(@PathVariable Long cartId) {
	        try {
	    		return ResponseEntity.status(HttpStatus.CREATED).body( cartService.checkoutCart(cartId));
	    	}
	    	catch(RuntimeException e) {
	    		return ResponseEntity.
	    				status(HttpStatus.BAD_REQUEST)
	    				.body(null);
	    	}
	    }
}
