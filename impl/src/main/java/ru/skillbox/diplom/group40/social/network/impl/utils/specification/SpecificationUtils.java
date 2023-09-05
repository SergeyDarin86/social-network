package ru.skillbox.diplom.group40.social.network.impl.utils.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity_;

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

}
