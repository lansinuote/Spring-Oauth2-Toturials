package lee;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class TT {

    public static String authorization_server = "http://127.0.0.1:3001/";
    public static String resource_server = "http://127.0.0.1:4001/";

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
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/token", null, null, params);
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
                "token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIiwic2NvcGUyIl0sImV4cCI6MTU4MjcwODk0MCwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiYjMyYjJjNmMtODE5NS00NTRkLTkwMDktOTljOGZlNDBjNDAxIiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.B4MJx52y1o2E9k5cG1MxlQjDRUvkEOBk0SvLg_hNg9M",
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/check_token", null, null, params);
        //{"aud":["resource1"],"user_name":"admin","scope":["scope1","scope2"],"active":true,"exp":1582703613,"authorities":["admin"],"jti":"b0901ed6-93c0-428c-8631-10bef8fbde63","client_id":"client1"}
    }

    //使用jwt token访问resource
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIiwic2NvcGUyIl0sImV4cCI6MTU4MjcxMjk0MSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiYzBiMTYzOGUtOThjNS00NTU2LTk0ZmMtYTgyNTE3NDhhYzM5IiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.l_j9s5XYz3NfrvI-Hky19V-P9vRz9U4Q1Ltkep5Up9Q");
        HttpUtil.send(HttpMethod.POST,resource_server + "/admin",head,null);
    }
}