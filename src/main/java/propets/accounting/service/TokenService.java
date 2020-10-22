package propets.accounting.service;

import java.util.Base64;

import propets.accounting.dto.UserInfoDto;
import propets.accounting.model.UserAccount;


public interface TokenService {
    String createToken(UserAccount userAccount);
    
//    String createToken(String login);
    
    UserInfoDto validateToken(String token);
    
    default String[] getCredentialsFromBase64(String token) {
        token = token.split(" ")[1];
        String[] credentials = new String(Base64.getDecoder().decode(token)).split(":");
        return credentials;
    }
    
}
