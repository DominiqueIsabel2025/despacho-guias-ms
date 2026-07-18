package cl.duoc.despachoproductor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuracion de seguridad con validacion de JWT emitidos por Azure AD B2C.
 *
 * IMPORTANTE: para que la validacion de la FIRMA del JWT funcione, Spring
 * Boot necesita saber contra que tenant validar. Eso NO se hace aqui en
 * codigo Java: se configura con la propiedad
 *
 *   spring.security.oauth2.resourceserver.jwt.issuer-uri=<URL del tenant>
 *
 * (o jwk-set-uri si el tenant no expone bien el endpoint de metadata).
 * Spring Boot detecta esa propiedad sola y arma el JwtDecoder
 * automaticamente - por eso no aparece un @Bean de JwtDecoder aqui.
 * Esta propiedad faltaba en el proyecto anterior (ver application.properties.example).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/guias/*/descargar").hasAnyRole("DESCARGA", "GESTION")
                .requestMatchers("/api/guias/**").hasRole("GESTION")
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extraerRoles);
        return converter;
    }

    private Collection<GrantedAuthority> extraerRoles(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        String tipoRol = jwt.getClaimAsString("extension_TipoRol");

        if (tipoRol != null) {
            if (tipoRol.equalsIgnoreCase("Descarga")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_DESCARGA"));
            } else if (tipoRol.equalsIgnoreCase("Gestion")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_GESTION"));
            }
        }

        return authorities;
    }
}
