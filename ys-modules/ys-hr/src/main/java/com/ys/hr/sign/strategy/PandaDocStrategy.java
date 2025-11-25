package com.ys.hr.sign.strategy;

import com.alibaba.fastjson.JSONObject;
import com.pandadoc.client.ApiClient;
import com.pandadoc.client.Configuration;
import com.pandadoc.client.api.DocumentsApi;
import com.pandadoc.client.auth.ApiKeyAuth;
import com.pandadoc.client.models.*;
import com.ys.common.core.utils.file.FileConversionUtil;
import com.ys.hr.sign.OssUtils;
import com.ys.hr.sign.entiry.ResponseEntity;
import com.ys.hr.sign.entiry.SignVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PandaDoc Platform Access Implementation
 */
@Component
@Qualifier("PANDA_DOC")
@Slf4j(topic = "eSign_info")
public class PandaDocStrategy implements SignApiStrategy<SignVo, JSONObject> {

    public PandaDocStrategy(){}

    public static PandaDocStrategy pandaDocStrategy;

    @PostConstruct
    public void init(){
        pandaDocStrategy = this;
    }

    @Resource
    private OssUtils ossUtils;


    /**
     * SendElectronic Signature
     * @param params
     * @return
     */
    @Override
    public ResponseEntity<JSONObject> send(SignVo params) {
        JSONObject config = params.getConfig();
        ResponseEntity<JSONObject> responseEntity = null;
        if (ObjectUtils.isEmpty(config)) {
            responseEntity = new ResponseEntity<>(500, null, "Configuration information does not exist!");
        }
        if (ObjectUtils.isEmpty(config.get("apiKey"))) {
            responseEntity = new ResponseEntity<>(500, null, "apiKey information does not exist!");
        }
        if (StringUtils.isEmpty(params.getUrl())){
            responseEntity = new ResponseEntity<>(500, null, "Api Url does not exist!");
        }
        ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath(params.getUrl());
        ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("apiKey");
        auth.setApiKey(params.getConfig().getString("apiKey"));
        auth.setApiKeyPrefix("API-Key");
        DocumentsApi docsApi = new DocumentsApi(client);
        try {
            List<DocumentCreateRequestRecipients> recipients = new ArrayList<>();
            DocumentCreateRequestRecipients signer1 = new DocumentCreateRequestRecipients();
            signer1.setEmail(params.getEmployeeContract().getCompanyEmail());
            signer1.setFirstName(params.getEmployeeContract().getCompanyName());
            signer1.setRole("partyA");
            recipients.add(signer1);
            DocumentCreateRequestRecipients signer2 = new DocumentCreateRequestRecipients();
            signer2.setEmail(params.getEmployeeContract().getEmail());
            signer2.setFirstName(params.getEmployeeContract().getName());
            signer2.setRole("partyB");
            recipients.add(signer2);
            // 2. Build and Create Document Request
            DocumentCreateRequest createReq = new DocumentCreateRequest()
                    .name(params.getTemplateName() + System.currentTimeMillis())
                    .parseFormFields(false)  // Use custom tag fields and do not parse the native forms of PDF: contentReference[oaicite:1]{index=1}
                    .recipients(recipients);

            // 3. Define the mapping of signature Name and date fields.
            // 4. Set up the mapping of fields.
            Map<String, Map<String, Object>> fields = new HashMap<>();

            Map<String, Object> fA1 = new HashMap<>();
            fA1.put("role", "partyA");
            fields.put("partyA_sig", fA1);

            Map<String, Object> fA2 = new HashMap<>();
            fA2.put("role", "partyA");
            fields.put("partyA_date", fA2);

            Map<String, Object> fB1 = new HashMap<>();
            fB1.put("role", "partyB");
            fields.put("partyB_sig", fB1);

            Map<String, Object> fB2 = new HashMap<>();
            fB2.put("role", "partyB");
            fields.put("partyB_date", fB2);
            createReq.setFields(fields);

            String s = uploadFile(params.getContent());
            createReq.setUrl(s);

            String documentId = docsApi.createDocument(/*file=*/createReq, null).getId();
            log.info("Initiate contract creation ---> Enterprise Email:{}, Staff Email:{}, Contract Number:{}", params.getEmployeeContract().getEmail(), params.getEmployeeContract().getCompanyEmail(), documentId);
            int requestCount = 0;
            while (true){
                if (requestCount >= 10){
                    responseEntity = new ResponseEntity<>(500, null, "Status query abnormality!");
                    return responseEntity;
                }
                String status = docsApi.statusDocument(documentId).getStatus();
                log.info("Status Query ---> Contract Number:{}, Status:{}", documentId, status);
                Thread.sleep(2000);
                if ("document.draft".equals(status)){
                    break;
                }
                requestCount++;
            }
            DocumentSendRequest sendReq = new DocumentSendRequest()
                .silent(false)
                .subject("Electronic contract signing")
                .message(params.getTemplateName());
            DocumentSendResponse documentSendResponse = docsApi.sendDocument(documentId, sendReq);
            String status = documentSendResponse.getStatus();
            log.info("Send electronic signature ---> Contract Number:{}, Status:{}", documentId, status);
            JSONObject data = new JSONObject();
            data.put("contractNumber", documentId);
            responseEntity = new ResponseEntity<>(200, data, null);
        }catch (Exception e) {
            responseEntity = new ResponseEntity<>(500, null, "Electronic signature is abnormal, please try again");
            e.printStackTrace();
        }
        return responseEntity;
    }

