package com.fu.lhm.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.user.entity.User;
import com.fu.lhm.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final String USER_ID = "userId";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String ROLE = "role";

    private static final String ADDRESS = "address";

    private static final String IDENTITYNUMBER = "identityNumber";
    private static final String PHONE = "phone";

    private static final String BIRTH = "birth";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public User getUser(HttpServletRequest httpServletRequest) throws BadRequestException {
        String token = httpServletRequest.getHeader("Authorization").substring(7);

        Claims claims = extractAllClaims(token);

        ObjectMapper objectMapper = new ObjectMapper();

        Long userId = objectMapper.convertValue(claims.get(USER_ID), Long.class);

        return userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User không tồn tại!"));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User userDetails) {

        Claims claims = buildClaims(userDetails);

        return generateToken(claims, userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            User userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims buildClaims(User user) {
        Claims claims = new DefaultClaims();

        claims.put(USER_ID, user.getId());
        claims.put(ROLE, user.getRole());
        claims.put(EMAIL, user.getEmail());
        claims.put(NAME, user.getName());
        claims.put(ADDRESS, user.getAddress());
        claims.put(IDENTITYNUMBER, user.getIdentityNumber());
        claims.put(PHONE, user.getPhone());
        claims.put(BIRTH, user.getBirth()+"");

        return claims;
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
