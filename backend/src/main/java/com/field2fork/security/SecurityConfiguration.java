package com.field2fork.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityConfiguration {

	private final CustomJWTAuthenticationFilter customJWTAuthenticationFilter;

	public SecurityConfiguration(CustomJWTAuthenticationFilter customJWTAuthenticationFilter) {
		this.customJWTAuthenticationFilter = customJWTAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain authorizeRequests(HttpSecurity http) throws Exception {
		http
		
		.csrf(csrf -> csrf.disable())
		.cors(withDefaults()) // ✅ Enable CORS from CorsConfig
				
				.authorizeHttpRequests(requests -> requests
						.requestMatchers("/auth/login", "/auth/forgot-password", "/auth/reset-password/{token}",
								"/orders/**","/payments/**","/v*/api-doc*/**", "/swagger-ui/**",
								"/users/buyers/register","/users/sellers/register",
								"/products","/products/categories","/products/{product_id}","/products/category","/reviews","/api/**",
								"/product-images/product/{productId}","/product-images/images/{id}","/orders","/reviews/{review_id}",
								"/cart/**","/product-images/{id}","/products/{product_id}")
						.permitAll().requestMatchers(HttpMethod.OPTIONS).permitAll() // CORS support

						// Admin Endpoints (Accessible only to Admins)
						.requestMatchers("/users/buyers/after/{lastId}","/users/buyers/before/{firstId}","/users/",
								"/users/sellers/after/{lastId}","/users/sellers/before/{firstId}","/users/sellers/{seller_id}",
								"/users/buyers/{buyer_id}","/users/{user_id}","/users/{user_id}/restore",
								"/users/dashboard-stats").hasAuthority("ROLE_ADMIN")

						// Seller Endpoints (Accessible only to Seller)
						.requestMatchers("/users/sellers/{sellerId}","/products/add",
								"/products/seller/{sellerId}","/product-images/upload",
								"/sales-report/{sellerId}").hasAuthority("ROLE_SELLER")

						// Buyer Endpoints (Accessible only to Buyer)
						.requestMatchers("/users/buyers/{buyerId}","/users/sellers/{sellerId}/rate","/reviews/{product_id}",
								"/orders/user/{userId}","/orders/Items","/orders/{orderId}/items","/orders/{orderId}").hasAuthority("ROLE_BUYER")
						
						.requestMatchers("/products/{product_id}/restore").hasAnyAuthority("ROLE_ADMIN","ROLE_SELLER")
						
						.requestMatchers("/orders/{orderId}/status").hasAnyAuthority("ROLE_SELLER","ROLE_BUYER")

						.requestMatchers("/auth/getall/{id}", "/auth/updateone/{id}").hasAnyAuthority("ROLE_SELLER","ROLE_ADMIN","ROLE_BUYER")

						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(customJWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
//added bcrytpasswordencoder
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // securing the password
	}
}