package ru.skillbox.diplom.group40.social.network.api.dto.dialog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UnreadCountDto {
    @Schema(description = "количество непрачитанных сообщений")
    Integer count;
}
