package sui.cloud.user.service;

import cn.hutool.core.util.IdUtil;
import sui.cloud.user.util.RedisUtil;
import sui.cloud.user.util.Result;

import com.wf.captcha.SpecCaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @GetMapping("/info")
    public Result info(){
        return new Result(userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()),200,"OK");
    }
    @GetMapping("/captcha")
    public Result captcha(){
    	SpecCaptcha  captcha=new SpecCaptcha ();
        String uuid=IdUtil.randomUUID();
        redisUtil.set("captcha|"+uuid,captcha.text(),60*5);
        Map<String,Object> map=new HashMap<>();
        map.put("key", uuid);
        map.put("captcha",captcha.toBase64());
        return new Result(map,200,"OK");
    }
}
