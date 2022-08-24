package com.shopMe.demo.controllers;

import com.shopMe.demo.dto.user.SignInDto;
import com.shopMe.demo.dto.user.SignInResponseDto;
import com.shopMe.demo.dto.user.SignupDto;
import com.shopMe.demo.exceptions.AuthenticationFailException;
import com.shopMe.demo.exceptions.CustomException;
import com.shopMe.demo.dto.user.SignUpResponseDto;
import com.shopMe.demo.model.User;
import com.shopMe.demo.service.AuthenticationService;
import com.shopMe.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public SignUpResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {
        return userService.signUp(signupDto);
    }

    @PostMapping("/signIn")
    public SignInResponseDto SignIn(@RequestBody SignInDto signInDto) throws CustomException, AuthenticationFailException {
        return userService.signIn(signInDto);
    }

}
