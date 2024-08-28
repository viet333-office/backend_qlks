package com.example.crud.config.security;

import com.example.crud.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.lang.Object;
import java.util.function.Function;
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    String secretkey;

    @Value("${application.security.jwt.refresh-token.expiration}")
    Long refreshExpiration;

    @Value("${application.security.jwt.expiration}")
    Long jwtExpiration;
    /**
     * Trích xuất tên người dùng (subject) từ JWT token.
     *
     * @param token JWT token
     * @return Tên người dùng
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Trích xuất một claim cụ thể từ JWT token.
     *
     * @param token          JWT token
     * @param claimsResolver Hàm lambda để trích xuất claim từ đối tượng Claims
     * @param <T>            Kiểu dữ liệu của claim
     * @return Giá trị của claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extracAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Tạo một access token JWT mới cho người dùng.
     *
     * @param userDetails Thông tin của người dùng
     * @return Access token JWT
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Tạo một access token JWT mới cho người dùng với các claim bổ sung.
     *
     * @param extraClaims Các claim bổ sung
     * @param userDetails Thông tin của người dùng
     * @return Access token JWT
     */

    public String generateToken( Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Tạo một refresh token JWT mới cho người dùng.
     *
     * @param userDetails Thông tin của người dùng
     * @return Refresh token JWT
     */
    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * Tạo một JWT token mới với các claim và thời gian hết hạn được chỉ định.
     *
     * @param extraClaims Các claim bổ sung
     * @param userDetails Thông tin của người dùng
     * @param expiration  Thời gian hết hạn của token (tính bằng mili giây)
     * @return JWT token
     */
    private String buildToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
//        KeyPair keyPair = getSignInKey();
//        PrivateKey privateKey = keyPair.getPrivate();
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Kiểm tra tính hợp lệ của JWT token dựa trên tên người dùng và thời gian hết hạn.
     *
     * @param token       JWT token
     * @param userDetails Thông tin của người dùng
     * @return True nếu token hợp lệ, false nếu không
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpirad(token);
    }

    /**
     * Kiểm tra xem JWT token đã hết hạn hay chưa.
     *
     * @param token JWT token
     * @return True nếu token đã hết hạn, false nếu còn hạn
     */
    private boolean isTokenExpirad(String token) {
        return extracExpiration(token).before(new Date());
    }

    /**
     * Trích xuất thời gian hết hạn từ JWT token.
     *
     * @param token JWT token
     * @return Thời gian hết hạn của token
     */
    private Date extracExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Trích xuất tất cả các claim (claims) từ JWT token.
     *
     * @param token JWT token
     * @return Đối tượng Claims chứa các claim
     */
    private Claims extracAllClaims(String token) {
//        KeyPair keyPair = getSignInKey();
//        PrivateKey privateKey = keyPair.getPrivate();
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Tạo khóa ký cho JWT token từ khóa bí mật (secret key).
     *
     * @return Khóa ký cho JWT token
     */
//    private KeyPair getSignInKey() {
////        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
////        return Keys.hmacShaKeyFor(keyBytes);
//        return Keys.keyPairFor(SignatureAlgorithm.ES256);
//    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
