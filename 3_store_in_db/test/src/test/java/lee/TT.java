package lee;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class TT {

    public static String authorization_server = "http://127.0.0.1:3001/";
    public static String resource_server = "http://127.0.0.1:4001/";

    //授权码模式
    @Test
    public void getTokenByCode() throws IOException {
        //标准模式
        //浏览器访问
        //http://127.0.0.1:3001/oauth/authorize?client_id=client1&response_type=code&scope=scope1&redirect_uri=http://www.baidu.com

        //重定向结果
        //https://www.baidu.com/?code=r9zqyd

        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "authorization_code",
                "code", "T6c0dN",//这个code是从getCode()方法获取的
                "redirect_uri", "http://www.baidu.com"
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/token", null, null, params);
        //{"access_token":"415b010c-4891-41ef-90ce-1653ecd37658","token_type":"bearer","refresh_token":"02021d0e-34ed-42eb-8abc-f495b2dcd512","expires_in":133,"scope":"all"}
    }

    //校验token
    @Test
    public void checkToken() throws IOException {
        String[] params = new String[]{
                "token", "0a5ad08f-34f8-4e3f-9aed-2d4af56ca10d",
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/check_token", null, null, params);
        //{"aud":["resource1"],"user_name":"admin","scope":["all"],"active":true,"exp":1582620698,"authorities":["admin"],"client_id":"client1"}
    }

    //刷新token
    @Test
    public void refreshToken() throws IOException {
        //使用有效的refresh_token去重新生成一个token,之前的会失效
        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "refresh_token",
                "refresh_token", "d3b51d0f-61e5-4e0e-b3dd-8041e4f0c467"
        };

        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/token", null, null, params);
        //{"access_token":"01f7b875-5ac6-49b1-ae42-251c399bddf4","token_type":"bearer","refresh_token":"60589e48-cdf3-4c5a-b0c4-a24c453f0819","expires_in":299,"scope":"scope1 scope2"}
    }

    //使用token访问resource
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer 0a5ad08f-34f8-4e3f-9aed-2d4af56ca10d");
        HttpUtil.send(HttpMethod.POST,resource_server + "/admin",head,null);
    }
}