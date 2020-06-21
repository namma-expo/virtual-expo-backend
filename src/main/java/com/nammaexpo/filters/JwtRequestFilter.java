package com.nammaexpo.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.services.ExpoUserDetailsService;
import com.nammaexpo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private ExpoUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            final String authorizationHeader = request.getHeader("Authorization");

            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
            }

            if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                String userName = jwtUtils.validateJwtTokenAndReturnUserName(jwt);

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            chain.doFilter(request, response);

        } catch (ExpoException e) {

            ObjectMapper mapper = new ObjectMapper();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(mapper.
                    writeValueAsString(MessageResponse.builder()
                            .messageCode(e.getMessageCode())
                            .context(e.getContext())
                            .build()
                    )
            );
        }
    }

}