    /**
     * QUERYElectronic Signature Status
     * @param params
     * @return
     */
    @Override
    public ResponseEntity<JSONObject> getSignDetail(SignVo params) {
        JSONObject config = params.getConfig();
        ResponseEntity<JSONObject> responseEntity = null;
        if (ObjectUtils.isEmpty(config)) {
            responseEntity = new ResponseEntity<>(500, null, "Configuration information does not exist!");
        }
        if (ObjectUtils.isEmpty(config.get("apiKey"))) {
            responseEntity = new ResponseEntity<>(500, null, "apiKey information does not exist!");
        }
        if (StringUtils.isEmpty(params.getUrl())){
            responseEntity = new ResponseEntity<>(500, null, "Api Url does not exist!");
        }
        ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath(params.getUrl());
        ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("apiKey");
        auth.setApiKey(params.getConfig().getString("apiKey"));
        auth.setApiKeyPrefix("API-Key");
        DocumentsApi docsApi = new DocumentsApi(client);
        try {
            DocumentDetailsResponse documentDetailsResponse = docsApi.detailsDocument(params.getEmployeeContract().getFileId());
            log.info("Electronic signature information inquiry ---> Contract Number:{}, Info:{}", params.getEmployeeContract().getFileId(), documentDetailsResponse);
            List<DocumentDetailsResponseRecipients> recipients = documentDetailsResponse.getRecipients();
            JSONObject data = new JSONObject();
            data.put("contractNumber", params.getEmployeeContract().getFileId());
            data.put("companyEmail", recipients.get(0).getEmail());
            data.put("companyUrl", recipients.get(0).getSharedLink());
            data.put("enterpriseSign", recipients.get(0).getHasCompleted() ? "1" : "0");
            data.put("email", recipients.get(1).getEmail());
            data.put("userSign", recipients.get(1).getHasCompleted() ? "1" : "0");
            data.put("userUrl", recipients.get(1).getSharedLink());
            responseEntity = new ResponseEntity<>(200, data, null);
        }catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(500, null, "Electronic signature information query is abnormal");
        }
        return responseEntity;
    }

    /**
     * File Upload
     * @param content
     * @return
     */
    private String uploadFile(String content){
        String cleanHtml = Jsoup.parse(content)
                .outputSettings(new Document.OutputSettings()
                        .syntax(Document.OutputSettings.Syntax.xml)
                        .escapeMode(Entities.EscapeMode.xhtml))
                .body()
                .html();
        File file = FileConversionUtil.convertHtmlToPdf(cleanHtml);
        String s = pandaDocStrategy.ossUtils.uploadFile(file, "sign/pdf/");
        // Delete Temporary File
        if (file.exists()){
            file.delete();
        }
        return s;
    }
}
