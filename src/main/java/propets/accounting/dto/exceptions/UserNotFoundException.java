package propets.accounting.dto.exceptions;

public class UserNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7427107060021266782L;

    public UserNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
        // also TODO http status sth
    }

    
    
}
