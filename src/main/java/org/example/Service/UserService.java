package org.example.Service;

import org.example.DTO.UserDto;
import org.example.Dal.Repository.UserRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public Optional<User> chechIfUserEmailExists(UserDto userDto){
     return  userRepository.findUserByEmail(userDto.getEmail());
    }
    public void registerNewUser(UserDto userDto) throws UserAlreadyExistsException {


           if(!isPasswordValid(userDto.getPassword())){
               throw new GeneralException("Password not valid");
           } User user = new User();
           user.setEmail(userDto.getEmail());
           user.setLogin(userDto.getLogin());
           user.setPassword(encodePassword(userDto.getPassword()));
           userRepository.save(user);



    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.encode(password);

    }
    public boolean checkPassword(String rawPassword,String encodedPassword){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.matches(rawPassword,encodedPassword);
    }
    public Optional<User> findUserByLogin(String login){
        return userRepository.findUserByLogin(login);
    }

//public boolean PasswordChecker(String password){
//
//}
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

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user=userRepository.findUserByLogin(login).get();
        if(user==null){
            throw new UsernameNotFoundException("login not found"+user.getLogin());
        }    return new UserDetailsImpl(user);
    }
}