package dev._xdbe.booking.creelhouse.infrastructure.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CreditCardConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        // Step 7a: Encrypt the PAN before storing it in the database
        if (attribute == null) {
            return null;
        }
        return CryptographyHelper.encryptData(attribute);
        // Step 7a: End of PAN encryption
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        // Step 7b: Decrypt the PAN when reading it from the database
        if (dbData == null) {
            return null;
        }
        String pan = CryptographyHelper.decryptData(dbData);
        // Step 7b: End of PAN decryption
        String maskedPanString = panMasking(pan);
        return maskedPanString;
    }

    private String panMasking(String pan) {
        // Step 6:
        if (pan == null || pan.length() <= 8) {
            return pan;
        }

        String firstDigits = pan.substring(0, 4);
        String lastDigits = pan.substring(pan.length() - 4);
        String maskedDigits = "*".repeat(pan.length() - 8);

        return firstDigits + maskedDigits + lastDigits;
        // Step 6: End
    }

    
}
