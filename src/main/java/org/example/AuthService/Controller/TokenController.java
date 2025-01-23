package org.example.AuthService.Controller;

import java.util.Optional;

import org.example.AuthService.Entity.RefreshToken;
import org.example.AuthService.Entity.UserInfo;
import org.example.AuthService.Request.AuthRequestDTO;
import org.example.AuthService.Request.RefreshTokenRequestDTO;
import org.example.AuthService.Response.JwtResponseDTO;
import org.example.AuthService.service.JwtService;
import org.example.AuthService.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Controller
public class TokenController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("auth/v1/login")
	public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
		if (authentication.isAuthenticated()) {
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
			return new ResponseEntity<>(
					JwtResponseDTO.builder().accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
							.token(refreshToken.getToken()).build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("auth/v1/refreshToken")
	public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
		Optional<RefreshToken> optionalRefreshToken = refreshTokenService
				.findByToken(refreshTokenRequestDTO.getToken());

		if (optionalRefreshToken.isPresent()) {
			RefreshToken refreshToken = refreshTokenService.verifyExpiration(optionalRefreshToken.get());
			UserInfo userInfo = refreshToken.getUserInfo();

			String accessToken = jwtService.GenerateToken(userInfo.getUsername());
			return JwtResponseDTO.builder().accessToken(accessToken).token(refreshTokenRequestDTO.getToken()).build();
		} else {
			throw new RuntimeException("Refresh Token is not in DB..!!");
		}
	}
}
