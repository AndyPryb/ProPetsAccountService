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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import propets.accounting.dto.UserInfoDto;
import propets.accounting.service.ValidationService;

@Service
@Order(value = 20)
public class UserValidationFilter implements Filter {
    
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    ValidationService validationService;

    final String PREFIX = "/account/en/v1";

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        
        String token;
        if(checkEndpoint(request.getServletPath(), request.getMethod())) { // X-Token should be ready at this moment (from previous filter)
            try {
                token = request.getHeader("X-Token");
                UserInfoDto userInfoDto = validationService.validateToken(token);
                
                System.out.println("Userinfodto email: "+userInfoDto.getEmail());
                System.out.println(request.getServletPath().matches(".*/"+userInfoDto.getEmail()+"/?")); 
                
                if(!request.getUserPrincipal().getName().equalsIgnoreCase(userInfoDto.getEmail())) {
                    response.sendError(401, "User validation failed! Email "+userInfoDto.getEmail()+" does not match!");
                    return;
                }
            } catch (HttpClientErrorException e) {
                response.sendError(403, "X-Token expired!");; // token expired
                return;
            } catch (Exception e) {
                response.sendError(400, "validation"); // sth wrong
                e.printStackTrace();
                return;
            }
            
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String path, String method) { // edit and remove user
        boolean res = path.matches(PREFIX+"/.+/?") && "PUT".equalsIgnoreCase(method);
        res = res || path.matches(PREFIX+"/.+/?") && "DELETE".equalsIgnoreCase(method);
        
        System.out.println("VPath: "+path);
        System.out.println("VMethod: "+method);
        System.out.println(res);
        System.out.println();
        
        return res;
    }
    
}
