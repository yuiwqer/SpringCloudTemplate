package sui.cloud.user.security.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sui.cloud.user.util.Result;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.stream.Collectors;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager=authenticationManager;
        this.setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest=new UsernamePasswordAuthenticationToken(request.getParameter("username"),request.getParameter("password"));
        this.setDetails(request,authRequest);
        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("token", JWT.create().withIssuer("Sui").withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*24*7L)).withClaim("rol",authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(Algorithm.HMAC256("Sui")));
        PrintWriter out=response.getWriter();
        out.write(JSON.toJSONString(new Result(null,200,"登陆成功")));
        out.flush();
        out.close();
    }

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setContentType("application/json;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.write(JSON.toJSONString(new Result(null,500,"登陆失败")));
        out.flush();
        out.close();
	}
}
