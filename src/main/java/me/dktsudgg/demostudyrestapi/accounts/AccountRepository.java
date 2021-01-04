package me.dktsudgg.demostudyrestapi.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    // null을 리턴할 수 있으므로 Optional로 감싸줌
    Optional<Account> findByEmail(String username);

}
