package com.ys.hr.controller;

import com.ys.hr.domain.ABAConfig;
import com.ys.hr.domain.ABAFileRequest;
import com.ys.hr.domain.Transaction;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aba")
public class ABAController {

        @PostMapping("/generate")
        public ResponseEntity<Resource> generateABAFile(
                        @Valid @RequestBody ABAFileRequest request) {

                // 1. Convert configuration
                ABAConfig config = ABAConfig.fromRequest(request);

                // 2. Prepare transaction records
                List<Transaction> transactions = request.getList().stream()
                                .map(emp -> {
                                        Transaction tx = new Transaction();
                                        // Use employee-specific BSB or default BSB
                                        tx.setBsb(emp.getBsb() != null ? emp.getBsb().replace("-", "")
                                                        : request.getBsb().replace("-", ""));
                                        tx.setAccountNumber(emp.getBankNumber());
                                        tx.setBeneficiaryName(emp.getFullName());
                                        tx.setAmount(emp.getAfterTaxSalary());
                                        tx.setLodgementReference(request.getDefaultText());
                                        return tx;
                                })
                                .collect(Collectors.toList());

                // 3. Generate ABA file content
                byte[] abaContent = generateABAContent(config, transactions);

                // 4. Create file resource
                ByteArrayResource resource = new ByteArrayResource(abaContent);

                // 5. Set response headers
                String filename = "payroll-" + LocalDate.now() + ".aba";
                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .contentLength(abaContent.length)
                                .body(resource);
        }

        private byte[] generateABAContent(ABAConfig config, List<Transaction> transactions) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.US_ASCII)) {

                        // Generate descriptive record
                        writer.write(generateDescriptiveRecord(config));
                        writer.write("\r\n"); // ABA files use CRLF line endings

                        // Generate detail records
                        for (Transaction t : transactions) {
                                writer.write(generateDetailRecord(config, t));
                                writer.write("\r\n");
                        }

                        // Generate total record
                        writer.write(generateTotalRecord(transactions));
                        writer.write("\r\n");

                        writer.flush();
                        return baos.toByteArray();
                } catch (Exception e) {
                        throw new RuntimeException("Failed to generate ABA file", e);
                }
        }

        private String generateDescriptiveRecord(ABAConfig config) {

                // If apcaNumber is empty, fill with spaces
                String apcaNumber = config.getApcaNumber() != null
                                ? String.format("%06d", Integer.parseInt(config.getApcaNumber()))
                                : "      "; // 6 spaces

                return String.format(
                                "0%-17s" + // Positions 2-18: 17 spaces
                                                "01" + // Positions 19-20: Volume serial number (fixed 01)
                                                "%-3s" + // Positions 21-23: Financial institution abbreviation (3
                                                         // characters)
                                                "%-7s" + // Positions 24-30: 7 spaces
                                                "%-26s" + // Positions 31-56: Account name (26 characters, left-aligned)
                                                // "%06d" + // Positions 57-62: APCA number (6 digits, right-aligned
                                                // zero-padded)
                                                "%s" + // Positions 57-62: APCA number (6 digits or spaces,
                                                       // right-aligned)
                                                "%-12s" + // Positions 63-74: File description (12 characters,
                                                          // left-aligned)
                                                "%s" + // Positions 75-80: Processing date (DDMMYY)
                                                "%-40s", // Positions 81-120: 40 spaces
                                "",
                                config.getBankAbbreviation(), // New bank abbreviation field required
                                "",
                                padRight(config.getAccountName(), 26),
                                // Integer.parseInt(config.getApcaNumber()),
                                apcaNumber,
                                padRight(config.getFileDescription(), 12),
                                config.getProcessingDate(),
                                "").substring(0, 120); // Ensure total length is 120 characters
        }

        private String generateDetailRecord(ABAConfig config, Transaction t) {
                // Format BSB (XXX-XXX)
                String formattedBSB = t.getBsb().substring(0, 3) + "-" + t.getBsb().substring(3);

                // Convert amount to cents (10 digits right-aligned zero-padded)
                String amount = String.format("%010d",
                                t.getAmount().multiply(BigDecimal.valueOf(100)).intValue());

                // Trace record BSB (payer's BSB)
                String traceBSB = config.getRemitterBSB().substring(0, 3) +
                                "-" +
                                config.getRemitterBSB().substring(3);

                return String.format(
                                "1" + // Record type
                                                "%-7s" + // Positions 2-8: BSB
                                                "%-9s" + // Positions 9-17: Account number (right-aligned space-padded)
                                                " " + // Position 18: Indicator (space)
                                                "53" + // Positions 19-20: Transaction code
                                                "%s" + // Positions 21-30: Amount (10 digits)
                                                "%-32s" + // Positions 31-62: Beneficiary name
                                                "%-18s" + // Positions 63-80: Lodgement reference
                                                "%-7s" + // Positions 81-87: Trace BSB
                                                "%-9s" + // Positions 88-96: Trace account number (right-aligned
                                                         // space-padded)
                                                "%-16s" + // Positions 97-112: Remitter name
                                                "%-8s", // Positions 113-120: Withholding tax (8 zeros)
                                formattedBSB,
                                padLeft(t.getAccountNumber(), 9), // Account number right-aligned
                                amount,
                                padRight(t.getBeneficiaryName(), 32),
                                padRight(t.getLodgementReference(), 18),
                                traceBSB,
                                padLeft(config.getRemitterAccount(), 9), // Account number right-aligned
                                padRight(config.getRemitterName(), 16),
                                "00000000").substring(0, 120); // Ensure total length is 120 characters
        }

        private String generateTotalRecord(List<Transaction> transactions) {
                long totalCents = transactions.stream()
                                .mapToLong(t -> t.getAmount()
                                                .multiply(BigDecimal.valueOf(100))
                                                .longValue())
                                .sum();

                String totalAmount = String.format("%010d", totalCents);
                int detailRecordCount = transactions.size();

                return String.format(
                                "7" + // Record type
                                                "999-999" + // Positions 2-8: BSB filler
                                                "%-12s" + // Positions 9-20: 12 spaces
                                                "%s" + // Positions 21-30: Net total
                                                "%s" + // Positions 31-40: Total credits
                                                "0000000000" + // Positions 41-50: Total debits (0)
                                                "%-24s" + // Positions 51-74: 24 spaces
                                                "%06d" + // Positions 75-80: Record count
                                                "%-40s", // Positions 81-120: 40 spaces
                                "",
                                totalAmount,
                                totalAmount,
                                "",
                                detailRecordCount,
                                "").substring(0, 120);
        }

        private String truncate(String value, int maxLength) {
                if (value == null)
                        return "";
                return value.length() > maxLength ? value.substring(0, maxLength) : value;
        }

        private String padLeft(String value, int length) {
                if (value == null)
                        value = "";
                return String.format("%" + length + "s", value);
        }

        // Helper method: left-align with space padding
        private String padRight(String value, int length) {
                if (value == null)
                        value = "";
                return String.format("%-" + length + "s", value);
        }
}
