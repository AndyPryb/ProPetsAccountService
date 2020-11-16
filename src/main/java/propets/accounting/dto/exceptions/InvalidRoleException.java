package propets.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidRoleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1772446172155405270L;
	
    public InvalidRoleException(String role) {
        super("Role "+role+" does not exist!");
    }
}
