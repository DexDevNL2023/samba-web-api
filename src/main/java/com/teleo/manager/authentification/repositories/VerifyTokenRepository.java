package com.teleo.manager.authentification.repositories;

import com.teleo.manager.authentification.entities.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long> {
    @Query("SELECT DISTINCT v FROM VerifyToken v WHERE v.token = :token")
    VerifyToken findByToken(@Param("token") String token);
}
