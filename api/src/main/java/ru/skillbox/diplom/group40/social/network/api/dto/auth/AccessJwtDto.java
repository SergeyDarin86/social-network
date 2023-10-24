package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessJwtDto extends JwtDto{
    private String refreshTokenId;
}
