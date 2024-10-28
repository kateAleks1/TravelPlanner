package org.example.Dal.Dao;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

//public class UserDaoImp implements UserDao{
//    private final Session session;
//
//    public UserDaoImp(Session session) {
//        this.session = session;
//    }
//
//    @Override
//    public Optional<User> findById(int id) {
//        String query="from User u where u.id=:userId";
//        Query<User> findByEmail= session.createQuery(query, User.class);
//        User user= findByEmail.setParameter("userId",User.class).getSingleResult();
//        return Optional.ofNullable(user);
//    }
//
//    @Override
//    public List<User> findAllUsers() {
//        String query="From User";
//        Query<User> users=session.createQuery(query,User.class);
//        return users.list();
//    }
//
//    @Override
//    public Optional<User> findByEmail(String email) {
//  String query="from User u where u.email=:email";
//        Query<User> findByEmail= session.createQuery(query, User.class);
//       User user= findByEmail.setParameter("email",email).getSingleResult();
//        return Optional.ofNullable(user);
//    }
//
//    @Override
//    public boolean ifUserExistsById(int id) {
//     String query="select u.id from User u where u.id=:userId";
//     Query<User> sessionQuery=session.createQuery(query,User.class);
//    User user= sessionQuery.setParameter("userId",id).getSingleResult();
//     return Optional.ofNullable(user).isPresent();
//    }

//}
