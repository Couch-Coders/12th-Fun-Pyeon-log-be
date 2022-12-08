package com.example.demo.dto;

import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirebaseTokenDTO {
    String uid;
    String name;
    String email;

    public FirebaseTokenDTO(FirebaseToken firebaseToken) {
        this.uid = firebaseToken.getUid();
        this.name = firebaseToken.getName();
        this.email = firebaseToken.getEmail();
    }
}
