package org.example.AuthService.Repository;

import org.example.AuthService.Entity.RefreshToken;
import org.example.AuthService.Entity.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String>
{
  
	public UserInfo findByUsername(String username);
}
