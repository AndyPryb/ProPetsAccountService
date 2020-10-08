package propets.accounting.dto.exceptions;

public class UserExistsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 290636192653926387L;

    public UserExistsException(String id) {
        super("User "+id+" already exists!");
    }
    
}
