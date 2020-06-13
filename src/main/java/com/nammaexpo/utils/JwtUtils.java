package com.nammaexpo.utils;

import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.expection.ExpoException.ErrorCode;
import com.nammaexpo.models.ExpoUserDetails;
import com.nammaexpo.models.JwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtils {

  @Value(value = "${jwtSecretKey}")
  private String SECRET_KEY;

  private Claims verifySignatureAndDecryptToken(String token) {
    try {
      return Jwts.parser()
          .setSigningKey(SECRET_KEY)
          .parseClaimsJws(token)
          .getBody();

    } catch (SignatureException e) {
      throw ExpoException.error(ErrorCode.TOKEN_VERIFICATION_FAILED);

    } catch (ExpiredJwtException e) {
      throw ExpoException.error(ErrorCode.TOKEN_EXPIRED);
    }
  }

  public String generateJwtToken(ExpoUserDetails userDetails) {

    Map<String, Object> claims = new HashMap<>();
    claims.put("payload", JwtPayload.builder()
        .email(userDetails.getUsername())
        .identity(userDetails.getIdentity())
        .role(userDetails.getRole())
        .build());

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1440 * 60 * 1000))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public String validateJwtTokenAndReturnUserName(String token) {
    Claims payload = verifySignatureAndDecryptToken(token);

    return payload.getSubject();
  }
}
