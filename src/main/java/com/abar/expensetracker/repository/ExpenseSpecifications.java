package com.abar.expensetracker.repository;

import org.springframework.data.jpa.domain.Specification;
import com.abar.expensetracker.entity.Expense;
import com.abar.expensetracker.entity.User;

import jakarta.persistence.criteria.Join;

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

}
