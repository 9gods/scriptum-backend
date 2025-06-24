package com.scriptum.backend.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseBody {

    private UUID id;

    private String name;

    private String email;

    private String password;

    private String avatarUrl;

    private boolean emailVerified;

}
