package io.xiaochangbai.muchat.server.interceptor;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.xiaochangbai.muchat.server.contant.Constant;
import io.xiaochangbai.muchat.common.core.enums.ResultCode;
import io.xiaochangbai.muchat.server.exception.GlobalException;
import io.xiaochangbai.muchat.server.session.UserSession;
import io.xiaochangbai.muchat.server.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //从 http 请求头中取出 token
        String token = request.getHeader("accessToken");
        if (token == null) {
            log.error("未登陆，url:{}",request.getRequestURI());
            throw new GlobalException(ResultCode.NO_LOGIN);
        }
        try{
            //验证 token
            JwtUtil.checkSign(token, Constant.ACCESS_TOKEN_SECRET);
        }catch (
        JWTVerificationException e) {
            log.error("token已失效，url:{}",request.getRequestURI());
            throw new GlobalException(ResultCode.INVALID_TOKEN);
        }
        // 存放session
        String  strJson = JwtUtil.getInfo(token);
        UserSession userSession = JSON.parseObject(strJson,UserSession.class);
        request.setAttribute("session",userSession);
        return true;
    }
}
