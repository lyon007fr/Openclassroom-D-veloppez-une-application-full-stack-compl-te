package com.mdd.pocmdd.repository;
import com.mdd.pocmdd.models.User;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRespository extends JpaRepository<User, Long> {
    
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);    
}
