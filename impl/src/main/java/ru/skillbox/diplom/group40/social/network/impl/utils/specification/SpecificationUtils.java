package ru.skillbox.diplom.group40.social.network.impl.utils.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity_;

import java.time.LocalDateTime;

public class SpecificationUtils {

    public static <T> Specification<T> like(String key, String value) {
        return (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.like(root.get(key), "%" + value + "%");
    }

    public static <T, K> Specification<T> equal(String key, K value) {
        return (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.equal(root.get(key), value);
    }

    public static Specification getBaseSpecification(BaseSearchDto baseSearchDto) {
        return equal(BaseEntity_.ID, baseSearchDto.getId())
                .and(equal(BaseEntity_.IS_DELETED, baseSearchDto.getIsDeleted()));
    }

    //taras
   public static <T, K> Specification<T> ageTo(String key, K value) {
       Specification<T> spec = (root, query, criteriaBuilder) -> value == null
               ? null : criteriaBuilder.greaterThan(root.get(key),  LocalDateTime.now().minusYears((int)value));
        return spec;
    }
    public static <T, K> Specification<T> equalIn(String key, K value) {
        Specification<T> spec = (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.in(root.get(key)).value(value);
        return spec;
    }


    public static <T, K> Specification<T> ageFrom(String key, K value) {
        Specification<T> spec = (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.lessThan(root.get(key),  LocalDateTime.now().minusYears((int)value));
        return spec;

    }

    public static <T, K> Specification<T> ageFromDate(String key, K value) {
        Specification<T> spec = (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.greaterThan(root.get(key),  LocalDateTime.parse(String.valueOf(value)));
        return spec;

    }

    public static <T, K> Specification<T> ageToDate(String key, LocalDateTime value) {
        Specification<T> spec = (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.lessThan(root.get(key),  LocalDateTime.parse(String.valueOf(value)));
        return spec;
    }

    public static <T, K> Specification <T> betweenDate(String key, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo){
        Specification<T> spec = ((root, query, criteriaBuilder) -> dateTimeFrom == null || dateTimeTo == null
        ? null : criteriaBuilder.between(root.get(key), dateTimeFrom, dateTimeTo));
        return spec;
    }

}
