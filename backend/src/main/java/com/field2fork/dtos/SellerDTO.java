package com.field2fork.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO to transfer Seller data to the frontend.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerDTO {
    private Long id;
    private String username;
    private String email;
    private String contactNumber;
    private String Location;
    private Float rating;
    private Boolean activeStatus;
}
