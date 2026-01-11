package com.abar.expensetracker.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.jpa.domain.Specification;

import com.abar.expensetracker.dto.request.ExpenseFilter;
import com.abar.expensetracker.entity.Expense;
import com.abar.expensetracker.entity.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class ExpenseSpecifications {

    public static Specification<Expense> hasUsername(String username) {

        return (root, query, builder) -> {
            if (username == null) {
                return builder.conjunction();
            }
            Join<Expense, User> userJoin = root.join("user");
            return builder.equal(userJoin.get("username"), username);

        };

    }

    public static Specification<Expense> filter(ExpenseFilter filter) {

        return (root, query, builder) -> {
            List<Predicate> preds = new ArrayList<>();

            if (filter.getCategoryName() != null) {
                Join<Expense, Category> categoryJoin = root.join("category");
                preds.add(builder.equal(categoryJoin.get("name"), filter.getCategoryName()));
            }

            if (filter.getFrom() != null) {
                preds.add(builder.greaterThanOrEqualTo(root.get("expenseDate"), filter.getFrom()));
            }

            if (filter.getTo() != null) {
                preds.add(builder.lessThanOrEqualTo(root.get("expenseDate"), filter.getTo()));
            }

            return builder.and(preds.toArray(new Predicate[0]));

        };

    }

}
