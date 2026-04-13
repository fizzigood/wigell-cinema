package com.wigell.cinema.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// Mappar realm_access.roles från Keycloak JWT till ROLE_-prefix för Spring Security.
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            for (String role : roles) {
                // Lägg till ROLE_-prefix
                if (role.startsWith("ROLE_")) {
                    authorities.add(new SimpleGrantedAuthority(role));
                } else {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }

        }

        return authorities;
    }

}