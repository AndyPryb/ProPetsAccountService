package propets.accounting.service;

import java.util.List;

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
    TokenService tokenService;
    
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
        headers.add(tokenName, tokenService.createToken(userAccount));
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
    public UserDto changeUserRole(String login, boolean add, String role) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<UserInfoDto> tokenValidation(String token) {
        UserInfoDto userInfoDto = tokenService.validateToken(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add(tokenName, userInfoDto.getToken());
        return new ResponseEntity<UserInfoDto>(userInfoDto, headers, HttpStatus.OK);
    }

	@Override
	public boolean blockUserAccount(String login, boolean status) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> addUserRole(String login, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> removeUserRole(String login, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUserFavorite(String login, String postId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUserActivity(String login, String postId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserFavorite(String login, String postId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserActivity(String login, String postId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserDataDto getUserData(String login, boolean dataType) {
		// TODO Auto-generated method stub
		return null;
	}

}
