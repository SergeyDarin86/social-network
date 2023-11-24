package ru.skillbox.diplom.group40.social.network.impl.resource.dialog;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageShortDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.UnreadCountDto;
import ru.skillbox.diplom.group40.social.network.api.resource.dialogs.DialogResource;
import ru.skillbox.diplom.group40.social.network.impl.service.dialog.DialogService;

@Controller
@AllArgsConstructor
public class DialogResourceImpl implements DialogResource {
    private final DialogService dialogService;
    @Override
    public ResponseEntity<Page<DialogDto>> getDialogs(Pageable page){
        return ResponseEntity.ok(dialogService.getDialogs(page));
    }

    @Override
    public ResponseEntity<UnreadCountDto> getUnreadDialogs() {
        return ResponseEntity.ok(dialogService.getUnreadDialogsCount());
    }

    @Override
    public ResponseEntity<DialogDto> getDialogByRecipientId(String id) {
        return ResponseEntity.ok(dialogService.getDialogByRecipientId(id));
    }

    @Override
    public ResponseEntity<Page<MessageShortDto>> getMessagesByRecipientId(String recipientId, Pageable page) {
        return ResponseEntity.ok(dialogService.getMessagesByRecipientId(recipientId, page));
    }

    @Override
    public ResponseEntity<String> putDialog(String dialogId) {
        dialogService.markDialogRead(dialogId);
        return ResponseEntity.ok("done");
    }

}
