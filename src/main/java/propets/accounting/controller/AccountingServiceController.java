package propets.accounting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import propets.acconting.service.AccountingService;
import propets.accounting.dto.RegisterUserDto;
import propets.accounting.dto.UserDto;

@RestController
public class AccountingServiceController {
	
	@Autowired
	AccountingService service;
	
	@PostMapping("/account/en/v1/registration")
	public UserDto registerUser(@RequestBody RegisterUserDto registerUserDto) {
		return null;
	}
}
