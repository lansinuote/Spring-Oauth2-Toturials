package lee.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

//定义一个自定义的filter
@Component
public class ScopeFilter extends ZuulFilter {

    @Override
    public String filterType() {
        //pre
        //routing
        //post
        //error
        return "pre";//定义是前置拦截器
    }

    @Override
    public int filterOrder() {
        return 0;//顺序
    }

    @Override
    public boolean shouldFilter() {
        return true;//开启
    }

    @Override
    public Object run() {

        //获取request对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String requestURI = request.getRequestURI();

        //如果是对授权中心的访问,则全部放过
        if (requestURI.startsWith("/authorization_server")) {
            return null;
        }

        //获取token
        String token = request.getHeader("Authorization");

        try {
            //解析token
            //JWTToken是由3部分组成的,以"."分割,第一段是头信息,第二段是授权信息,第三段是校验码
            //这里我们只需要第二段,取其中的scope做校验即可.
            //zuul只负责scope部分的校验,具体的权限由各个微服务自己做具体校验.
            token = token.split("\\.")[1];
            byte[] bytes = Base64.getUrlDecoder().decode(token);
            token = new String(bytes, "UTF-8");

            //解析为json对象
            JSONObject tokenJSON = JSONObject.parseObject(token);
            System.out.println(tokenJSON);

            //取scope
            List<String> scope = tokenJSON.getJSONArray("scope").toJavaList(String.class);

            //访问资源服务1,需要scope1的授权
            if (requestURI.startsWith("/resource_server1") && scope.contains("scope1")) {
                return null;
            }

            //访问资源服务2,需要scope2的授权
            if (requestURI.startsWith("/resource_server2") && scope.contains("scope2")) {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //未通过校验,返回403.
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(403);
        return null;
    }
}