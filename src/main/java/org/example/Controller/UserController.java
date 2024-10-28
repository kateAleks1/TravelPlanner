package org.example.Controller;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.DTO.RefreshTokenRequest;
import org.example.DTO.UserDto;
import org.example.DTO.TokenResponse;
import org.example.Service.TokenService;
import org.example.Service.UserService;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/api")

public class UserController {
    private final UserService userService;
    private BCryptPasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private final SecretKey secretKey;


    @Autowired
    public UserController(UserService userService, TokenService tokenService, BCryptPasswordEncoder passwordEncoder, SecretKey secretKey) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = secretKey;
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserDto userDto) {
        try {
            Optional<User> existingUser =  userService.findUserByLogin(userDto.getLogin());

            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Пользователь с таким логином уже существует"));
            }


            boolean passwordMatches = userService.isPasswordValid(userDto.getPassword());
            if (passwordMatches) {
                Optional<User> existingUserEmail =  userService.chechIfUserEmailExists(userDto);
                if(existingUserEmail.isPresent()){
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Map.of("error", "Пользователь с таким email уже существует"));
                }else{

                    String accessToken = Jwts.builder()
                            .setSubject(userDto.getLogin())
                            .setIssuedAt(new Date(System.currentTimeMillis()))
                            .setExpiration(new Date(System.currentTimeMillis() + 15L))
                            .signWith(secretKey, SignatureAlgorithm.HS512)
                            .compact();
                    userService.registerNewUser(userDto);
                    return ResponseEntity.ok(Map.of("AccessToken", accessToken));
                }

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "An error occurred"));
        }
    }

    @GetMapping("/mainPage")
    public String showSuccessPage() {

        return "items";
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.replace("Bearer", "").trim();
        if (tokenService.validateToken(token)) {
            String username = tokenService.getUsernameFromToken(token);
            HashMap<String, String> Accessclaims = new HashMap<>();
            Accessclaims.put("type", "access");

            String newAccessToken = tokenService.generateAccessToken(username, secretKey);
            Accessclaims.put("type", "refresh");
            String newRefreshToken = tokenService.generateRefreshToken(Accessclaims, username);
            return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("user-login")
    public ResponseEntity<List<String>> getUsersLogin() {
        return ResponseEntity.ok(userService.getAllUsers().stream().map(user -> user.getLogin()).toList());
    }

    @PostMapping("/success")
    public ResponseEntity<String> postOk(@RequestBody String someInput) {

        return ResponseEntity.ok("Success!");
    }
//    @GetMapping("/items")
//    public String getItems(@RequestParam(defaultValue = "0") int page,
//                           @RequestParam(defaultValue = "5") int size,
//                           Model model) {
//        Page<UserDto> usersPage = userService.getUsers(PageRequest.of(page, size));
//        model.addAttribute("users", usersPage.getContent());
//        model.addAttribute("currentPage", usersPage.getNumber());
//        model.addAttribute("totalPages", usersPage.getTotalPages());
//        model.addAttribute("name", "Users List");
//        return "items";
//    }

    @GetMapping("/protectedResources")
    public ResponseEntity<?> protectedResources(@RequestHeader("Authorization") String authorizationHeader) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Authorization header");
        }

        String token = authorizationHeader.substring(7);
        if (tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.getAllUsers().stream().map(user -> user.getLogin()).toList());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (tokenService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");

        }
        String userLogin = tokenService.getUsernameFromToken(refreshToken);
        Optional<User> user = userService.findUserByLogin(userLogin);
        if(!user.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{


            String accessToken = Jwts.builder()
                    .setSubject(user.get().getLogin())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 40L))
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();
            return ResponseEntity.ok(new TokenResponse(accessToken));
        }



    }

    @GetMapping("/tokenIot")
    public ResponseEntity<HashMap<String, String>> getTimeTonen(
            @RequestHeader(value = "Authorization", required = false) String header) {
        HashMap<String, String> map = new HashMap<>();
            String token = header.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(100)
                .build()
                .parseClaimsJws(token)
                .getBody();

            map.put("login",claims.getSubject());
            map.put("exp",claims.getExpiration().toString());
map.put("creatAt",claims.getIssuedAt().toString());
            return ResponseEntity.ok(map);


    }

    @GetMapping("/items")
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<UserDto> usersPage = userService.getUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(usersPage.getContent());
    }

    @PostMapping("/login/{login}")
    public ResponseEntity<Map<String, String>> getUsers(@RequestBody UserDto userDto, @PathVariable String login) {
        Optional<User> user = userService.findUserByLogin(login);
        Map<String, String> response = new HashMap<>();

        if (user.isPresent()) {
            boolean passwordMatches = new BCryptPasswordEncoder().matches(userDto.getPassword(), user.get().getPassword());
            if (passwordMatches) {



               String accessToken = Jwts.builder()
                        .setSubject(login)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 40L))
                       .signWith(secretKey, SignatureAlgorithm.HS512)
                        .compact();
                response.put("AccessToken", accessToken);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/loginUser")
//    public ResponseEntity<String> loginJWTUser(@RequestBody UserDto userDto) {
////login with user so firstly we check if login exists, next if it true we check the password that was entered we encode it and compare it to the password that saved in our datastore
//        //we generate jwt token for user and save it
//        try {
//            Authentication authentication = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String jwt = Jwts.builder()
//                    .setSubject(authentication.getName())
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date(System.currentTimeMillis() + 604800000L))
//                    .signWith(SignatureAlgorithm.HS512, "mySecretKey")
//                    .compact();
//
//            String jsonResponse = "{\"accessToken\": \"" + jwt + "\", \"tokenType\": \"Bearer\"}";
//
//            return ResponseEntity.ok(jsonResponse);
//
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login or password");
//        }
//
//
//    }


}