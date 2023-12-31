package com.example.jwtauthorisedlogin.service;

import com.example.jwtauthorisedlogin.Entity.OrderHistory;
import com.example.jwtauthorisedlogin.Entity.QRCode;
import com.example.jwtauthorisedlogin.payload.request.QRCodeRequest;
import com.example.jwtauthorisedlogin.repository.OrderHistoryRepository;
import com.example.jwtauthorisedlogin.repository.QRCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@AllArgsConstructor
public class QRCodeService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int RANDOM_STRING_LENGTH = 25;
    private final QRCodeRepository qrCodeRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    public String generateQRCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        QRCode existingQRCode = qrCodeRepository.findByEmail(email);
        if(existingQRCode != null){
            StringBuilder randomStringBuilder = new StringBuilder();
            SecureRandom secureRandom = new SecureRandom();

            for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
                int randomIndex = secureRandom.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                randomStringBuilder.append(randomChar);
            }
            existingQRCode.setQrCodeString(randomStringBuilder.toString());
            qrCodeRepository.save(existingQRCode);

            return existingQRCode.getQrCodeString();
        }
        else {
            StringBuilder randomStringBuilder = new StringBuilder();
            SecureRandom secureRandom = new SecureRandom();

            for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
                int randomIndex = secureRandom.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                randomStringBuilder.append(randomChar);
            }
            String qrCodeString = randomStringBuilder.toString();

            QRCode qrCode = new QRCode();
            qrCode.setEmail(email);
            qrCode.setQrCodeString(qrCodeString);
            qrCodeRepository.save(qrCode);

            return qrCodeString;

        }

    }

        public boolean isValidQRCode(QRCodeRequest qrCodeRequest) {
            QRCode qrCode = qrCodeRepository.findByQrCodeString(qrCodeRequest.getQrCodeString());
            if (qrCode == null) {
                return false;
            }
            qrCodeRepository.delete(qrCode);

            List<OrderHistory> orderList=orderHistoryRepository.findAllByCurrentOrder(true);
            for(OrderHistory orderHistory: orderList){
                orderHistory.setCurrentOrder(false);
                orderHistoryRepository.save(orderHistory);
            }

            return true;
        }
}
