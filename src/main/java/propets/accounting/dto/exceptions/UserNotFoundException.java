package propets.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7427107060021266782L;

    public UserNotFoundException(String id) {
        super("User "+id+" not found!");
        // TODO Auto-generated constructor stub
        // also TODO http status sth
    }

    
    
}
