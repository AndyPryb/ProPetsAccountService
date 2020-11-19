package propets.accounting.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import propets.accounting.dto.UserInfoDto;
import propets.accounting.dto.exceptions.TokenExpiredException;
import propets.accounting.model.UserAccount;

@Service
public class ValidationService {
    
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    ModelMapper modelMapper;
    
    public String[] getCredentialsFromBase64(String token) {
        token = token.split(" ")[1];
        String[] credentials = new String(Base64.getDecoder().decode(token)).split(":");
        return credentials;
    }
    
    public String createToken(UserAccount userAccount) {
        try {
            List<String> list = new ArrayList<String>();
            list.stream().map(a -> getCredentialsFromBase64(userAccount.getEmail())).collect(Collectors.toList());
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-login", userAccount.getEmail());
//            String login = userAccount.getEmail();
            UserInfoDto userInfoDto = new UserInfoDto(userAccount.getEmail(), userAccount.getName(), userAccount.getAvatar(), null);
            RequestEntity<UserInfoDto> requestEntity = new RequestEntity<>(userInfoDto, HttpMethod.POST, new URI("https://propets-validation-bruma.herokuapp.com/createToken"));
//            RequestEntity<String> requestEntity = new RequestEntity<String>(headers, HttpMethod.GET, new URI("https://propets-validation-bruma.herokuapp.com/createToken"));
            ResponseEntity<UserInfoDto> responseEntity = restTemplate.exchange(requestEntity, UserInfoDto.class);
            System.out.println(responseEntity.getBody().getEmail());
//            return responseEntity.getBody().getToken();
            return responseEntity.getHeaders().get("X-Token").get(0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("token creation failed");
            return null;
        }
    }
    
    public UserInfoDto validateToken(String token) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Token", token);
            RequestEntity<String> requestEntity = new RequestEntity<String>(headers, HttpMethod.POST, new URI("https://propets-validation-bruma.herokuapp.com/token"));
            ResponseEntity<UserInfoDto> responseEntity = restTemplate.exchange(requestEntity, UserInfoDto.class);
            if(responseEntity.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                throw new TokenExpiredException();
            }
            UserInfoDto userInfoDto = responseEntity.getBody();
            return userInfoDto;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("token validation failed");
            return null;
        }
        
    }
}

