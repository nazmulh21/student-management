package com.exam.school_management.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // টোকেন এনক্রিপ্ট করার জন্য একটি সিক্রেট কি (Key)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // টোকেন কতক্ষণ সচল থাকবে (২৪ ঘণ্টা = ৮৬৪০০০০০ মিলিমেকেন্ড)
    private final long jwtExpirationMs = 86400000;

    // ১. ইউজারনেম দিয়ে টোকেন তৈরি করার মেথড
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ৩. টোকেনটি সঠিক এবং সচল আছে কি না তা যাচাই করার মেথড
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}