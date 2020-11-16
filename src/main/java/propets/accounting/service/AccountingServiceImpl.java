package propets.accounting.service;

import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import propets.accounting.dao.AccountingRepository;
import propets.accounting.dto.EditUserDto;
import propets.accounting.dto.RegisterUserDto;
import propets.accounting.dto.UserDataDto;
import propets.accounting.dto.UserDto;
import propets.accounting.dto.UserInfoDto;
import propets.accounting.dto.exceptions.InvalidRoleException;
import propets.accounting.dto.exceptions.UserExistsException;
import propets.accounting.dto.exceptions.UserNotFoundException;
import propets.accounting.model.UserAccount;

@Service
public class AccountingServiceImpl implements AccountingService {
    
    @Value("${header.token}") // SpEL spring expression language
    String tokenName;

    @Autowired
    AccountingRepository repository;
    
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ValidationService validationService;
    
    @Override
    @Transactional
    public ResponseEntity<UserDto> registerUser(RegisterUserDto registerUserDto) {
        if(repository.existsById(registerUserDto.getEmail())) {
            throw new UserExistsException(registerUserDto.getEmail());
        }
        UserAccount userAccount = new UserAccount(registerUserDto.getName(), registerUserDto.getEmail());
        userAccount.setPassword(BCrypt.hashpw(registerUserDto.getPassword(), BCrypt.gensalt()));
        repository.save(userAccount);
        UserDto userDto = modelMapper.map(userAccount, UserDto.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add(tokenName, validationService.createToken(userAccount));
        return new ResponseEntity<UserDto>(userDto, headers, HttpStatus.OK);
    }

    @Override
    @Transactional
    public UserDto getUser(String login) {
        UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto editUser(String login, EditUserDto editUserDto) {
        UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        if(editUserDto.getAvatar()!=null) {
            userAccount.setAvatar(editUserDto.getAvatar());
        }
        if(editUserDto.getName()!=null) {
            userAccount.setName(editUserDto.getName());
        }
        if(editUserDto.getPhone()!=null) {
            userAccount.setPhone(editUserDto.getPhone());
        }
        repository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto deleteUser(String login) {
        UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        repository.delete(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public ResponseEntity<UserInfoDto> tokenValidation(String token) {
        UserInfoDto userInfoDto = validationService.validateToken(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add(tokenName, userInfoDto.getToken());
        return new ResponseEntity<UserInfoDto>(userInfoDto, headers, HttpStatus.OK);
    }

	@Override
	public boolean blockUserAccount(String login, boolean status) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if(status) {
			userAccount.setBlocked(true);
			repository.save(userAccount);
			return true;
		} else {
			userAccount.setBlocked(false);
			repository.save(userAccount);
			return false;
		}
	}

	@Override
	public Set<String> addUserRole(String login, String role) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (role.equalsIgnoreCase("MODER") || role.equalsIgnoreCase("ADMIN")) {
			userAccount.addUserRole(role);
			repository.save(userAccount);
			return userAccount.getRoles();
		} else {
			throw new InvalidRoleException(role);
		}
	}

	@Override
	public Set<String> removeUserRole(String login, String role) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (role.equalsIgnoreCase("MODER") || role.equalsIgnoreCase("ADMIN")) {
			userAccount.removeUserRole(role);
			repository.save(userAccount);
			return userAccount.getRoles();
		} else {
			throw new InvalidRoleException(role);
		}
	}

	@Override
	public void addUserFavorite(String login, String postId) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userAccount.getFavorites().add(postId);
		repository.save(userAccount);
	}

	@Override
	public void addUserActivity(String login, String postId) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userAccount.getActivities().add(postId);
		repository.save(userAccount);
	}

	@Override
	public void removeUserFavorite(String login, String postId) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userAccount.getFavorites().remove(postId);
		repository.save(userAccount);
	}

	@Override
	public void removeUserActivity(String login, String postId) {
			UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
			userAccount.getActivities().remove(postId);
			repository.save(userAccount);
	}

	@Override
	public UserDataDto getUserData(String login, boolean dataType) {
        UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        if (dataType) {
        	return new UserDataDto(null, userAccount.getActivities());
        } else {
        	return new UserDataDto(userAccount.getFavorites(), null);
        }
//        if (dataType) {
//        	return new UserDataDto(userAccount.getActivityMessages(), userAccount.getActivityLostAndFound());
//        } else {
//        	return new UserDataDto(userAccount.getFavoriteMessages(), userAccount.getFavoriteLostAndFound()); 
//        }
	}
}
