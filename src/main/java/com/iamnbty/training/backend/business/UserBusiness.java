package com.iamnbty.training.backend.business;

import com.iamnbty.training.backend.entity.User;
import com.iamnbty.training.backend.exception.BaseException;
import com.iamnbty.training.backend.exception.FileException;
import com.iamnbty.training.backend.exception.UserException;
import com.iamnbty.training.backend.mapper.UserMapper;
import com.iamnbty.training.backend.model.*;
import com.iamnbty.training.backend.service.TokenService;
import com.iamnbty.training.backend.service.UserService;
import com.iamnbty.training.backend.util.SecurityUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Log4j2
public class UserBusiness {

    private final UserService userService;

    private final TokenService tokenService;

    private final UserMapper userMapper;

    private final EmailBusiness emailBusiness;

    public UserBusiness(UserService userService, TokenService tokenService, UserMapper userMapper, EmailBusiness emailBusiness) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.emailBusiness = emailBusiness;
    }

    public MLoginResponse login(MLoginRequest request) throws BaseException {
        // validate request

        // verify database
        Optional<User> opt = userService.findByEmail(request.getEmail());
        if (opt.isEmpty()) {
            throw UserException.loginFailEmailNotFound();
        }

        User user = opt.get();

        // verify password
        if (!userService.matchPassword(request.getPassword(), user.getPassword())) {
            throw UserException.loginFailPasswordIncorrect();
        }

        // verify activate status
        if (!user.isActivated()) {
            throw UserException.loginFailUserUnactivated();
        }

        MLoginResponse response = new MLoginResponse();
        response.setToken(tokenService.tokenize(user));
        return response;
    }

    public String refreshToken() throws BaseException {
        Optional<String> opt = SecurityUtil.getCurrentUserId();
        if (opt.isEmpty()) {
            throw UserException.unauthorized();
        }

        String userId = opt.get();

        Optional<User> optUser = userService.findById(userId);
        if (optUser.isEmpty()) {
            throw UserException.notFound();
        }

        User user = optUser.get();
        return tokenService.tokenize(user);
    }

    public MRegisterResponse register(MRegisterRequest request) throws BaseException {
        String token = SecurityUtil.generateToken();
        User user = userService.create(request.getEmail(), request.getPassword(), request.getName(), token, nextXMinute(30));

        sendEmail(user);

        return userMapper.toRegisterResponse(user);
    }

    public MActivateResponse activate(MActivateRequest request) throws BaseException {
        String token = request.getToken();
        if (StringUtil.isNullOrEmpty(token)) {
            throw UserException.activateNoToken();
        }

        Optional<User> opt = userService.findByToken(token);
        if (opt.isEmpty()) {
            throw UserException.activateFail();
        }

        User user = opt.get();

        if (user.isActivated()) {
            throw UserException.activateAlready();
        }

        Date now = new Date();
        Date expireDate = user.getTokenExpire();
        if (now.after(expireDate)) {
            throw UserException.activateTokenExpire();
        }

        user.setActivated(true);
        userService.update(user);

        MActivateResponse response = new MActivateResponse();
        response.setSuccess(true);
        return response;
    }

    public void resendActivationEmail(MResendActivationEmailRequest request) throws BaseException {
        String email = request.getEmail();
        if (StringUtil.isNullOrEmpty(email)) {
            throw UserException.resendActivationEmailNoEmail();
        }

        Optional<User> opt = userService.findByEmail(email);
        if (opt.isEmpty()) {
            throw UserException.resendActivationEmailNotFound();
        }

        User user = opt.get();

        if (user.isActivated()) {
            throw UserException.activateAlready();
        }

        user.setToken(SecurityUtil.generateToken());
        user.setTokenExpire(nextXMinute(30));
        user = userService.update(user);

        sendEmail(user);
    }

    private Date nextXMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private void sendEmail(User user) {
        String token = user.getToken();

        try {
            emailBusiness.sendActivateUserEmail(user.getEmail(), user.getName(), token);
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

    public String uploadProfilePicture(MultipartFile file) throws BaseException {
        // validate file
        if (file == null) {
            // throw error
            throw FileException.fileNull();
        }

        // validate size
        if (file.getSize() > 1048576 * 2) {
            // throw error
            throw FileException.fileMaxSize();
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            // throw error
            throw FileException.unsupported();
        }

        List<String> supportedTypes = Arrays.asList("image/jpeg", "image/png");
        if (!supportedTypes.contains(contentType)) {
            // throw error (unsupport)
            throw FileException.unsupported();
        }

        // TODO: upload file File Storage (AWS S3, etc...)
        try {
            byte[] bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
