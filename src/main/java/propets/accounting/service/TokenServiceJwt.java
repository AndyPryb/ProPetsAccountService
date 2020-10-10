package propets.accounting.service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import propets.accounting.dto.UserInfoDto;
import propets.accounting.dto.exceptions.TokenExpiredException;
import propets.accounting.model.UserAccount;

@Service
public class TokenServiceJwt implements TokenService {

    @Autowired
    SecretKey secretKey;
    
    String secret = "yFMaanX_I-mO1tahpyeG1rqxAVRdiZQuqci__e1Y2QPoaTG6F27P1OKwAI8M9wRVZTIfOr-pZSet_ea7M9CGOrxxx7oWUlCct1tMHNbL9FVzyMcun8xTtyPPuUy4hAclR2i5sRteiqO5-37F5Ggw2CZAPXlyVEKZU13ALgzaWnM";

    final ChronoUnit UNIT = ChronoUnit.DAYS;
    
    @Override
    public String createToken(UserAccount userAccount) {
        return createToken(userAccount.getEmail());
    }

    @Override
    public UserInfoDto validateToken(String token) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        Claims claims = jws.getBody();
        Instant exp = Instant.ofEpochMilli(Long.parseLong(claims.get("exp").toString()));
        if(exp.isBefore(Instant.now())) {
            throw new TokenExpiredException();
        }
        claims.put("exp", Instant.now().plus(30, UNIT).toEpochMilli());
        token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();
        return new UserInfoDto((String)claims.get("sub"), token);
    }
    
    @Bean
    public SecretKey secretKey() {
        return new SecretKeySpec(Base64.getUrlEncoder().encode(secret.getBytes()), "AES");
    }

    @Override
    public String createToken(String login) {
        return Jwts.builder()
                .claim("iat", Instant.now().toEpochMilli())
                .claim("exp", Instant.now().plus(30, UNIT).toEpochMilli())
                .claim("sub", login)
//                .claim("isBlocked", userAccount.isBlocked())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}
