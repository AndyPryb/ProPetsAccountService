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
import org.springframework.stereotype.Service;

import propets.accounting.dao.AccountingRepository;
import propets.accounting.dto.UserInfoDto;
import propets.accounting.model.UserAccount;
import propets.accounting.service.TokenService;

@Service
public class AuthenticationFilter implements Filter {
    
    @Autowired
    TokenService tokenService;
    
    @Autowired
    AccountingRepository repository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        
        String login, token, basicToken;
        if(checkEndpoint(request.getServletPath(), request.getMethod())) {
            try {
                token = request.getHeader("X-Token");
                basicToken = request.getHeader("Authorization");
                if(basicToken!=null) {
                    // basic auth
                    String[] credentials = tokenService.getCredentialsFromBase64(basicToken);
                    UserAccount userAccount = repository.findById(credentials[0]).orElse(null);
                    if(userAccount==null) {
                        response.sendError(401);
                        return;
                    }
                    if(!BCrypt.checkpw(credentials[1],userAccount.getPassword())) {
                        response.sendError(403);
                        return;
                    }
                    login = credentials[0]; //userAccount.getEmail()
                    response.setHeader("X-Token", tokenService.createToken(userAccount));
                } else {
                    if (token != null) {
                        UserInfoDto userInfoDto = tokenService.validateToken(token);
                        login = userInfoDto.getEmail();
                        response.setHeader("X-Token", userInfoDto.getToken());
                    } else {
                        response.sendError(403);
                        return;
                    }
                }
                request = new WrapperRequest(request, login);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(400);
                return;
            }
        }
        
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String path, String method) {
        boolean res = path.endsWith("/login") && "POST".equalsIgnoreCase(method);
        res = res || path.matches(".*/.+/info") && "GET".equalsIgnoreCase(method);
        System.out.println("Path: "+path);
        System.out.println("Method: "+method);
        System.out.println(res);
        System.out.println();
        return res;
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
