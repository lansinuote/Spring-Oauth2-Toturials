package lee;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HttpUtil {

    public static WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);

    public static Page send(HttpMethod httpMethod, String url, Map<String, String> heads, String body, String... params) throws IOException {
        WebRequest webRequest = new WebRequest(new URL(url), httpMethod);

        if (httpMethod.equals(HttpMethod.GET)) {
            url += buildGetParam(params);
        } else {
            webRequest.setRequestParameters(buildPostParam(params));
        }

        if (body != null) {
            webRequest.setRequestBody(body);
        }

        if (heads != null) {
            webRequest.setAdditionalHeaders(heads);
        }

        Page page = webClient.getPage(webRequest);
        System.out.println(page.getWebResponse().getContentAsString());
        return page;
    }

    private static List<NameValuePair> buildPostParam(String... params) {
        List<NameValuePair> pairs = new ArrayList<>();
        for (int i = 0; i < params.length; i += 2) {
            pairs.add(new NameValuePair(params[i], params[i + 1]));
        }
        return pairs;
    }

    private static String buildGetParam(String... params) {
        if (params == null || params.length == 0) {
            return "";
        }
        String param = "?";
        for (int i = 0; i < params.length; i += 2) {
            if (!param.equals("?")) {
                param += "&";
            }
            String key = params[i];
            String value = params[i + 1];
            if (key.equals("id") && value == null) {
                continue;
            }
            param += key + "=" + value;
        }
        return param;
    }
}