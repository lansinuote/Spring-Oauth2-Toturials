package lee;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class TT {

    public static String authorization_server = "http://127.0.0.1:3001/";
    public static String resource_server = "http://127.0.0.1:4001/";

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

    //校验token
    @Test
    public void checkToken() throws IOException {
        String[] params = new String[]{
                "token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIiwic2NvcGUyIl0sImV4cCI6MTYxMzk4Nzk5OCwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiNzM1MTBmNDUtODY4ZS00ZWQ2LTlmZmMtMzU5NzQ4OTBhMzQ5IiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9._oexcj4pjEik-cvG0c2g9lGvuXgdkaaVTQvaZG1gg_4",
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/check_token", null, null, params);
        //{"aud":["resource1"],"user_name":"admin","scope":["all"],"active":true,"exp":1582620698,"authorities":["admin"],"client_id":"client1"}
    }

    //使用token访问resource
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIiwic2NvcGUyIl0sImV4cCI6MTYxMzg5NDU0MiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiMDYzOTkwNWYtZGIxMi00NTA3LTgzNGQtYWZmYTIxYTU3MGVjIiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.x8FFZxne9_egr_99S--HILKrLquZ9Ao4y-x2TyC0vkA");
        HttpUtil.send(HttpMethod.POST, resource_server + "/admin", head, null);
    }
}