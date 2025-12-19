package id.sajiin.sajiinservices.shared.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ActiveStatusConverter implements AttributeConverter<ActiveStatus, String> {
    @Override
    public String convertToDatabaseColumn(ActiveStatus activeStatus) {
        return activeStatus == null ? null : activeStatus.name().toLowerCase();
    }

    @Override
    public ActiveStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ActiveStatus.valueOf(dbData.toUpperCase());
    }
}
