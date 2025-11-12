package com.ys.hr.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.ys.hr.exception.ABAValidationException;
import com.ys.hr.service.CSVProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/hraba")
public class HrAbaConversionController {

    @Autowired
    private CSVProcessingService csvService;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertCsvToAba(
            @RequestParam("file") MultipartFile csvFile,
            @RequestParam("senderName") String senderName,
            @RequestParam("senderAccount") String senderAccount,
            @RequestParam("senderBsb") String senderBsb,
            @RequestParam("senderBank") String senderBank,
            @RequestParam("acpaNumber") String acpaNumber,
            @RequestParam(value = "strictMode", defaultValue = "false") boolean strictMode,
            @RequestParam(value = "batchDescription", required = false) String batchDescription) {

        try {
            validateRequiredFields(senderName, senderAccount, senderBsb, senderBank, acpaNumber);
            String fileName = StringUtils.cleanPath(csvFile.getOriginalFilename());

            try (InputStream inputStream = csvFile.getInputStream()) {
                String abaContent = csvService.convertCsvToAba(
                        inputStream,
                        senderName,
                        senderAccount,
                        senderBsb,
                        senderBank,
                        acpaNumber,
                        strictMode,
                        batchDescription
                );

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + fileName + ".aba\"")
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(abaContent.getBytes());
            }
        } catch (ABAValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (IOException | CsvValidationException e) {
            return ResponseEntity.internalServerError().body(("File processing error: " + e.getMessage()).getBytes());
        }
    }

    private void validateRequiredFields(String senderName, String senderAccount, String senderBsb,
                                        String senderBank, String acpaNumber) {
        if (StringUtils.isEmpty(senderName) ||
                StringUtils.isEmpty(senderAccount) ||
                StringUtils.isEmpty(senderBsb) ||
                StringUtils.isEmpty(senderBank) ||
                StringUtils.isEmpty(acpaNumber)) {
            throw new ABAValidationException("Missing required fields: senderName, senderAccount, senderBsb, senderBank, acpaNumber");
        }
    }
}
