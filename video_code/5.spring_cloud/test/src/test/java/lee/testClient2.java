package lee;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class testClient2 {

    public static String zuul = "http://127.0.0.1:5001/";

    //授权码模式
    @Test
    public void getTokenByCode() throws IOException {
        //标准模式
        //浏览器访问
        //http://127.0.0.1:5001/auth/oauth/authorize?client_id=client2&response_type=code&scope=scope2&redirect_uri=http://www.sogou.com

        //重定向结果
        //https://www.baidu.com/?code=r9zqyd

        String[] params = new String[]{
                "client_id", "client2",
                "client_secret", "123123",
                "grant_type", "authorization_code",
                "code", "Qw83wz",//这个code是从getCode()方法获取的
                "redirect_uri", "http://www.sogou.com"
        };
        HttpUtil.send(HttpMethod.POST, zuul + "auth/oauth/token", null, null, params);
        //{"access_token":"415b010c-4891-41ef-90ce-1653ecd37658","token_type":"bearer","refresh_token":"02021d0e-34ed-42eb-8abc-f495b2dcd512","expires_in":133,"scope":"all"}
    }

    //校验token
    @Test
    public void checkToken() throws IOException {
        String[] params = new String[]{
                "token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUyIl0sImV4cCI6MTYxNDA5MTAzNSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiNTQwZWMxZDMtNGQ1OS00YjY3LWJjZTItYzM2NzI0MzRlNzk2IiwiY2xpZW50X2lkIjoiY2xpZW50MiJ9.LRtjPcaYykEjWAiYmZfLPeNUPKMddg8Q4jvhVxIKrzU",
        };
        HttpUtil.send(HttpMethod.POST, zuul + "auth/oauth/check_token", null, null, params);
        //{"aud":["resource1"],"user_name":"admin","scope":["all"],"active":true,"exp":1582620698,"authorities":["admin"],"client_id":"client1"}
    }

    //使用token访问resource
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUyIl0sImV4cCI6MTYxNDA5MTAzNSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiNTQwZWMxZDMtNGQ1OS00YjY3LWJjZTItYzM2NzI0MzRlNzk2IiwiY2xpZW50X2lkIjoiY2xpZW50MiJ9.LRtjPcaYykEjWAiYmZfLPeNUPKMddg8Q4jvhVxIKrzU");
        HttpUtil.send(HttpMethod.POST, zuul + "source2/me", head, null);
    }
}