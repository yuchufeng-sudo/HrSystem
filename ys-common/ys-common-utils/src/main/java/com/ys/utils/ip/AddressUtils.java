package com.ys.utils.ip;

import com.alibaba.fastjson.JSONObject;
import com.ys.common.core.constant.Constants;
import com.ys.common.core.utils.StringUtils;
import com.ys.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Obtain Address
 *
 * @author ys
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IPAddressQUERY
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // Address
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        String address = UNKNOWN;
        // 
        if (IpUtils.internalIp(ip))
        {
            return "Internal Network IP";
        }
        try
        {
            String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
            if (StringUtils.isEmpty(rspStr))
            {
                log.error("Exception occurred while obtaining geographical location {}", ip);
                return UNKNOWN;
            }
            JSONObject obj = JSONObject.parseObject(rspStr);
            String region = obj.getString("pro");
            String city = obj.getString("city");
            return String.format("%s %s", region, city);
        }
        catch (Exception e)
        {
            log.error("Exception occurred while obtaining geographical location {}", ip);
        }
        return address;
    }
}
