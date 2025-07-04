package com.field2fork.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.field2fork.pojos.Role;
import com.field2fork.pojos.User;

public interface UserDao extends JpaRepository<User, Long> {

	boolean existsByUsername(String username);

	Optional<User> findByUsername(String username);
	
	/**
     * Custom query to get the next 10 buyers after the given ID.
     */
	@Query("SELECT u FROM User u WHERE u.id > :lastId AND u.role = 'BUYER' ORDER BY u.id ASC")
	List<User> findNextBuyers(Long lastId, Pageable pageRequest);

	@Query("SELECT u FROM User u WHERE u.id < :firstId AND u.role = 'BUYER' ORDER BY u.id DESC")
	List<User> findPreviousBuyers(Long firstId, Pageable pageRequest);
	
	/**
     * Custom query to get the next 10 buyers after the given ID.
     */
	@Query("SELECT u FROM User u WHERE u.id > :lastId AND u.role = 'SELLER' ORDER BY u.id ASC")
	List<User> findNextSellers(Long lastId, Pageable pageRequest);

	@Query("SELECT u FROM User u WHERE u.id < :firstId AND u.role = 'SELLER' ORDER BY u.id DESC")
	List<User> findPreviousSellers(Long firstId, Pageable pageRequest);
	
	 long countByActiveStatus(boolean activeStatus);

	    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
	    long countByRole(Role role);  // Use Role enum instead of String

	    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.activeStatus = :activeStatus")
	    long countByRoleAndActiveStatus(Role role, boolean activeStatus);

	Optional<User> findByEmail(String email);

}


