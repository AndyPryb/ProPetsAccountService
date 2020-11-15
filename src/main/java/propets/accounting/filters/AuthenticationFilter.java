package propets.accounting.filters;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import propets.accounting.dao.AccountingRepository;
import propets.accounting.dto.UserInfoDto;
import propets.accounting.dto.exceptions.UserNotFoundException;
import propets.accounting.model.UserAccount;
import propets.accounting.service.ValidationService;

@Service
@Order(10)
public class AuthenticationFilter implements Filter {
    
    @Autowired
    AccountingRepository repository;
    
    @Autowired
    ValidationService validationService;
    
    final String PREFIX = "/account/en/v1";

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        
        String login, token, basicToken;
        UserAccount userAccount = null;
        if(checkEndpoint(request.getServletPath(), request.getMethod())) {
            try {
                token = request.getHeader("X-Token"); // JWT
                basicToken = request.getHeader("Authorization");
                if(basicToken!=null) {
                    // basic auth
                    String[] credentials = validationService.getCredentialsFromBase64(basicToken);
                    login = credentials[0]; //userAccount.getEmail()
                    userAccount = repository.findById(login).orElse(null);
                    if(userAccount==null) {
                        response.sendError(404, "User not found!"); // may be 401? (to hide email availability information) anyway it shows 401 o_O
                        return;
                    }
                    if(!BCrypt.checkpw(credentials[1],userAccount.getPassword())) {
                        response.sendError(403, "wrong password!");
                        return;
                    }
                    token = validationService.createToken(userAccount);
                    response.setHeader("X-Token", token);
                } else {
                    if (token != null) {
                        UserInfoDto userInfoDto = validationService.validateToken(token);
                        login = userInfoDto.getEmail();
                        response.setHeader("X-Token", userInfoDto.getToken());
                    } else {
                        response.sendError(403, "token missing");
                        return;
                    }
                }
                request = new WrapperRequest(request, login);
            } catch (HttpClientErrorException e) {
                e.printStackTrace();
                response.sendError(403, "X-Token expired");
                return;
            } catch (UserNotFoundException e) {
                response.sendError(404, e.getMessage());
                return;
                
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(400, "auth");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String path, String method) {
//        boolean res = path.endsWith(PREFIX+"/login") && "POST".equalsIgnoreCase(method);
//        res = res || path.matches(PREFIX+"/.+/info") && "GET".equalsIgnoreCase(method);
//        res = res || path.matches(PREFIX+"/.+") && "PUT".equalsIgnoreCase(method);
//        res = res || path.matches(PREFIX+"/.+") && "DELETE".equalsIgnoreCase(method);
//        res = res || path.matches(PREFIX+"/.+/role/.+") && "PUT".equalsIgnoreCase(method);
//        System.out.println("Path: "+path);
//        System.out.println("Method: "+method);
//        System.out.println(res);
//        System.out.println();
//        return res;
        boolean res = path.matches(PREFIX+"/registration/?") && "POST".equalsIgnoreCase(method); // register endpoint
        return !res;
    }

    private class WrapperRequest extends HttpServletRequestWrapper {
        String user;

        public WrapperRequest(HttpServletRequest request, String user) {
            super(request);
            this.user = user;
        }

        @Override
        public Principal getUserPrincipal() {
            return new Principal() {

                @Override
                public String getName() {
                    return user;
                }
            };
        }
    }

    
}
