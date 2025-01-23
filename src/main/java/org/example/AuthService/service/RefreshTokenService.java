package org.example.AuthService.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.example.AuthService.Entity.RefreshToken;
import org.example.AuthService.Entity.UserInfo;
import org.example.AuthService.Repository.RefreshTokenRepository;
import org.example.AuthService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@Autowired
	UserRepository userRepository;

	public RefreshToken createRefreshToken(String username) {
		UserInfo userInfoExtracted = userRepository.findByUsername(username);
		RefreshToken refreshToken = RefreshToken.builder().userInfo(userInfoExtracted)
				.token(UUID.randomUUID().toString()).expiryDate(Instant.now().plusMillis(600000)).build();
		return refreshTokenRepository.save(refreshToken);
	}
	
	public RefreshToken verifyExpiration(RefreshToken token)
	{
		if(token.getExpiryDate().compareTo(Instant.now())<0)
		{
			refreshTokenRepository.delete(token);
			throw new RuntimeException(token.getToken()+ "Refresh token is expired. Please make a new login...!");
		}
		return token;
	}
	
	public Optional<RefreshToken> findByToken(String token)
	{
		return refreshTokenRepository.findByToken(token);
	}

}
