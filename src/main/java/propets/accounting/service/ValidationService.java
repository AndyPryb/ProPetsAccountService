package propets.accounting.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import propets.accounting.dto.UserDto;
import propets.accounting.dto.UserInfoDto;
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
            UserDto userDto = modelMapper.map(userAccount, UserDto.class);
            System.out.println(userDto.toString());
            RequestEntity<UserDto> requestEntity = new RequestEntity<UserDto>(userDto, HttpMethod.GET, new URI("http://localhost:9000/createToken"));
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            return responseEntity.getBody();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    public UserInfoDto validateToken(String token) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Token", token);
            RequestEntity<String> requestEntity = new RequestEntity<String>(headers, HttpMethod.GET, new URI("http://localhost:9000/token"));
            ResponseEntity<UserInfoDto> responseEntity = restTemplate.exchange(requestEntity, UserInfoDto.class);
            UserInfoDto userInfoDto = responseEntity.getBody();
            return userInfoDto;
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
    }
}

