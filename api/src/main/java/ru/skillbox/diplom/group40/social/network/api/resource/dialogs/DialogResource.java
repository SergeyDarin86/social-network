package ru.skillbox.diplom.group40.social.network.api.resource.dialogs;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageShortDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.UnreadCountDto;

@RestController
@RequestMapping("api/v1/dialogs")
public interface DialogResource {

    @GetMapping
    public ResponseEntity<Page<DialogDto>> getDialogs(Pageable page);

    @GetMapping("/unread")
    public ResponseEntity<UnreadCountDto> getUnreadDialogs();

    @GetMapping("/recipientId/{id}")
    public ResponseEntity<DialogDto> getDialogByRecipientId(@PathVariable String id);

    @GetMapping("/messages")
    public ResponseEntity<Page<MessageShortDto>> getMessagesByRecipientId(String recipientId, Pageable page);

    @PutMapping("/{dialogId}")
    public ResponseEntity<String> putDialog(@PathVariable String dialogId);
}
