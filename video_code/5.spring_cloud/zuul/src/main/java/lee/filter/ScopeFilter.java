package lee.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@Component
public class ScopeFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/auth")) {
            return null;
        }

        String token = request.getHeader("Authorization");

        token = token.split("\\.")[1];

        byte[] bytes = Base64.getUrlDecoder().decode(token);
        try {
            token = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        JSONObject tokenJSON = JSONObject.parseObject(token);
        System.out.println(tokenJSON);

        List<String> scope = tokenJSON.getJSONArray("scope").toJavaList(String.class);

        if (requestURI.startsWith("/source1") && scope.contains("scope1")) {
            return null;
        }

        if (requestURI.startsWith("/source2") && scope.contains("scope2")) {
            return null;
        }

        currentContext.setSendZuulResponse(false);
        currentContext.setResponseStatusCode(403);
        return null;
    }
}
