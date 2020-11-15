package propets.accounting.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import propets.accounting.dto.EditUserDto;
import propets.accounting.dto.RegisterUserDto;
import propets.accounting.dto.UserDto;
import propets.accounting.dto.UserInfoDto;

public interface AccountingService {
	
	ResponseEntity<UserDto> registerUser(RegisterUserDto registerUserDto);
	
	UserDto getUser(String login);
	
	UserDto editUser(String login, EditUserDto editUserDto);
	
	UserDto deleteUser(String login);
	
	UserDto changeUserRole(String login, boolean add, String role);
	
	boolean blockUserAccount(String login, boolean status);
	
	List<String> addUserRole(String login, String role);
	
	List<String> removeUserRole(String login, String role);
	
	void addUserFavorite(String login, String postId);
	
	void addUserActivity(String login, String postId);
	
	void removeUserFavorite(String login, String postId);
	
	void removeUserActivity(String login, String postId);
	
//	UserDataDto getUserData(String login, boolean dataType);
	
	ResponseEntity<UserInfoDto> tokenValidation(String token);
	
	
}
