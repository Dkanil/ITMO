package org.example.lab4.repository;

import org.example.lab4.model.User;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.jooq.tables.Users.USERS;

@Repository
public class UserRepository {
    private final DSLContext dsl;

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void save(User user) {
        dsl.insertInto(USERS)
                .set(USERS.USERNAME, user.getUsername())
                .set(USERS.PASSWORD, user.getPassword())
                .execute();
    }

    public Optional<User> findByUsername(String username) {
        return dsl.selectFrom(USERS)
                .where(USERS.USERNAME.eq(username))
                .fetchOptionalInto(User.class);
    }

    public boolean existsByUsername(String username) {
        return dsl.fetchExists(
                dsl.selectFrom(USERS)
                        .where(USERS.USERNAME.eq(username))
        );
    }
}
