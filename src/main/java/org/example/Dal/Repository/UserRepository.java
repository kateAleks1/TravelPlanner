package org.example.Dal.Repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findUserById(int id);
    Optional<User> findUserById(Integer id);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByLogin(String login);

}
