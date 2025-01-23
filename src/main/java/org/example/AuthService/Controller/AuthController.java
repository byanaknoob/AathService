package org.example.AuthService.Controller;

import org.example.AuthService.Entity.RefreshToken;
import org.example.AuthService.Model.UserInfoDTO;
import org.example.AuthService.Response.JwtResponseDTO;
import org.example.AuthService.service.JwtService;
import org.example.AuthService.service.RefreshTokenService;
import org.example.AuthService.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class AuthController {
  
	@Autowired
	private JwtService jwtService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@PostMapping("auth/v1/signup")
	public ResponseEntity Signup(@RequestBody UserInfoDTO userInfoDTO) {

		try {
			Boolean isSignUped = userDetailsServiceImpl.signupUser(userInfoDTO);
			if (Boolean.FALSE.equals(isSignUped)) {
				return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
			}
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDTO.getUsername());
			String jwtToken = jwtService.GenerateToken(userInfoDTO.getUsername());
			return new ResponseEntity<>(
					JwtResponseDTO.builder().accessToken(jwtToken).token(refreshToken.getToken()).build(),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Exception is User Service", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
