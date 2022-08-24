package com.shopMe.demo.service;


import com.shopMe.demo.config.MessageStrings;
import com.shopMe.demo.dto.ResponseDto;
import com.shopMe.demo.dto.user.*;
import com.shopMe.demo.enums.ResponseStatus;
import com.shopMe.demo.enums.Role;
import com.shopMe.demo.exceptions.AuthenticationFailException;
import com.shopMe.demo.exceptions.CustomException;
import com.shopMe.demo.model.AuthenticationToken;
import com.shopMe.demo.model.User;
import com.shopMe.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import static com.shopMe.demo.config.MessageStrings.USER_CREATED;
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;


    Logger logger = LoggerFactory.getLogger(UserService.class);
    public SignUpResponseDto signUp(SignupDto signupDto)  throws CustomException {
        // Check to see if the current email address has already been registered.
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // If the email address has been registered then throw an exception.
            throw new CustomException("User already exists");
        }
        // first encrypt the password
        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), Role.user, encryptedPassword );
        try {
            // save the User
             userRepository.save(user);
            // generate token for user
            final AuthenticationToken authenticationToken = new AuthenticationToken(user);
            // save token in database
            authenticationService.saveConfirmationToken(authenticationToken);
            // success in creating
            return new SignUpResponseDto("success", "user created successfully");
        } catch (Exception e) {
            // handle signup error
            throw new CustomException(e.getMessage());
        }
    }

    String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }

    public SignInResponseDto signIn(SignInDto signInDto) throws AuthenticationFailException, CustomException {
        // first find User by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if(!Objects.nonNull(user)){
            throw new AuthenticationFailException("user not present");
        }
        try {
            // check if password is right
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
                // passwords do not match
                throw  new AuthenticationFailException(MessageStrings.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if(!Objects.nonNull(token)) {
            // token not present
            throw new CustomException(MessageStrings.AUTH_TOEKN_NOT_PRESENT);
        }

        return new SignInResponseDto ("success", token.getToken());
    }
//    public ResponseDto createUser(String token, UserCreateDto userCreateDto) throws CustomException, AuthenticationFailException {
//        User creatingUser = authenticationService.getUser(token);
//        if (!canCrudUser(creatingUser.getRole())) {
//            // user can't create new user
//            throw  new AuthenticationFailException(MessageStrings.USER_NOT_PERMITTED);
//        }
//        String encryptedPassword = userCreateDto.getPassword();
//        try {
//            encryptedPassword = hashPassword(userCreateDto.getPassword());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            logger.error("hashing password failed {}", e.getMessage());
//        }
//
//        User user = new User(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getEmail(), userCreateDto.getRole(), encryptedPassword );
//        User createdUser;
//        try {
//            createdUser = userRepository.save(user);
//            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
//            authenticationService.saveConfirmationToken(authenticationToken);
//            return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
//        } catch (Exception e) {
//            // handle user creation fail error
//            throw new CustomException(e.getMessage());
//        }
//
//    }

//    boolean canCrudUser(Role role) {
//        if (role == Role.admin || role == Role.manager) {
//            return true;
//        }
//        return false;
//    }

//    boolean canCrudUser(User userUpdating, Integer userIdBeingUpdated) {
//        Role role = userUpdating.getRole();
//        // admin and manager can crud any user
//        if (role == Role.admin || role == Role.manager) {
//            return true;
//        }
//        // user can update his own record, but not his role
//        if (role == Role.user && userUpdating.getId() == userIdBeingUpdated) {
//            return true;
//        }
//        return false;
//    }
//
//    public List<User> findAll() {
//        return userRepository.findAll();
//    }
}
