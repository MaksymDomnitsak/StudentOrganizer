package com.studyorganizer.userstokens.repositories;


import com.studmodel.Token;
import com.studmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    void deleteByToken(String token);
    @Modifying
    int deleteByUser(User user);
    Optional<Token> findByUser(User user);
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId")
    Token findByUserId(Long userId);
}
