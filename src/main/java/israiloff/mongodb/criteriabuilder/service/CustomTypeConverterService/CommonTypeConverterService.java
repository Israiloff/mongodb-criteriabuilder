package israiloff.mongodb.criteriabuilder.service.CustomTypeConverterService;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

@Service
public class CommonTypeConverterService {
    private static final Logger log = LoggerFactory.getLogger(CommonTypeConverterService.class);
    private ApplicationContext applicationContext;

    public CommonTypeConverterService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object typeConverter(String value, Class<?> targetType) {
        log.info("typeConverter started with value : {}; target  type : {}", value, targetType);
        if (targetType.equals(String.class)) {
            return value;
        }
        if (targetType.isEnum() || targetType.isPrimitive() || ClassUtils.wrapperToPrimitive(targetType) != null) {
            log.debug("primitive type converter started");
            return stringToPrimitiveTypeConverter(value, targetType);
        }
        log.debug("complex type converter started");
        CustomTypeConverterService converterService;
        try {
            converterService = applicationContext.getBean(targetType.getSimpleName().toLowerCase(), CustomTypeConverterService.class);
            log.debug("complex type converter is {}", converterService);
        } catch (Exception e) {
            return value;
        }

        return converterService.stringToObjectConverter(value);
    }

    private Object stringToPrimitiveTypeConverter(String value, Class<?> targetType) {
        PropertyEditor propertyEditor = PropertyEditorManager.findEditor(targetType);
        propertyEditor.setAsText(value);
        return propertyEditor.getValue();
    }
}
