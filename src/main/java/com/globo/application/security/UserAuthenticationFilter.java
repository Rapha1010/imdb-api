package com.globo.application.security;

import com.globo.application.exceptions.CustomException;
import com.globo.application.models.UserDetailsImplModel;
import com.globo.application.models.UserModel;
import com.globo.application.repositories.UserRepository;
import com.globo.application.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    UserService userService;

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkIfEndpointIsNotPublic(request) ) {
            String token = recoveryToken(request);

            try {
                if (token != null && !"null".equals(token)) {
                    String subject = jwtTokenService.getSubjectFromToken(token);
                    UserModel user = userRepository.findByEmail(subject);

                    if (user.getDisabled()) throw new CustomException("usuário desativado" + request.getRequestURI());

                    UserDetailsImplModel userDetails = new UserDetailsImplModel(user);

                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new CustomException("O token está ausente." + request.getRequestURI());
                }
            } catch (CustomException e) {
                System.err.println(e.getMessage());
            } catch ( RuntimeException e) {
                System.err.println("Erro inesperado: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }

}
