package propets.accounting.service;

import org.springframework.stereotype.Component;

import propets.accounting.dto.UserDto;

@Component
public interface AccountingService {
	
	public UserDto registerUser();
	
}
