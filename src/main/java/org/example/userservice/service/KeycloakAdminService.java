package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.config.KeycloakConfig.KeycloakProps;
import org.example.userservice.data.enums.Role;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final Keycloak keycloak;
    private final KeycloakProps props;

    /**
     * Method for assign role by admin
     * @param userId
     * @param role
     */
    public void assignRole(String userId, Role role) {
        String roleName = "ROLE_" + role.name();

//        String clientUuid = keycloak.realm(props.getRealm())
//                .clients()
//                .findByClientId(props.getTargetClientId())
//                .get(0)
//                .getId();

//        ClientResource clientResource = keycloak.realm(props.getRealm())
//                .clients()
//                .get(clientUuid);

//        RoleRepresentation roleRepresentation = clientResource.roles()
//                .get(roleName)
//                .toRepresentation();
//
//        keycloak.realm(props.getRealm())
//                .users()
//                .get(userId)
//                .roles()
//                .clientLevel(clientUuid)
//                .add(List.of(roleRepresentation));
        RoleRepresentation roleRepresentation = keycloak.realm(props.getRealm())
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloak.realm(props.getRealm())
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(roleRepresentation));
    }

    public void removeAllClientRoles(String userId) {
        String clientUuid = keycloak.realm(props.getRealm())
                .clients()
                .findByClientId(props.getTargetClientId())
                .get(0)
                .getId();

        List<RoleRepresentation> roles = keycloak.realm(props.getRealm())
                .users()
                .get(userId)
                .roles()
                .clientLevel(clientUuid)
                .listAll();

        keycloak.realm(props.getRealm())
                .users()
                .get(userId)
                .roles()
                .clientLevel(clientUuid)
                .remove(roles);
    }
}