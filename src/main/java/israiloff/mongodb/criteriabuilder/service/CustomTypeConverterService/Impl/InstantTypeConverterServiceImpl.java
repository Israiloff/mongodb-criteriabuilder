package israiloff.mongodb.criteriabuilder.service.CustomTypeConverterService.Impl;

import israiloff.mongodb.criteriabuilder.service.CustomTypeConverterService.CustomTypeConverterService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service(InstantTypeConverterServiceImpl.NAME)
public class InstantTypeConverterServiceImpl implements CustomTypeConverterService {
    static final String NAME = "instant";

    @Override
    public Object stringToObjectConverter(String value) {
        return Instant.parse(value);
    }
}