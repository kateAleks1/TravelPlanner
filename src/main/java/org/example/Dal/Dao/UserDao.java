package org.example.Dal.Dao;
import org.example.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(int id);
List<User> findAllUsers();
Optional<User> findByEmail(String email);
boolean ifUserExistsById(int id);



}
