package com.example.demo.filter;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class FirebaseTokenFilterFactory {

    String profileName;

    public FirebaseTokenFilterFactory(Environment environment) {
        this.profileName = environment.getActiveProfiles()[0];
    }

    public FirebaseTokenFilter getInstance(UserDetailsService userDetailsService, FirebaseAuth firebaseAuth){
        switch (profileName) {
            case "prod" :
                return new ProdFirebaseTokenFilter(userDetailsService, firebaseAuth);

            case "inter" :
                return new InterFirebaseTokenFilter(userDetailsService, firebaseAuth);

            default:
                return new ProdFirebaseTokenFilter(userDetailsService, firebaseAuth);
        }
    }
}
