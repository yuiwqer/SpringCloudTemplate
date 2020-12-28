package sui.cloud.user.security.filter;

import com.alibaba.fastjson.JSON;

import sui.cloud.user.util.RedisUtil;
import sui.cloud.user.util.Result;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CaptchaFilter extends OncePerRequestFilter {
    private RedisUtil redisUtil;
    public CaptchaFilter(RedisUtil redisUtil){
        this.redisUtil=redisUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取地址信息进行判断,try判断验证码是否正确cache执行异常信息打印并结束过滤器链
        if(httpServletRequest.getRequestURI().equals("/user/login")&&httpServletRequest.getMethod().equals("POST")){
            try {
                String captcha=(String)redisUtil.get("captcha|"+httpServletRequest.getParameter("key"));
                if(!captcha.equals(httpServletRequest.getParameter("code"))){
                    throw new ServletRequestBindingException("验证码错误");
                }
                redisUtil.del("captcha|"+httpServletRequest.getParameter("key"));
            }catch (Exception e){
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter pw=httpServletResponse.getWriter();
                String res=JSON.toJSONString(new Result(null,500,"验证码错误"));
                pw.write(res);
                pw.flush();
                pw.close();
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
