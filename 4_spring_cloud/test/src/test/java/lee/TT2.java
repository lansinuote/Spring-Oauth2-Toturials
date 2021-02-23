package lee;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class TT2 {

    //测试对资源服务2的访问

    public static String zuul = "http://127.0.0.1:5001/";

    //授权码模式
    @Test
    public void getTokenByCode() throws IOException {
        //标准模式
        //浏览器访问
        //http://127.0.0.1:5001/authorization_server/oauth/authorize?client_id=client2&response_type=code&scope=scope2&redirect_uri=http://www.sogou.com

        //重定向结果
        //https://www.sogou.com/?code=T65dRC

        String[] params = new String[]{
                "client_id", "client2",
                "client_secret", "123123",
                "grant_type", "authorization_code",
                "code", "T65dRC",//这个code是从getCode()方法获取的
                "redirect_uri", "http://www.sogou.com"
        };
        HttpUtil.send(HttpMethod.POST, zuul + "authorization_server/oauth/token", null, null, params);
        //{"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUyIl0sImV4cCI6MTYxNDA1OTk1NSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiZThlNzM5ODItZGRhYy00YmY5LWEwMjAtYWJhMWY0NWE1OTkzIiwiY2xpZW50X2lkIjoiY2xpZW50MiJ9.Z-E0U_JAAHc_F1K28q8Dzv0O4mBwh49NvXFlYMyes6M","token_type":"bearer","refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUyIl0sImF0aSI6ImU4ZTczOTgyLWRkYWMtNGJmOS1hMDIwLWFiYTFmNDVhNTk5MyIsImV4cCI6MTYxNDA2MTE1NSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiODM3YzUzNzYtYWM5Yi00Yjc5LTg4NzgtNzRhZDczODdjNTdiIiwiY2xpZW50X2lkIjoiY2xpZW50MiJ9.i1A5VmgEMnyPfilYsWhsJyjnbJALX3kBeEoyxBpO1d8","expires_in":299,"scope":"scope2","jti":"e8e73982-ddac-4bf9-a020-aba1f45a5993"}
    }

    //验证jwt token
    @Test
    public void checkToken() throws IOException {
        String[] params = new String[]{
                "token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUyIl0sImV4cCI6MTYxNDA1OTk1NSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiZThlNzM5ODItZGRhYy00YmY5LWEwMjAtYWJhMWY0NWE1OTkzIiwiY2xpZW50X2lkIjoiY2xpZW50MiJ9.Z-E0U_JAAHc_F1K28q8Dzv0O4mBwh49NvXFlYMyes6M",
        };
        HttpUtil.send(HttpMethod.POST, zuul + "authorization_server/oauth/check_token", null, null, params);
        //{"aud":["resource2"],"user_name":"admin","scope":["scope2"],"active":true,"exp":1614059955,"authorities":["admin"],"jti":"e8e73982-ddac-4bf9-a020-aba1f45a5993","client_id":"client2"}
    }

    //使用jwt token访问resource
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUyIl0sImV4cCI6MTYxNDA1OTk1NSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiZThlNzM5ODItZGRhYy00YmY5LWEwMjAtYWJhMWY0NWE1OTkzIiwiY2xpZW50X2lkIjoiY2xpZW50MiJ9.Z-E0U_JAAHc_F1K28q8Dzv0O4mBwh49NvXFlYMyes6M");
        HttpUtil.send(HttpMethod.POST, zuul + "resource_server2/me", head, null);
        //{"authorities":[{"authority":"admin"}],"details":{"remoteAddress":"127.0.0.1","sessionId":null,"tokenValue":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUyIl0sImV4cCI6MTYxNDA1OTk1NSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiZThlNzM5ODItZGRhYy00YmY5LWEwMjAtYWJhMWY0NWE1OTkzIiwiY2xpZW50X2lkIjoiY2xpZW50MiJ9.Z-E0U_JAAHc_F1K28q8Dzv0O4mBwh49NvXFlYMyes6M","tokenType":"Bearer","decodedDetails":null},"authenticated":true,"userAuthentication":{"authorities":[{"authority":"admin"}],"details":null,"authenticated":true,"principal":"admin","credentials":"N/A","name":"admin"},"principal":"admin","credentials":"","oauth2Request":{"clientId":"client2","scope":["scope2"],"requestParameters":{"client_id":"client2"},"resourceIds":["resource2"],"authorities":[],"approved":true,"refresh":false,"redirectUri":null,"responseTypes":[],"extensions":{},"refreshTokenRequest":null,"grantType":null},"clientOnly":false,"name":"admin"}
    }
}