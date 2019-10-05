package israiloff.mongodb.criteriabuilder.service.CustomTypeConverterService;

import org.springframework.stereotype.Service;

/**
 * Интерфейс для реализации кастомных конвертеров сложных типов. (Пр.: класс InstantTypeConverterServiceImpl)
 * Имеется ограничение для имени сервиса: оно должно быть в нижнем регистре и таким же, как и тип, в который конвертируются сторчные данные.
 * Пр. имен сервисов:
 * String to Instant: instant;
 * String to UserCustomConverterService: usercustomconverterservice;
 * Лайфхак: копируем имя класса в графу "name" в @Service("name") -> выделяем имя (name)-> нажимаем ctrl+shift+u -> уже нижний регистр, готово! - изи=))
 */
@Service
public interface CustomTypeConverterService {
    Object stringToObjectConverter(String value);
}
