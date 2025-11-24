package br.ifsp.virtual_studies.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import br.ifsp.virtual_studies.security.CustomJwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.core.io.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("classpath:keys/public.pem")
    private Resource publicKeyResource;

    @Value("classpath:keys/private.pem")
    private Resource privateKeyResource;

    @Bean
    public CustomJwtAuthenticationConverter customJwtAuthenticationConverter() {
        return new CustomJwtAuthenticationConverter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            CustomJwtAuthenticationConverter customJwtAuthenticationConverter) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/register").permitAll().anyRequest().authenticated())
                .oauth2ResourceServer(
                        conf -> conf.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() throws Exception {
        String publicKeyContent = new String(publicKeyResource.getInputStream().readAllBytes());
        RSAPublicKey publicKey = (RSAPublicKey) publicKey(publicKeyResource);
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() throws Exception {
        String publicKeyContent = new String(publicKeyResource.getInputStream().readAllBytes());
        String privateKeyContent = new String(privateKeyResource.getInputStream().readAllBytes());

        RSAPublicKey publicKey = (RSAPublicKey) publicKey(publicKeyResource);
        RSAPrivateKey privateKey = (RSAPrivateKey) privateKey(privateKeyResource);

        var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public RSAPublicKey publicKey(@Value("classpath:certs/public.pem") Resource resource) throws Exception {
        return PemUtils.loadPublicKey(resource.getInputStream());
    }

    @Bean
    public RSAPrivateKey privateKey(@Value("classpath:certs/private.pem") Resource resource) throws Exception {
        return PemUtils.loadPrivateKey(resource.getInputStream());
    }
}
