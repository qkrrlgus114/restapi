package com.park.restapi.domain.user.repository;

import com.park.restapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    //  로그인
    @Query("select u from User u where u.email = :email and u.withdrawDate is null")
    Optional<User> findByUserLogin(@Param("email") String email);

    @Query("select u from User u where u.id = :id")
    User findByUserQuery(@Param("id") Long id);
}
