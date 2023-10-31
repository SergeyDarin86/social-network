package ru.skillbox.diplom.group40.social.network.impl.utils.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity_;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag_;

import java.time.LocalDateTime;

public class SpecificationUtils {

    public static <T> Specification<T> like(String key, String value) {
        return (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), "%" + value.toLowerCase() + "%");
    }

    public static <T, K> Specification<T> equal(String key, K value) {
        return (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.equal(root.get(key), value);
    }
    public static <T, K> Specification<T>notEqual(String key, K value) {
        return (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.notEqual(root.get(key), value);
    }


    public static <T, K> Specification<T>notEqualIn(String key, K value) {
        return (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.not(criteriaBuilder.in(root.get(key)).value(value));
    }


    public static Specification getBaseSpecification(BaseSearchDto baseSearchDto) {
        return equal(BaseEntity_.ID, baseSearchDto.getId())
                .and(equal(BaseEntity_.IS_DELETED, baseSearchDto.getIsDeleted()));
    }


    public static <T, K> Specification<T> in(String key, K value) {
        Specification<T> spec = (root, query, criteriaBuilder) -> value == null
                ? null : criteriaBuilder.in(root.get(key)).value(value);
        return spec;
    }

    public static <T, K> Specification<T>  between(String key, Integer ageFrom,  Integer ageTo) {
        Specification<T> spec = null;
        if((ageFrom==null)&(ageTo!=null)){
            spec = (root, query, criteriaBuilder) ->  criteriaBuilder.greaterThan(root.get(key),  LocalDateTime.now().minusYears((int)ageTo));
        }
        else if(((ageFrom!=null)&(ageTo==null))){
            spec = (root, query, criteriaBuilder) ->  criteriaBuilder.lessThan(root.get(key),  LocalDateTime.now().minusYears((int)ageFrom));
        }
        else if(((ageFrom!=null)&(ageTo!=null))) {
            spec = (root, query, criteriaBuilder) ->  criteriaBuilder.between(root.get(key), LocalDateTime.now().minusYears((int) ageTo), LocalDateTime.now().minusYears((int) ageFrom));
        }
        return spec;
    }



    public static <T, K> Specification <T> betweenDate(String key, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo){
        Specification<T> spec = ((root, query, criteriaBuilder) -> dateTimeFrom == null || dateTimeTo == null
                ? null : criteriaBuilder.between(root.get(key), dateTimeFrom, dateTimeTo));
        return spec;
    }

    public static <T, K> Specification<T> containTag(String key, K value) {

        Specification spec = null;
        if (value != null) {
            spec = (root, query, criteriaBuilder) -> {
                Join<Post, Tag> postsTag = root.join(key);
                return query.multiselect(postsTag).where(criteriaBuilder.in(postsTag.get(Tag_.NAME)).value(value)).getRestriction();
            };
        }
        return spec;

    }

}