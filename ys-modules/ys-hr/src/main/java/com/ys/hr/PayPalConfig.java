package com.ys.hr;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Component; 

import java.util.HashMap;
import java.util.Map;

/**
 * PayPal configuration class, managing the APIContext singleton.
 */
@Component 
class PayPalConfig {
    private static final String CLIENT_ID = "AWO79hd3gEJoFODR25LF_ZkFRsVlk_eEjRDYQAJamR50ERwGnH0zev72XVV1HJHRD-E6uRtceUHn7C0R";
    private static final String CLIENT_SECRET = "EHI-vPa74X8wnG4rbUIgL_TIQYeQkVd436e7JnPJjIvtBWvNEHRLcxTXyj-0oE17-z6gxZbPeFhaScEo";
    private static final String MODE = "sandbox"; 

    private APIContext apiContext;

    public PayPalConfig() {
        try {
            initApiContext();
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Initializing the PayPal API context failed.", e);
        }
    }

    private void initApiContext() throws PayPalRESTException {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", MODE);

        // Set HTTP connection timeout and retries.
        configMap.put("http.ConnectionTimeOut", "30000");
        configMap.put("http.Retry", "1");

        apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        apiContext.setConfigurationMap(configMap);
    }

    public APIContext getAPIContext() {
        return apiContext;
    }
}