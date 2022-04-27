package com.nixsolutions.clouds.vkazakov.aws.jwtauthentication;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nixsolutions.clouds.vkazakov.aws.config.AwsConfig;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import static java.util.List.of;

public class JwtAuthTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = Logger.getLogger(JwtAuthTokenFilter.class);
    private static final String TOKEN_TYPE = "Bearer ";

    @Autowired
    private AwsConfig awsConfig;

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
                User user = new User(username, "", of());
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
        if (!claims.getIssuer().equals(awsConfig.getUserPoolIdURL())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(),
                awsConfig.getUserPoolIdURL()));
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
            logger.error("Can NOT set user authentication -> Message: {" + e.getMessage() +"}", e);
        }
        filterChain.doFilter(request, response);
    }
}
