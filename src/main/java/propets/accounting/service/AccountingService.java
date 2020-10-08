package propets.accounting.service;

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
	
	// TODO ...
	
	ResponseEntity<UserInfoDto> tokenValidation(String token);
	
	
}
