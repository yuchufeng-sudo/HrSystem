package com.ys.hr;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.ys.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class PayPalClient {
    public PayPalHttpClient client(String mode, String clientId, String clientSecret) {
        log.info("mode={}, clientId={}, clientSecret={}", mode, clientId, clientSecret);
        PayPalEnvironment environment = mode.equals("live")
                ? new PayPalEnvironment.Live(clientId, clientSecret)
                : new PayPalEnvironment.Sandbox(clientId, clientSecret);
        return new PayPalHttpClient(environment);
    }

    public String prettyPrint(JSONObject jo, String pre) {
        StringBuilder pretty = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = jo.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object value = entry.getValue();
            pretty.append(String.format("%s%s: ", pre, StringUtils.capitalize(key)));

            if (value instanceof JSONObject) {
                pretty.append(prettyPrint((JSONObject) value, pre + "\t"));
            } else if (value instanceof JSONArray) {
                int sno = 1;
                JSONArray array = (JSONArray) value;
                for (Object obj : array) {
                    pretty.append(String.format("\n%s\t%d:\n", pre, sno++));
                    if (obj instanceof JSONObject) {
                        pretty.append(prettyPrint((JSONObject) obj, pre + "\t\t"));
                    } else {
                        pretty.append(pre).append("\t\t").append(obj).append("\n");
                    }
                }
            } else {
                pretty.append(String.format("%s\n", value));
            }
        }
        return pretty.toString();
    }
}
