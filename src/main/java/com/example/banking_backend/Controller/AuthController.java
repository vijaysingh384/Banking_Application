package com.example.banking_backend.Controller;

import com.example.banking_backend.Model.*;
import com.example.banking_backend.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(@Valid @RequestBody UserDTO userDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        userDTO.setRole("USER");
        UserDTO savedUser = authService.signup(userDTO);
        responseDTO.setStatuscode(HttpStatus.CREATED.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Signup successful");
        responseDTO.setDtos(List.of(savedUser));
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> csrf(CsrfToken csrfToken){
        Map<String, String> token = new LinkedHashMap<>();
        token.put("token", csrfToken.getToken());
        token.put("headerName", csrfToken.getHeaderName());
        token.put("parameterName", csrfToken.getParameterName());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO,
                                             HttpServletRequest request,
                                             HttpServletResponse response){
        ResponseDTO responseDTO = new ResponseDTO();
        String username = loginDTO.getUsername() == null ? "" : loginDTO.getUsername().trim();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginDTO.getPassword()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // Prevent session fixation: a pre-login session (e.g. the CSRF token session) gets a new id.
        if (request.getSession(false) != null) {
            request.changeSessionId();
        }

        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        UserDTO user = authService.getUserByUsername(username);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Login successful");
        responseDTO.setDtos(List.of(user));
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request){
        ResponseDTO responseDTO = new ResponseDTO();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Logout successful");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
