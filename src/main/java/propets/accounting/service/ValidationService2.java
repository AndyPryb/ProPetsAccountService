package propets.accounting.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import propets.accounting.dto.UserInfoDto;

@Service
public class ValidationService2 {
    
    @Autowired
    RestTemplate restTemplate;
    
    public String[] getCredentialsFromBase64(String token) {
        token = token.split(" ")[1];
        String[] credentials = new String(Base64.getDecoder().decode(token)).split(":");
        return credentials;
    }
    
    public String createToken(String login) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-login", login);
        RequestEntity<String> requestEntity = new RequestEntity<String>(headers, HttpMethod.GET, new URI("http://localhost:9000/createToken"));
        ResponseEntity<UserInfoDto> responseEntity = restTemplate.exchange(requestEntity, UserInfoDto.class);
        System.out.println(responseEntity.getBody().getEmail());
        return responseEntity.getHeaders().get("X-Token").get(0);
    }
    
    public UserInfoDto validateToken(String token) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Token", token);
        RequestEntity<String> requestEntity = new RequestEntity<String>(headers, HttpMethod.GET, new URI("http://localhost:9000/token"));
        ResponseEntity<UserInfoDto> responseEntity = restTemplate.exchange(requestEntity, UserInfoDto.class);
        UserInfoDto userInfoDto = responseEntity.getBody();
        return userInfoDto;
    }
}
