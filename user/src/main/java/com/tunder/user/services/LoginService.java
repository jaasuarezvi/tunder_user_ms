package com.tunder.user.services;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Optional;

import com.tunder.user.models.LoginModel;
import com.tunder.user.models.TokenModel;
import com.tunder.user.models.UserModel;
import com.tunder.user.repositories.LoginRepository;
import com.tunder.user.repositories.TokenRopository;
import com.tunder.user.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired 
    TokenRopository tokenRopository;

    public TokenModel login(UserModel user){
        //TODO
        //get user by name (set names as unique value)\
        Optional<UserModel> dbUser;
        dbUser = userRepository.findByName(user.getName());
        if (dbUser.isPresent()){
            if(user.encrypt().equals(dbUser.get().getPassword())){
                Optional<TokenModel> token = tokenRopository.findByuserID(user.getId());
                if (token.isPresent()){
                    return tokenRopository.save(token.get().refreshToken());
                } 
            }
        }
        return null;
    }

    public boolean isValidToken(TokenModel token){
        TokenModel dbToken;
        Optional<TokenModel> OptionalToken = tokenRopository.findById(token.getUserID());
        if( OptionalToken.isPresent()){
            dbToken = OptionalToken.get();
            if(dbToken.getToken().equals(token)){
                return true;
            }
        }
        return false;
    }

    public TokenModel refeshToken(TokenModel token){
        TokenModel dbToken;
        Optional<TokenModel> OptionalToken = tokenRopository.findById(token.getUserID());
        if( OptionalToken.isPresent()){
            dbToken = OptionalToken.get();
            dbToken.refreshToken();
            tokenRopository.save(dbToken);
            return dbToken;
        }
        return null;
    }
    
}
