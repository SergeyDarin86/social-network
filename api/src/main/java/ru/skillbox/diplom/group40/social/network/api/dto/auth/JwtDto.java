package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {
    private UUID id;
    private String email;
    private List<String> roles;
}
