package org.example.Dal.Repository;

import org.example.entity.Trip;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findUserById(int id);
    Optional<User> findUserById(Integer id);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByLogin(String login);
    @Query("SELECT u.id FROM User u WHERE u.login = :login")
    Optional<Integer> findUserIdByUserLogin(@Param("login") String login);
    @Query("select u.id FROM User u where u.login in:logins")
    Optional<List<Integer>> findAllUserIdsFromUserLogins(@Param("logins") List<String> logins);
    @Query("SELECT u FROM User u WHERE u.login LIKE %:prefix%")
Optional<List<User>> findUserByPrefix(@Param("prefix")String prefix);

}
