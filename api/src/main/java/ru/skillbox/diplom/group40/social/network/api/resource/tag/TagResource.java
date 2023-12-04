package ru.skillbox.diplom.group40.social.network.api.resource.tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagSearchDto;

/**
 * TagResource
 *
 * @author Sergey D.
 */

@Tag(name = "Api сервиса тегов", description = "Сервис для получения всех тегов.")
public interface TagResource {

    @Operation(description = "Отправка запроса на получение тегов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TagDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping
    ResponseEntity getAll(TagSearchDto tagSearchDto);
}
