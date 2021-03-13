package com.iamnbty.training.backend.util;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SecurityUtil {

    private SecurityUtil() {

    }

    public static Optional<String> getCurrentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return Optional.empty();
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return Optional.empty();
        }

        String userId = (String) principal;
        return Optional.of(userId);
    }


    public static String generateToken() {
        List<CharacterRule> rules = Arrays.asList(
                new CharacterRule(EnglishCharacterData.UpperCase, 10),

                new CharacterRule(EnglishCharacterData.LowerCase, 10),

                new CharacterRule(EnglishCharacterData.Digit, 10)
        );

        PasswordGenerator generator = new PasswordGenerator();

        return generator.generatePassword(30, rules);
    }

}
