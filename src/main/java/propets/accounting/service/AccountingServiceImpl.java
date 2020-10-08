package propets.accounting.service;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import propets.accounting.dao.AccountingRepository;
import propets.accounting.dto.EditUserDto;
import propets.accounting.dto.RegisterUserDto;
import propets.accounting.dto.UserDto;
import propets.accounting.dto.UserInfoDto;
import propets.accounting.dto.exceptions.UserExistsException;
import propets.accounting.dto.exceptions.UserNotFoundException;
import propets.accounting.model.UserAccount;

@Service
public class AccountingServiceImpl implements AccountingService {

    @Autowired
    AccountingRepository repository;
    
    @Autowired
    TokenService tokenService;
    
    @Autowired
    ModelMapper modelMapper;
    
    @Override
    public ResponseEntity<UserDto> registerUser(RegisterUserDto registerUserDto) {
        if(repository.existsById(registerUserDto.getEmail())) {
            throw new UserExistsException(registerUserDto.getEmail());
        }
        UserAccount userAccount = new UserAccount(registerUserDto.getName(), registerUserDto.getEmail());
        userAccount.setPassword(BCrypt.hashpw(registerUserDto.getPassword(), BCrypt.gensalt()));
        repository.save(userAccount);
        UserDto userResponseDto = modelMapper.map(userAccount, UserDto.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Token", tokenService.createToken(userAccount));
        return new ResponseEntity<UserDto>(userResponseDto, headers, HttpStatus.OK);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto editUser(String login, EditUserDto editUserDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserDto deleteUser(String login) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserDto changeUserRole(String login, boolean add, String role) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<UserInfoDto> tokenValidation(String token) {
        UserInfoDto userRoleDto = tokenService.validateToken(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Token", userRoleDto.getToken());
        return new ResponseEntity<UserInfoDto>(userRoleDto, headers, HttpStatus.OK);
    }

}
