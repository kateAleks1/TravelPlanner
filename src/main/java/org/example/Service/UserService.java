package org.example.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.DTO.UserDto;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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

    public User registerNewUser(UserDto userDto) throws UserAlreadyExistsException {


           if(!isPasswordValid(userDto.getPassword())){
               throw new GeneralException("Password not valid");
           } User user = new User();
           user.setEmail(userDto.getEmail());
           user.setLogin(userDto.getLogin());
           user.setPassword(encodePassword(userDto.getPassword()));
          return userRepository.save(user);
    }
public Optional<User> findUserById(int id){
        return userRepository.findUserById(id);
}
    public Optional<List<User>> findListUserById(List<Integer> usersIds){
        List<User> users = usersIds.stream()
                .map(userRepository::findUserById) // Вернет Optional<User>
                .filter(Optional::isPresent)       // Отфильтруем только присутствующие значения
                .map(Optional::get)                // Получим объект User из Optional
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
    return usersPage.map(user -> new UserDto(user.getId(), user.getEmail(),user.getLogin(),user.getPassword()));
}


    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword()
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

        // Удаляем записи из trip_participants
        List<TripPartcipants> participants = tripParticipantRepository.findAllByUser(user);
        tripParticipantRepository.deleteAll(participants);

        // Удаляем самого пользователя
        userRepository.delete(user);
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user=userRepository.findUserByLogin(login).get();
        if(user==null){
            throw new UsernameNotFoundException("login not found"+user.getLogin());
        }    return new UserDetailsImpl(user);
    }
}