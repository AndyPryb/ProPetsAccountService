package propets.accounting.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import propets.accounting.dao.AccountingRepository;
import propets.accounting.dto.UserInfoDto;
import propets.accounting.service.ValidationService;

//@Service
//@Order(30)
public class AdminValidationFilter implements Filter {
    
    final String PREFIX = "/account/en/v1";
    
    @Autowired
    ValidationService validationService;
    
    @Autowired
    AccountingRepository repository;
    

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        
        String token;
        if(checkEndpoint(request.getServletPath(), request.getMethod())) { // X-Token should be ready at this moment
            token = request.getHeader("X-Token");
            UserInfoDto userInfoDto = validationService.validateToken(token);
            
            
        }
        
    }

    private boolean checkEndpoint(String path, String method) { // add/delete user role, block user account
        boolean res = path.matches(PREFIX+"/.+/?") && "DELETE".equalsIgnoreCase(method);
        return res;
    }

}
