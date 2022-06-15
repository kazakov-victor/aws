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

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    private final AwsConstants awsConstants;

    @Bean
    public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor()
        throws MalformedURLException {
        ResourceRetriever resourceRetriever = getResourceRetriever();
        ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor =
            getConfigurableJWTProcessor();
        JWKSource<SecurityContext> jwkSource = getJwkSource(resourceRetriever);
        JWSKeySelector<SecurityContext> keySelector = getKeySelector(jwkSource);
        configurableJWTProcessor.setJWSKeySelector(keySelector);
        return configurableJWTProcessor;
    }

    private DefaultJWTProcessor<SecurityContext> getConfigurableJWTProcessor() {
        return new DefaultJWTProcessor<>();
    }

    private RemoteJWKSet<SecurityContext> getJwkSource(ResourceRetriever resourceRetriever)
        throws MalformedURLException {
        return new RemoteJWKSet<>(new URL(awsConstants.getJwkUrl()), resourceRetriever);
    }

    private DefaultResourceRetriever getResourceRetriever() {
        return new DefaultResourceRetriever(awsConstants.getConnectionTimeout(),
            awsConstants.getReadTimeout());
    }

    private JWSVerificationKeySelector<SecurityContext> getKeySelector(
        JWKSource<SecurityContext> jwkSource) {
        return new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
    }
}
