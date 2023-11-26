package com.example.jwtauthorisedlogin.controller;

import com.example.jwtauthorisedlogin.payload.request.QRCodeRequest;
import com.example.jwtauthorisedlogin.service.QRCodeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qrCode")
@AllArgsConstructor
public class QRCodeController {
    private final QRCodeService qrCodeService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateQRCode() {
        String qrCodeString = qrCodeService.generateQRCode();
        return ResponseEntity.ok(qrCodeString);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateQRCode(@RequestBody QRCodeRequest qrCodeRequest) {
        boolean isValid = qrCodeService.isValidQRCode(qrCodeRequest);
        if (isValid) {
            return ResponseEntity.ok("QR Code is valid");
        } else {
            return ResponseEntity.badRequest().body("QR Code is not valid");
        }
    }
}
