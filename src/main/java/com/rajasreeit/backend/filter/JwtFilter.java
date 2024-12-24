package com.rajasreeit.backend.filter;

import com.rajasreeit.backend.service.CrmDetailsService;
import com.rajasreeit.backend.service.JwtService;
import com.rajasreeit.backend.service.userdetails.CrmEmployeeDetailService;
import com.rajasreeit.backend.service.userdetails.CustomerDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Qualifier("crmDetailsService")
    private CrmDetailsService crmDetailsService;

    @Autowired
    @Qualifier("crmEmployeeDetailService")
    private CrmEmployeeDetailService crmEmployeeDetailService;

    @Autowired
    @Qualifier("customerDetailsService")
    private CustomerDetailsService customerDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String uri = request.getRequestURI();
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));


            UserDetails userDetails = null;

            // Select the appropriate UserDetailsService based on the role
            if ("ADMIN".equals(role)) {
                userDetails = crmDetailsService.loadUserByUsername(username);
            } else if ("EMPLOYEE".equals(role)) {
                userDetails = crmEmployeeDetailService.loadUserByUsername(username);
            } else if ("CUSTOMER".equals(role)) {
                userDetails = customerDetailsService.loadUserByUsername(username);
            }

            if(jwtService.validateToken(token, userDetails)){
                // Verify if the role is authorized to access the current URI
                if (!isAuthorized(role, uri)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return; // Stop further processing
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthorized(String role, String uri) {
        if (uri.startsWith("/crm/admin") && "ADMIN".equals(role)) return true;
        if (uri.startsWith("/crm/employee") && "EMPLOYEE".equals(role)) return true;
        if (uri.startsWith("/customer") && "CUSTOMER".equals(role)) return true;
        return false;
    }
}
