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
                "code", "uE4h82",//这个code是从getCode()方法获取的
                "redirect_uri", "http://www.baidu.com"
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/token", null, null, params);
        //{"access_token":"415b010c-4891-41ef-90ce-1653ecd37658","token_type":"bearer","refresh_token":"02021d0e-34ed-42eb-8abc-f495b2dcd512","expires_in":133,"scope":"all"}
    }

    //简单模式
    @Test
    public void simpleMode() {
        //这个一般给纯前端的项目用,没有后台的
        //浏览器访问
        //http://127.0.0.1:3001/oauth/authorize?client_id=client1&response_type=token&scope=all&redirect_uri=http://www.baidu.com

        //重定向结果
        //https://www.baidu.com/#access_token=415b010c-4891-41ef-90ce-1653ecd37658&token_type=bearer&expires_in=60
    }

    //密码模式
    @Test
    public void getTokenByPassword() throws IOException {
        //直接用用户的账号密码去申请权限,会把密码泄露给客户端
        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "password",
                "username", "admin",
                "password", "admin"
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/token", null, null, params);
        //{"access_token":"8d29e6ea-6b92-4a9d-8c73-fe36dbf8ff35","token_type":"bearer","refresh_token":"ac9d9203-9106-427b-a285-287a20fe4a42","expires_in":299,"scope":"all"}
    }

    //客户端模式
    @Test
    public void getTokenByClient() throws IOException {
        //直接用客户端id去申请权限
        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "client_credentials"
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/token", null, null, params);
        //{"access_token":"4ed1469d-4e66-4379-acbc-22170079b831","token_type":"bearer","expires_in":299,"scope":"all"}
    }

    //校验token
    @Test
    public void checkToken() throws IOException {
        String[] params = new String[]{
                "token", "fef0dc3d-0569-4008-af18-2641924f64b0",
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
                "refresh_token", "60589e48-cdf3-4c5a-b0c4-a24c453f0819"
        };

        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/token", null, null, params);
        //{"access_token":"01f7b875-5ac6-49b1-ae42-251c399bddf4","token_type":"bearer","refresh_token":"60589e48-cdf3-4c5a-b0c4-a24c453f0819","expires_in":299,"scope":"scope1 scope2"}
    }

    //使用token访问resource
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer 58d08ff8-85af-4337-a3a2-cf7b435b0330");
        HttpUtil.send(HttpMethod.POST,resource_server + "/me",head,null);
    }
}