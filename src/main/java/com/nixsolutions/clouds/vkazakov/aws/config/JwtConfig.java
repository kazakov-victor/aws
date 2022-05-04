package com.nixsolutions.clouds.vkazakov.aws.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nixsolutions.clouds.vkazakov.aws.util.AwsConstants;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    private final AwsConstants awsConstants;

    @Bean
    public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor()
        throws MalformedURLException {
        ResourceRetriever resourceRetriever =
            new DefaultResourceRetriever(awsConstants.getConnectionTimeout(),
                awsConstants.getReadTimeout());
        ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor =
            new DefaultJWTProcessor<>();
        JWKSource<SecurityContext> jwkSource =
            new RemoteJWKSet<>(new URL(awsConstants.getJwkUrl()), resourceRetriever);
        JWSKeySelector<SecurityContext> keySelector =
            new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
        configurableJWTProcessor.setJWSKeySelector(keySelector);
        return configurableJWTProcessor;
    }
}
