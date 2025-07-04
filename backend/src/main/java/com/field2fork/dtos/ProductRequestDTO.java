package com.field2fork.dtos;

import java.math.BigDecimal;

import com.field2fork.pojos.ProductCategory;
import com.field2fork.pojos.ProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequestDTO {
	   
	    private String name;	
	    private String description;   
	    private BigDecimal pricePerUnit;
	    private Integer stockQuantity;
	    private ProductStatus status;
	    private ProductCategory category; 
	    private Long userId;
	    
	    public ProductRequestDTO(String name, String description, BigDecimal pricePerUnit, Integer stockQuantity,ProductCategory category) {
			super();
			this.name = name;
			this.description = description;
			this.pricePerUnit = pricePerUnit;
			this.stockQuantity = stockQuantity;
			this.category = category;
			
		}
	    
		public ProductRequestDTO(String name, String description, BigDecimal pricePerUnit, Integer stockQuantity,
				ProductStatus status,ProductCategory category) {
			super();
			this.name = name;
			this.description = description;
			this.pricePerUnit = pricePerUnit;
			this.stockQuantity = stockQuantity;
			this.status = status;
			this.category = category;
		}
		
		
	    
	    

}
