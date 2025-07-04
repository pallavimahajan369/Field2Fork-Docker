package com.field2fork.pojos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"cartItems","user"})
@EqualsAndHashCode(callSuper = true)
@Table(name = "cart")
public class Cart extends BaseEntity {
	
	@Id
	@Column(name = "cart_id") 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false,unique = false)
    private User user;


     @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<CartItem> cartItems;

     public Cart(User user) {
    	    this.user = user;
    	    this.cartItems = new ArrayList<>();
    	}

}
