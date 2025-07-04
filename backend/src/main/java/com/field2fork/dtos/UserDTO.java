package com.field2fork.dtos;

import com.field2fork.pojos.Role;
import com.field2fork.pojos.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private Role role;
    private String contactNumber;
    private String address;
    private String location;
    private Float rating;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.contactNumber = user.getContactNumber();
        this.address = user.getAddress();

        if (user.getRole() == Role.BUYER) {
            this.location = null;
            this.rating = null;
        } else {
            this.location = user.getLocation();
            this.rating = user.getRating();
        }
    }
}
