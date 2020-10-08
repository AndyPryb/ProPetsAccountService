package propets.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 290636192653926387L;

    public UserExistsException(String id) {
        super("User "+id+" already exists!");
    }
    
}
