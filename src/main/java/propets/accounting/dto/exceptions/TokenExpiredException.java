package propets.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class TokenExpiredException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2452450897901400791L;
//
//    public TokenExpiredException() {
//        super("Token expired!");
//    }
    
    

}
