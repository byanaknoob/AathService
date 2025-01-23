package org.example.AuthService.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.example.AuthService.Entity.UserInfo;
import org.example.AuthService.Model.UserInfoDTO;
import org.example.AuthService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor; 

@AllArgsConstructor
@NoArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService 
{
	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private  PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userRepository.findByUsername(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("could not found user...!!");
		}
		return new CustomUserDetails(user);
	}
	
	public UserInfo checkIfUserAlreadyExsist(UserInfoDTO userInfoDTO)
	{
		return userRepository.findByUsername(userInfoDTO.getUsername());
	}
	
	public Boolean signupUser(UserInfoDTO userInfoDTO)
	{
		// yehi validate kar sakte hai email or password ko
		userInfoDTO.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));
		if(Objects.nonNull(checkIfUserAlreadyExsist(userInfoDTO)))
		{
			return false;
		}
		String userId = UUID.randomUUID().toString();
		userRepository
				.save(new UserInfo(userId, userInfoDTO.getUsername(), userInfoDTO.getPassword(), new HashSet<>()));
		return true;
	}

}
