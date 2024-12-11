package org.example.Dal.Repository;

import org.example.DTO.UserStatistic;
import org.example.DTO.UserStatisticProjection;
import org.example.entity.Trip;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
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
    @Query("SELECT new org.example.DTO.UserStatistic(" +
            "CASE " +
            "    WHEN u.createdAt >= :lastWeek THEN 'Last Week' " +
            "    WHEN u.createdAt >= :lastMonth THEN 'Last Month' " +
            "    WHEN function('DATE', u.createdAt) = function('DATE', :today) THEN 'Today' " +
            "    ELSE 'Older' " +
            "END, " +
            "COUNT(u)) " +
            "FROM User u " +
            "GROUP BY " +
            "CASE " +
            "    WHEN u.createdAt >= :lastWeek THEN 'Last Week' " +
            "    WHEN u.createdAt >= :lastMonth THEN 'Last Month' " +
            "    WHEN function('DATE', u.createdAt) = function('DATE', :today) THEN 'Today' " +
            "    ELSE 'Older' " +
            "END")
    List<UserStatistic> getUserStatistics(@Param("lastWeek") Date lastWeek,
                                          @Param("lastMonth") Date lastMonth,
                                          @Param("today") Date today);

    @Query("SELECT new org.example.DTO.UserStatistic(" +
            "CASE " +
            "    WHEN function('DATE', u.createdAt) = function('DATE', :today) THEN 'Today' " +
            "    ELSE 'Older' " +
            "END, " +
            "COUNT(u)) " +
            "FROM User u " +
            "GROUP BY " +
            "CASE " +
            "    WHEN function('DATE', u.createdAt) = function('DATE', :today) THEN 'Today' " +
            "    ELSE 'Older' " +
            "END")
    List<UserStatistic> getUserStatisticsToday(@Param("today") Date today);
}
