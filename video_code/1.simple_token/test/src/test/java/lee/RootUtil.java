package lee;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RootUtil {

    public static Map<String, String> buildMap(String... args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            map.put(args[i], args[i + 1]);
        }
        return map;
    }

    public static JSONObject buildJson(String... args) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < args.length; i += 2) {
            jsonObject.put(args[i], args[i + 1]);
        }
        return jsonObject;
    }
}
