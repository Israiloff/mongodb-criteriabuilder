package israiloff.mongodb.criteriabuilder.service.QueryBuilderService;

import israiloff.mongodb.criteriabuilder.service.CustomTypeConverterService.CommonTypeConverterService;
import israiloff.mongodb.criteriabuilder.service.exception.ParamsIncorrectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryBuilderService<T> {
    private static final Logger log = LoggerFactory.getLogger(QueryBuilderService.class);
    private Class<T> type;
    private CommonTypeConverterService typeConverterService;

    public QueryBuilderService(Class<T> type, CommonTypeConverterService typeConverterService) {
        this.type = type;
        this.typeConverterService = typeConverterService;
    }

    public Query criteriaQueryBuilder(Map<String, List<String>> criteriaParams, Pageable pageable) {
        log.info("criteriaQueryBuilder started with param : {}", criteriaParams);
        if (criteriaParams == null || criteriaParams.size() == 0) {
            throw new ParamsIncorrectException();
        }

        List<Criteria> criteriaList = new ArrayList<>();
        for (Map.Entry<String, List<String>> member : criteriaParams.entrySet()) {
            log.debug("Adding next member of <key, value> collection into the query : {}", member);
            addCriteria(criteriaList, member);
        }

        Query query = criteriaList.size() > 1
                ? new Query().addCriteria(criteriaList.remove(0).andOperator(criteriaList.toArray(new Criteria[0])))
                : new Query().addCriteria(criteriaList.remove(0));
        return pageable == null ? query : query.with(pageable);
    }

    private void addCriteria(List<Criteria> criteriaList, Map.Entry<String, List<String>> member) {
        try {
            if (member.getValue().size() > 1) {
                betweenCriteriaPartConstructor(criteriaList, member);
            } else {
                oneArgumentCriteriaPartConstructor(criteriaList, member);
            }
        } catch (Exception e) {
            log.error("Error occurred while parsing fields of : {} | Error argument : {}", type.getTypeName(), e);
        }
    }

    private void betweenCriteriaPartConstructor(List<Criteria> criteriaList, Map.Entry<String, List<String>> member) throws NoSuchFieldException {
        log.debug("Between partial criteria constructor started for field : {} | with value : {}", member.getKey(), member.getValue());
        criteriaList.add(Criteria.where(member.getKey()).gte(typeConverterService.typeConverter(member.getValue().get(0),
                type.getDeclaredField(member.getKey()).getType())));
        criteriaList.add(Criteria.where(member.getKey()).lte(typeConverterService.typeConverter(member.getValue().get(1),
                type.getDeclaredField(member.getKey()).getType())));
    }

    private void oneArgumentCriteriaPartConstructor(List<Criteria> criteriaList, Map.Entry<String, List<String>> member) throws NoSuchFieldException {
        log.debug("One argument partial criteria constructor started for field : {} | with value : {}", member.getKey(), member.getValue());
        Class<?> targetType = type.getDeclaredField(member.getKey()).getType();
        if (targetType.equals(String.class)) {
            criteriaList.add(Criteria.where(member.getKey())
                    .regex(member.getValue().get(0)));
        } else {
            criteriaList.add(Criteria.where(member.getKey())
                    .is(typeConverterService.typeConverter(member.getValue().get(0), targetType)));
        }
    }
}
