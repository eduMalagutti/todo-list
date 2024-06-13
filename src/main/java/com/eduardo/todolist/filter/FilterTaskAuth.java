package com.eduardo.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eduardo.todolist.user.IUserRepository;
import com.eduardo.todolist.user.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    IUserRepository userRepository;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/tasks")) {
            String authorization = request.getHeader("Authorization");

            System.out.println(authorization);

            String authEncoded = authorization.substring("Basic".length()).trim();

            System.out.println(authEncoded);

            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

            System.out.println(authDecoded);

            String authString = new String(authDecoded);

            System.out.println(authString);

            String[] credentials = authString.split(":");

            String username = credentials[0];
            String password = credentials[1];

            System.out.println(username);
            System.out.println(password);

            UserModel user = this.userRepository.findByUsername(username);

            if (user == null) {

                response.sendError(401, "usuario não existe");

            } else {

                Result passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerified.verified) {

                    request.setAttribute("idUser", user.getId());

                    filterChain.doFilter(request, response);
                
                } else {
                    
                    response.sendError(401, "senha incorreta");
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

}
