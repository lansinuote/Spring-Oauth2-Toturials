package lee;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class TT {

    public static String zuul = "http://127.0.0.1:5001/";

    //授权码模式
    @Test
    public void getTokenByCode() throws IOException {
        //标准模式
        //浏览器访问
        //http://127.0.0.1:5001/authorization_server/oauth/authorize?client_id=client1&response_type=code&scope=scope1&redirect_uri=http://www.baidu.com

        //重定向结果
        //https://www.baidu.com/?code=r9zqyd

        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "authorization_code",
                "code", "j3ofvR",//这个code是从getCode()方法获取的
                "redirect_uri", "http://www.baidu.com"
        };
        HttpUtil.send(HttpMethod.POST, zuul + "authorization_server/oauth/token", null, null, params);
        //{"access_token":"415b010c-4891-41ef-90ce-1653ecd37658","token_type":"bearer","refresh_token":"02021d0e-34ed-42eb-8abc-f495b2dcd512","expires_in":133,"scope":"all"}
    }

    //获取jwt token,把用户信息加密成token,服务端不存储token
    @Test
    public void getTokenByPassword() throws IOException {
        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "password",
                "username", "admin",
                "password", "admin"
        };
        HttpUtil.send(HttpMethod.POST, zuul + "authorization_server/oauth/token", null, null, params);
        /*
        {
            "access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIiwic2NvcGUyIl0sImV4cCI6MTU4MjcwMzYxMywiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiYjA5MDFlZDYtOTNjMC00MjhjLTg2MzEtMTBiZWY4ZmJkZTYzIiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.kogkaxd1x-XkfqIR8avqSL5y6caa0Kk_DFWjepjVO70",
            "token_type":"bearer",
            "refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIiwic2NvcGUyIl0sImF0aSI6ImIwOTAxZWQ2LTkzYzAtNDI4Yy04NjMxLTEwYmVmOGZiZGU2MyIsImV4cCI6MTU4MjcwNDgxMywiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiOThmYTc3NDQtNTU4ZS00MjI0LThmYjEtZThiNGY3ZjNlNGE5IiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.Ex8rX6eMfXr7_pmC6sftIfIvFExyjx4_lzRYZqWHeII",
            "expires_in":299,
            "scope":"scope1 scope2",
            "jti":"b0901ed6-93c0-428c-8631-10bef8fbde63"
        }*/
    }

    //验证jwt token
    @Test
    public void checkToken() throws IOException {
        String[] params = new String[]{
                "token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIiwic2NvcGUyIl0sImV4cCI6MTU4Mjc5MjA2NywiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiMjI0ZGM0N2MtOWI1My00NjgyLTlhZDktZjBjMDEyOTM1NWZjIiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.fbkYw6b6WNdvRxTwX-2dKlZvrI116JF9El_I35NceK4",
        };
        HttpUtil.send(HttpMethod.POST, zuul + "authorization_server/oauth/check_token", null, null, params);
        //{"aud":["resource1"],"user_name":"admin","scope":["scope1","scope2"],"active":true,"exp":1582703613,"authorities":["admin"],"jti":"b0901ed6-93c0-428c-8631-10bef8fbde63","client_id":"client1"}
    }

    //使用jwt token访问resource
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIl0sImV4cCI6MTU4Mjc5MzM3MywiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiZGI2NTYzMWYtNTE1Zi00YjlhLWJkZWUtMTlkMWQzNzk4NDU0IiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.gT9NRB19SDZJBRKwqzB5x3n6tqrkxXKMZbA8c8wlI2M");
        HttpUtil.send(HttpMethod.POST, zuul + "resource_server/me", head, null);
    }
}