package com.opentranslation.management.controller;

import java.util.Map;
import java.util.Set;

import com.opentranslation.management.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for authentication-related endpoints.
 * <p>
 * Provides a JWT token generation endpoint based on a client code. The generated token is used to access secured endpoints in the application.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController
{

  /**
   * Utility class for generating and validating JWT tokens.
   */
  private final JwtUtil jwtUtil;

  /**
   * Set of valid client codes allowed to request a JWT token.
   */
  private final Set<String> validClientCodes = Set.of("CLIENT_ABC", "CLIENT_XYZ");

  /**
   * Constructs the AuthController with the provided JWT utility.
   *
   * @param jwtUtil the JWT utility for token generation and validation
   */
  public AuthController(JwtUtil jwtUtil)
  {
    this.jwtUtil = jwtUtil;
  }

  /**
   * Generates a JWT token for a valid client code.
   * <p>
   * Accepts a client code as a request parameter. If the client code is valid, a JWT token is generated and returned in the response body. If the client code
   * is invalid, a 401 Unauthorized response is returned.
   * </p>
   *
   * @param clientCode the client code provided by the requesting client
   * @return {@link ResponseEntity} containing the JWT token or an error message
   */
  @PostMapping("/token")
  public ResponseEntity<?> generateToken(@RequestParam String clientCode)
  {
    if (!validClientCodes.contains(clientCode))
    {
      return ResponseEntity.status(401)
                           .body("Invalid client code");
    }
    String token = jwtUtil.generateToken(clientCode);
    return ResponseEntity.ok(Map.of("token", token));
  }
}
