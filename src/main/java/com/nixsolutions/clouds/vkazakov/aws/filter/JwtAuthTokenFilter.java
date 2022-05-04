package com.nixsolutions.clouds.vkazakov.aws.filter;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nixsolutions.clouds.vkazakov.aws.util.AwsConstants;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static java.util.List.of;

@Log4j
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    private static final String TOKEN_TYPE = "Bearer ";

    @Autowired
    private AwsConstants awsConstants;

    @Autowired
    private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String idToken = request.getHeader("Authorization");
        if (idToken != null) {
            JWTClaimsSet claims =
                this.configurableJWTProcessor.process(this.getBearerToken(idToken), null);
            validateIssuer(claims);
            verifyIfAccessToken(claims);
            String username = claims.getClaims().get("username").toString();
            if (username != null) {
                List<GrantedAuthority> grantedAuthorities =
                    of(new SimpleGrantedAuthority("ROLE_ADMIN"));
                return new UsernamePasswordAuthenticationToken(username, claims,
                    grantedAuthorities);
            }
        }
        return null;
    }

    private String getBearerToken(String token) {
        return token.startsWith(TOKEN_TYPE) ? token.substring(TOKEN_TYPE.length()) : token;
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(awsConstants.getUserPoolIdURL())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(),
                awsConstants.getUserPoolIdURL()));
        }
    }

    private void verifyIfAccessToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getClaim("token_use").equals("access")) {
            throw new Exception("JWT Token is not an Access Token");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {
        try {
            Authentication authentication = this.authenticate(request);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Can NOT set user authentication -> Message: {" + e.getMessage() +"}", e);
        }
        filterChain.doFilter(request, response);
    }
}
