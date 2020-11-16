package propets.accounting.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import propets.accounting.dto.EditUserDto;
import propets.accounting.dto.RegisterUserDto;
import propets.accounting.dto.UserDto;
import propets.accounting.dto.UserInfoDto;
import propets.accounting.service.AccountingService;

@RestController
@RequestMapping("/account/en/v1")
public class AccountingController {

    @Autowired
    AccountingService service;

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        return service.registerUser(registerUserDto);
    }

    @PostMapping("/login")
    public UserDto login(Principal principal) {
        return service.getUser(principal.getName());
    }
    
    @GetMapping("/{login}/info")
    public UserDto getUser(@PathVariable String login) {
        return service.getUser(login);
    }
    
    @PutMapping("/{login}")
    public UserDto editUser(@PathVariable String login, @RequestBody EditUserDto editUserDto) {
        return service.editUser(login, editUserDto);
    }
    
    @DeleteMapping("/{login}")
    public UserDto deleteUser(@PathVariable String login) {
        return service.deleteUser(login);
    }
    
    @PutMapping("/{userLogin}/role/{role}")
    public Set<String> addUserRole(@PathVariable String userLogin, @PathVariable String role) {
        return service.addUserRole(userLogin, role);
    }
    
    @DeleteMapping("/{login}/role/{role}")
    public Set<String> deleteUserRole(@PathVariable String userLogin, @PathVariable String role) {
    	return service.removeUserRole(userLogin, role);
    }
    
    @PutMapping("/{userLogin}/block/{status}")
    public boolean blockUserAccount(@PathVariable String userLogin, @PathVariable boolean status) {
        return service.blockUserAccount(userLogin, status);
    }
    
    @PutMapping("/{login}/favorite/{id}")
    public void addUserFavorite(@PathVariable String login, @PathVariable String id) {
        service.addUserFavorite(login, id);
        return;
    }
    
    @PutMapping("/{login}/activity/{id}")
    public void addUserActivity(@PathVariable String login, @PathVariable String id) {
        service.addUserActivity(login, id);
    	return;
    }
    
    @DeleteMapping("/{login}/favorite/{id}")
    public void removeUserFavorite(@PathVariable String login, @PathVariable String id) {
        service.removeUserFavorite(login, id);
        return;
    }
    
    @DeleteMapping("/{login}/activity/{id}")
    public void removeUserActivity(@PathVariable String login, @PathVariable String id) {
    	service.removeUserActivity(login, id);
    	return;
    }
    
    @GetMapping("/{login}")
    public void getUserData(@PathVariable String login, @RequestParam boolean dataType) {
    	service.getUserData(login, dataType);
    }
    
    @GetMapping("/token")
    public ResponseEntity<UserInfoDto> tokenValidation(@RequestHeader("X-Token") String token) {
        return service.tokenValidation(token);
    }
    
}
