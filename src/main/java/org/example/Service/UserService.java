package org.example.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Temporal;
import org.example.DTO.UserDto;
import org.example.DTO.UserStatistic;
import org.example.DTO.UserStatisticProjection;
import org.example.Dal.Repository.TripParticipantRepository;
import org.example.Dal.Repository.TripRepository;
import org.example.Dal.Repository.UserRepository;
import org.example.entity.Trip;
import org.example.entity.TripPartcipants;
import org.example.entity.User;
import org.example.exception.GeneralException;
import org.example.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
private final TripRepository tripRepository;
private final TripParticipantRepository tripParticipantRepository;
@Autowired
    public UserService(UserRepository userRepository, TripRepository tripRepository, TripParticipantRepository tripParticipantRepository) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.tripParticipantRepository = tripParticipantRepository;
    }

    public boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public Optional<User> chechIfUserEmailExists(UserDto userDto){
     return  userRepository.findUserByEmail(userDto.getEmail());
    }
    public Optional<User> chechIfUserIdExists(int id){
        return  userRepository.findUserById(id);
    }
    public Optional<User> findUserByLogin(String login){
        return userRepository.findUserByLogin(login);
    }

    public void setCreatedDates() {
        List<User> users = userRepository.findAll();

        for (User user : users) {

            Optional<List<Trip>> optionalTrips = tripRepository.getTripByUsersId(user.getId());
            if (optionalTrips.isEmpty()) {
                continue;
            }

            List<Trip> trips = optionalTrips.get();
            Date earliestDate = Date.from(Instant.now());

            for (Trip trip : trips) {
                Date dateTrip = tripRepository.getCreatedAtFromTripById(trip.getTripId());
                if (dateTrip.before(earliestDate)) {
                    earliestDate = dateTrip;
                }
            }

            LocalDate localDate = earliestDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate userCreatedAt = localDate.minusDays(5);
            Date dateUserCreated = Date.from(userCreatedAt.atStartOfDay(ZoneId.systemDefault()).toInstant());

            user.setCreatedAt(dateUserCreated);
            userRepository.save(user);
        }



    }
    public User registerNewUser(UserDto userDto) throws UserAlreadyExistsException {


           if(!isPasswordValid(userDto.getPassword())){
               throw new GeneralException("Password not valid");
           } User user = new User();
           user.setEmail(userDto.getEmail());
           user.setLogin(userDto.getLogin());

           user.setPassword(encodePassword(userDto.getPassword()));
           user.setCreatedAt(userDto.getCreatedAt());
          return userRepository.save(user);
    }
public Optional<User> findUserById(int id){
        return userRepository.findUserById(id);
}
    public Optional<List<User>> findListUserById(List<Integer> usersIds){
        List<User> users = usersIds.stream()
                .map(userRepository::findUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return users.isEmpty()?Optional.empty():Optional.of(users);
    }
    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.encode(password);

    }
    public boolean checkPassword(String rawPassword,String encodedPassword){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.matches(rawPassword,encodedPassword);
    }


    public List<UserStatistic> getUserStatistics(Date lastWeek, Date lastMonth,Date today) {
        return userRepository.getUserStatistics(lastWeek, lastMonth,today);
    }

    public List<UserStatistic> getUserStatisticsToday(Date today) {
        return userRepository.getUserStatisticsToday(today);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Page<User> getUserPaginated(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        return userRepository.findAll(pageable);

    }
//    @Transactional(readOnly = true)
//    public Page<UserDto> getUsers(Pageable pageable) {
//
//        Page<User> usersPage = userRepository.findAll(pageable);
//
//        return usersPage.map(this::convertToDto);
//    }

public Page<UserDto> getUsers(Pageable pageable) {
    Page<User> usersPage = userRepository.findAll(pageable);
    return usersPage.map(user -> new UserDto(user.getId(), user.getEmail(),user.getLogin(),user.getPassword(),user.getCreatedAt()));
}


    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword(),user.getCreatedAt()
        );
    }

    public Optional<User> updateUser(int userId, UserDto userDto){
        if(userRepository.findUserById(userId).isPresent()){
           User user = userRepository.findUserById(userId).get();
           user.setLogin(userDto.getLogin());
           user.setEmail(userDto.getEmail());
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        List<TripPartcipants> participants = tripParticipantRepository.findAllByUser(user);
        tripParticipantRepository.deleteAll(participants);


        userRepository.delete(user);
    }
     public Integer findUserIdByUserLogin(String login){
    return  userRepository.findUserIdByUserLogin(login).get();
    }
    public List<Integer> findAllUserIdsFromUserLogins(List<String> logins){
    return userRepository.findAllUserIdsFromUserLogins(logins).get();
    }
public List<User> SearchUsersByPrefix(String loginPrefix){
    return userRepository.findUserByPrefix(loginPrefix).get();
}
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user=userRepository.findUserByLogin(login).get();
        if(user==null){
            throw new UsernameNotFoundException("login not found"+user.getLogin());
        }    return new UserDetailsImpl(user);
    }
}