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
import org.springframework.web.bind.annotation.RestController;

import propets.accounting.dto.EditUserDto;
import propets.accounting.dto.RegisterUserDto;
import propets.accounting.dto.UserDto;
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
        return null;
    }
    
    @DeleteMapping("/{login}")
    public UserDto deleteUser(@PathVariable String login) {
        return null;
    }
    
    @PutMapping("/{userLogin}/role/{role}")
    public Set<String> addUserRole(@PathVariable String userLogin, @PathVariable String role) {
        return null;
    }
    
    @DeleteMapping("/{login}/role/{role}")
    public Set<String> deleteUserRole(@PathVariable String login, @PathVariable String role) {
        return null;
    }
    
    @PutMapping("/{userLogin}/block/{status}")
    public boolean blockUserAccount(@PathVariable String userLogin, @PathVariable String status) {
        return false;
    }
    
    @PutMapping("/{login}/favorite/{id}")
    public void addUserActivity(@PathVariable String login, @PathVariable String id) {
        return;
    }
    
    public ResponseEntity<UserDto> tokenValidation(@RequestHeader("X-Token") String token) {
        return null;
    }
    
    // TODO ...
    
}
