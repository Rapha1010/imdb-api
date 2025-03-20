package com.globo.application.specifications;

import com.globo.application.models.UserModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<UserModel> {

    public String searchTerm;

    public UserSpecification(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public Predicate toPredicate(Root<UserModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(this.searchTerm)) {
            Predicate movieNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + this.searchTerm.toLowerCase() + "%");
//            Predicate genresPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("genres")), "%" + this.searchTerm.toLowerCase() + "%");
//            Predicate directorNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("directorName")), "%" + this.searchTerm.toLowerCase() + "%");
//            Predicate castNamesPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("castNames")), "%" + this.searchTerm.toLowerCase() + "%");
            predicates.add(criteriaBuilder.or(movieNamePredicate));
        }

        return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    }
}
