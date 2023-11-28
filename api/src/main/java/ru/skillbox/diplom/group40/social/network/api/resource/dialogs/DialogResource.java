package ru.skillbox.diplom.group40.social.network.api.resource.dialogs;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageShortDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.UnreadCountDto;

@RestController
@RequestMapping("api/v1/dialogs")
@Tag(name = "Api сервиса диалогов",
        description = "Api сервиса диалогов и сообщений")
public interface DialogResource {

    @Operation(summary = "Запрос на получение списка диалогов",
            description = "Получение списка диалогов Page&lt;DialogDto&gt;")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DialogDto.class))
                    })
    })
    @GetMapping
    public ResponseEntity<Page<DialogDto>> getDialogs(Pageable page);

    @Operation(summary = "Получение количества диалогов с непрочитанными сообщениями.",
            description = "Получение количества диалогов с непрочитанными сообщениями.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UnreadCountDto.class))
                    })
    })
    @GetMapping("/unread")
    public ResponseEntity<UnreadCountDto> getUnreadDialogs();

    @Operation(summary = "Получение диалога по id собеседника.",
            description = "Получение DialogDto по id собеседника.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DialogDto.class))
                    })
    })
    @GetMapping("/recipientId/{id}")
    public ResponseEntity<DialogDto> getDialogByRecipientId(@Parameter(description = "id собеседника")
                                                                @PathVariable String id);

    @Operation(summary = "Получение списка сообщений диалога",
            description = "Получение Page&lt;MessageShortDto&gt; по id собеседника")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageShortDto.class))
                    })
    })
    @GetMapping("/messages")
    public ResponseEntity<Page<MessageShortDto>> getMessagesByRecipientId(String recipientId, Pageable page);

    @Operation(summary = "прочтение диалога",
            description = "Отмечаем все непрочитанные сообщения в диалоге как прочитанные")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @PutMapping("/{dialogId}")
    public ResponseEntity<String> putDialog(@PathVariable String dialogId);
}
