package com.ys.hr.controller;

import com.cmb.xft.open.api.BaseReqInf;
import com.cmb.xft.open.api.HttpResponseData;
import com.cmb.xft.open.api.Sm4Util;
import com.cmb.xft.open.api.XftOpenApiReqClient;
import com.google.gson.Gson;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/xft")
@RestController
public class XftController extends BaseController {

    // @Value("${xft.AppId}")
    private final String appId = "89341d81-b58f-49b2-bf42-5eb5758a7d0e";

    // @Value("${xft.AuthoritySecret}")
    private final String authoritySecret = "00a1d875c6baefefaf99de52a3952a268262287cd7ad62a7ec8ef7272491913220";

    // @Value("${xft.url}")
    private final String url = "https://api.cmburl.cn:8065/apm/apmuat/xft-agentprotocol/agent-protocol/query";

    /**
     *agent protocol query
     * @return
     */
    @PostMapping("/agentProtocolQuery")
    public AjaxResult agentProtocolQuery(@RequestBody Map<String, Object> queryParam) {
        String key = authoritySecret.substring(0, 32);
        BaseReqInf baseReqInf = new BaseReqInf(appId, authoritySecret);
        baseReqInf.setUsrNbr("A0001");
        baseReqInf.setUsrUid("AUTO0001");
        String requestBody =  "{}";
        String secretMsg = null;
        try {
            secretMsg = Sm4Util.encryptEcb(key, requestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, String> newRequestBodyMap = new HashMap<>();
        newRequestBodyMap.put("secretMsg", secretMsg);
        Gson gson = new Gson();
        requestBody = gson.toJson(newRequestBodyMap);
        String queryString = "{}";
        Gson gs = new Gson();

        HttpResponseData postReqResult = null;
        try {
            postReqResult = XftOpenApiReqClient.doCommonPostReq(baseReqInf, url, queryParam,requestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String responseBody = postReqResult.getBody();
        try {
            responseBody = Sm4Util.decryptEcb(key, responseBody);
            System.out.println(responseBody);
        } catch (Exception e) {
            System.out.println("解密失败：" + responseBody);
        }
        return AjaxResult.success();
    }

    /**
     * Query of agency agreement
     * 
     * @return
     */
     @PostMapping("/agencyQuery")
     public AjaxResult availableQuery(@RequestBody Map<String, Object> queryParam) {
     /* 1. Basic information content */
     /* XFT tenant number */
     String companyId = "213";
     /* Open platform appId */
     String appId = "123";
     /* Open platform authoritySecret */
     String authoritySecret = "123";
     /* Key for encrypting and decrypting ciphertext */
     String key = authoritySecret.substring(0, 32);
     /* 2. Public information construction */
     BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
     baseReqInf.setUsrNbr("A0001");
     baseReqInf.setUsrUid("AUTO0001");
     /* 3. Request message, queryParam construction: String format (recommended
     way) */
     String requestBody = "{}";
     String secretMsg = null;
     try {
     secretMsg = Sm4Util.encryptEcb(key, requestBody);
     } catch (Exception e) {
     throw new RuntimeException(e);
     }
     Map<String, String> newRequestBodyMap = new HashMap<>();
     // The name secretMsg is fixed
     newRequestBodyMap.put("secretMsg", secretMsg);
     Gson gson = new Gson();
     requestBody = gson.toJson(newRequestBodyMap);
     /* Other parameters in the url besides public parameters (CSCAPPUID,
     CSCPRJCOD, CSCUSRUID, CSCREQTIM, CSCUSRNBR) */
     String queryString = "{}";
     Gson gs = new Gson();
     /* 4. Invoke the interface */
     HttpResponseData postReqResult = null;
     try {
     postReqResult = XftOpenApiReqClient.doCommonPostReq(baseReqInf, url +
     "/EAIUSRST", queryParam, requestBody);
     } catch (Exception e) {
     throw new RuntimeException(e);
     }
     String responseBody = postReqResult.getBody();
     try {
     responseBody = Sm4Util.decryptEcb(key, responseBody);
     System.out.println(responseBody);
     } catch (Exception e) {
     System.out.println("Decryption failed: " + responseBody);
     }
     return AjaxResult.success();
     }

    /**
     * Create a drop shipping batch
     * 
     * @return
     */
    @PostMapping("/eaclacrt")
    public AjaxResult eaclacrt(@RequestBody Map<String, Object> queryParam) {
        /* 1. Basic information content */
        /* Xinfutong tenant number */
        String companyId = "213";
        /* Open platform appId */
        /* Open platform authoritySecret */
        /* Key for encrypting and decrypting ciphertext */
        String key = authoritySecret.substring(0, 32);
        /* Request path (taking the test environment as an example) */
        /* 2. Public information construction */
        BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
        baseReqInf.setUsrNbr("A0001");
        baseReqInf.setUsrUid("AUTO0001");
        /*
         * 3. Request message and queryParam construction: in string form (recommended
         * way)
         */
        String requestBody = "{}";
        String secretMsg = null;
        try {
            secretMsg = Sm4Util.encryptEcb(key, requestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, String> newRequestBodyMap = new HashMap<>();
        // The name secretMsg is fixed
        newRequestBodyMap.put("secretMsg", secretMsg);
        Gson gson = new Gson();
        requestBody = gson.toJson(newRequestBodyMap);
        /*
         * Other parameters in the URL except for public parameters (CSCAPPUID,
         * CSCPRJCOD, CSCUSRUID, CSCREQTIM, CSCUSRNBR)
         */
        String queryString = "{}";
        Gson gs = new Gson();
        /* 4. Invoke the interface */
        try {
            HttpResponseData postReqResult = XftOpenApiReqClient.doCommonPostReq(baseReqInf, url + "/EACLACRT",
                    queryParam, requestBody);
            String responseBody = postReqResult.getBody();
            responseBody = Sm4Util.decryptEcb(key, responseBody);
            System.out.println(responseBody);
        } catch (Exception e) {
        }
        return AjaxResult.success();
    }

    /**
     * Batch execution of payment
     * 
     * @return
     */
    @PostMapping("/eaclaexe")
    public AjaxResult eaclaexe(@RequestBody Map<String, Object> queryParam) {
        /* 1. Basic information content */
        /* Xinfutong tenant number */
        String companyId = "213";
        /* Key for encrypting and decrypting ciphertext */
        String key = authoritySecret.substring(0, 32);
        /* Request path (taking the test environment as an example) */
        String url = "https://api.cmbchina.com/xft/apm/apm/";
        /* 2. Public information construction */
        BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
        baseReqInf.setUsrNbr("A0001");
        baseReqInf.setUsrUid("AUTO0001");
        /*
         * 3. Request message and queryParam construction: in string form (recommended
         * way)
         */
        String requestBody = "{}";
        String secretMsg = null;
        try {
            secretMsg = Sm4Util.encryptEcb(key, requestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, String> newRequestBodyMap = new HashMap<>();
        // The name secretMsg is fixed
        newRequestBodyMap.put("secretMsg", secretMsg);
        Gson gson = new Gson();
        requestBody = gson.toJson(newRequestBodyMap);
        /*
         * Other parameters in the URL except for public parameters (CSCAPPUID,
         * CSCPRJCOD, CSCUSRUID, CSCREQTIM, CSCUSRNBR)
         */
        String queryString = "{}";
        Gson gs = new Gson();
        /* 4. Invoke the interface */
        try {
            HttpResponseData postReqResult = XftOpenApiReqClient.doCommonPostReq(baseReqInf, url + "/EACLAEXE",
                    queryParam, requestBody);
            String responseBody = postReqResult.getBody();
            responseBody = Sm4Util.decryptEcb(key, responseBody);
            System.out.println(responseBody);
        } catch (Exception e) {
        }
        return AjaxResult.success();
    }

    /**
     * Query of the summary of the agency
     * 
     * @return
     */
    @PostMapping("/EACLATAL")
    public AjaxResult EACLATAL(@RequestBody Map<String, Object> queryParam) {
        /* 1. Basic information content */
        /* Xinfutong tenant number */
        String companyId = "213";
        /* Key for encrypting and decrypting ciphertext */
        String key = authoritySecret.substring(0, 32);
        /* 2. Public information construction */
        BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
        baseReqInf.setUsrNbr("A0001");
        baseReqInf.setUsrUid("AUTO0001");
        /*
         * 3. Request message and queryParam construction: in string form (recommended
         * way)
         */
        String requestBody = "{}";
        String secretMsg = null;
        try {
            secretMsg = Sm4Util.encryptEcb(key, requestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, String> newRequestBodyMap = new HashMap<>();
        // The name secretMsg is fixed
        newRequestBodyMap.put("secretMsg", secretMsg);
        Gson gson = new Gson();
        requestBody = gson.toJson(newRequestBodyMap);
        /*
         * Other parameters in the URL except for public parameters (CSCAPPUID,
         * CSCPRJCOD, CSCUSRUID, CSCREQTIM, CSCUSRNBR)
         */
        String queryString = "{}";
        Gson gs = new Gson();
        try {
            /* 4. Invoke the interface */
            HttpResponseData postReqResult = XftOpenApiReqClient.doCommonPostReq(baseReqInf, url + "/EACLATAL",
                    queryParam, requestBody);
            String responseBody = postReqResult.getBody();
            responseBody = Sm4Util.decryptEcb(key, responseBody);
            System.out.println(responseBody);
        } catch (Exception e) {
        }
        return AjaxResult.success();
    }

    /**
     * Invention details query
     * 
     * @return
     */
    @PostMapping("/eacladtl")
    public AjaxResult eacladtl(@RequestBody Map<String, Object> queryParam) {
        /* 1. Basic information content */
        /* Xinfutong tenant number */
        String companyId = "213";
        /* Key for encrypting and decrypting ciphertext */
        String key = authoritySecret.substring(0, 32);
        /* 2. Public information construction */
        BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
        baseReqInf.setUsrNbr("A0001");
        baseReqInf.setUsrUid("AUTO0001");
        /*
         * 3. Request message and queryParam construction: in string form (recommended
         * way)
         */
        String requestBody = "{}";
        String secretMsg = null;
        try {
            secretMsg = Sm4Util.encryptEcb(key, requestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, String> newRequestBodyMap = new HashMap<>();
        // The name secretMsg is fixed
        newRequestBodyMap.put("secretMsg", secretMsg);
        Gson gson = new Gson();
        requestBody = gson.toJson(newRequestBodyMap);
        /*
         * Other parameters in the URL except for public parameters (CSCAPPUID,
         * CSCPRJCOD, CSCUSRUID, CSCREQTIM, CSCUSRNBR)
         */
        String queryString = "{}";
        /* 4. Invoke the interface */
        try {
            HttpResponseData postReqResult = XftOpenApiReqClient.doCommonPostReq(baseReqInf, url + "/EACLADTL",
                    queryParam, requestBody);
            String responseBody = postReqResult.getBody();
            responseBody = Sm4Util.decryptEcb(key, responseBody);
            System.out.println(responseBody);
        } catch (Exception e) {
        }
        return AjaxResult.success();
    }

    /**
     * Cancellation of agency payment
     * 
     * @return
     */
    @PostMapping("/eaipaycl")
    public AjaxResult eaipaycl(@RequestBody Map<String, Object> queryParam) {
        /* 1. Basic information content */
        /* Xinfutong tenant number */
        String companyId = "213";
        /* Key for encrypting and decrypting ciphertext */
        String key = authoritySecret.substring(0, 32);
        /* 2. Public information construction */
        BaseReqInf baseReqInf = new BaseReqInf(companyId, appId, authoritySecret);
        baseReqInf.setUsrNbr("A0001");
        baseReqInf.setUsrUid("AUTO0001");
        /*
         * 3. Request message and queryParam construction: in string form (recommended
         * way)
         */
        String requestBody = "{}";
        String secretMsg = null;
        try {
            secretMsg = Sm4Util.encryptEcb(key, requestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, String> newRequestBodyMap = new HashMap<>();
        // The name secretMsg is fixed
        newRequestBodyMap.put("secretMsg", secretMsg);
        Gson gson = new Gson();
        requestBody = gson.toJson(newRequestBodyMap);

        try {
            /* 4. Invoke the interface */
            HttpResponseData postReqResult = XftOpenApiReqClient.doCommonPostReq(baseReqInf, url + "/EAIPAYCL",
                    queryParam, requestBody);
            String responseBody = postReqResult.getBody();
            responseBody = Sm4Util.decryptEcb(key, responseBody);
            System.out.println(responseBody);
        } catch (Exception e) {
        }
        return AjaxResult.success();
    }

}
