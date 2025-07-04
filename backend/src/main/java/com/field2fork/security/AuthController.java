package com.field2fork.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.field2fork.dtos.ApiResponse;
//import com.osms.dtos.ForgotPasswordRequestDTO;
//import com.osms.dtos.ResetPasswordRequestDTO;
import com.field2fork.dtos.UserDTO;
//import com.osms.dtos.UserUpdateReqDTO;
import com.field2fork.pojos.User;
import com.field2fork.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:5173")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender mailSender;

	@PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // Authenticate the user using email and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Retrieve user details
        CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUserEntity();

        // Check if the user is active (active status must be 1)
        if (user.getActiveStatus() != true) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Account is locked. Please contact support."));
        }

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Build the response with token and user details
        Map<String, Object> response = Map.of("token", jwt, "user",
                Map.of(
                        "id", user.getId() != null ? user.getId() : "null",
                        "email", user.getEmail() != null ? user.getEmail() : "null",
                        "address", user.getAddress() != null ? user.getAddress() : "null",
                        "contactNumber", user.getContactNumber() != null ? user.getContactNumber() : "null",
                        "username", user.getUsername() != null ? user.getUsername() : "null",
                        "role", user.getRole() != null ? user.getRole().name() : "null",
                        "location", user.getLocation() != null ? user.getLocation() : "null",
                        "rating", user.getRating() != null ? user.getRating() : "null"
                ));

        return ResponseEntity.ok(response);
    }

//	@PostMapping("/forgot-password")
//	public ApiResponse forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
//		String email = request.getEmail();
//		User user = userService.findUserByEmail(email);
//
//		if (user == null) {
//			return new ApiResponse("Email not found");
//		}
//
//		// Generate a unique reset token
//		String token = UUID.randomUUID().toString();
//
//		// database me save token
//		userService.savePasswordResetToken(user, token);
//
//		// Send email with the reset link
//		String resetLink = "http://localhost:3000/reset-password/" + token;
//		sendResetPasswordEmail(email, resetLink);
//
//		return new ApiResponse("Password reset link sent to your email.");
//	}

	public void sendResetPasswordEmail(String email, String resetLink) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setFrom("no-reply@yourdomain.com");
			helper.setSubject("🔒 Password Reset Request");

			String emailContent = "<html>" + "<body style=\"font-family: Arial, sans-serif; text-align: center;\">"
					+ "<h2 style=\"color: #007bff;\">Password Reset Request</h2>" + "<p>Hello,</p>"
					+ "<p>You recently requested to reset your password. Click the button below to proceed:</p>" + "<p>"
					+ "<a href=\"" + resetLink + "\" "
					+ "style=\"background: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;\">"
					+ "Reset Password" + "</a>" + "</p>"
					+ "<p>If you did not request a password reset, please ignore this email.</p>"
					+ "<p>Best Regards,</p>" + "<p><b>Online Society Management system</b></p>" + "</body>" + "</html>";

			helper.setText(emailContent, true); // Enables HTML content

			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to send email", e);
		}
	}
//
//	@PostMapping("/reset-password/{token}")
//	public ApiResponse resetPassword(@PathVariable String token, @RequestBody ResetPasswordRequestDTO request) {
//		String newPassword = request.getNewPassword();
//		User user = userService.findUserByResetToken(token);
//
//		if (user == null) {
//			return new ApiResponse("Invalid or expired token");
//		}
//
//		userService.updatePassword(user, newPassword);
//		return new ApiResponse("Password reset successfully.");
//	}
	
//
//	@GetMapping("/getall/{id}")
//	public ResponseEntity<UserUpdateReqDTO> getUserById(@PathVariable Long id) {
//		UserUpdateReqDTO user = userService.getUserById(id);
//		return ResponseEntity.ok(user);
//	}
//
//	
//	// Update user details
//	@PutMapping("/updateone/{id}")
//	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateReqDTO updateUserDTO) {
//		userService.updateUser(id, updateUserDTO);
//		return ResponseEntity.ok("User updated successfully!");
//	}
}
