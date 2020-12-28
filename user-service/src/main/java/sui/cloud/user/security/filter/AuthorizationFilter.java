package sui.cloud.user.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import sui.cloud.user.service.Role;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authRequest=authentication(request);
        if(authRequest==null){
            chain.doFilter(request,response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        chain.doFilter(request,response);
    }
    private UsernamePasswordAuthenticationToken authentication(HttpServletRequest request){
        String token=request.getHeader("token");
        if(token==null){
            return null;
        }
        try {
            DecodedJWT jwt=JWT.require(Algorithm.HMAC256("Sui")).build().verify(token);
            return new UsernamePasswordAuthenticationToken(jwt.getIssuer(),null,jwt.getClaim("rol").asList(Role.class));
        }catch (JWTVerificationException e){
            e.printStackTrace();
            return null;
        }
    }
}
