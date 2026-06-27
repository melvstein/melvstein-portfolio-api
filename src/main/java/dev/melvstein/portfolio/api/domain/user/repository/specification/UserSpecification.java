package dev.melvstein.portfolio.api.domain.user.repository.specification;

import dev.melvstein.portfolio.api.domain.user.entity.User;
import dev.melvstein.portfolio.api.domain.user.repository.specification.filter.UserFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filter(UserFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.role() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), filter.role()));
            }

            if (filter.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.status()));
            }

            if (filter.username() != null) {
                predicates.add(criteriaBuilder.equal(root.get("username"), filter.username()));
            }

            if (filter.createdAtFrom() != null && filter.createdAtTo() != null) {
                predicates.add(
                        criteriaBuilder.between(
                                root.get("createdAt"),
                                filter.createdAtFrom(),
                                filter.createdAtTo()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
