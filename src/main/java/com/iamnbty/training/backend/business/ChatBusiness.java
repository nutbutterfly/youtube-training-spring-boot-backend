package com.iamnbty.training.backend.business;

import com.iamnbty.training.backend.exception.BaseException;
import com.iamnbty.training.backend.exception.ChatException;
import com.iamnbty.training.backend.model.ChatMessage;
import com.iamnbty.training.backend.model.ChatMessageRequest;
import com.iamnbty.training.backend.util.SecurityUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatBusiness {

    private final SimpMessagingTemplate template;

    public ChatBusiness(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void post(ChatMessageRequest request) throws BaseException {
        Optional<String> opt = SecurityUtil.getCurrentUserId();

        if (opt.isEmpty()) {
            throw ChatException.accessDenied();
        }

        // TODO: validate message

        final String destination = "/topic/chat";

        ChatMessage payload = new ChatMessage();
        payload.setFrom(opt.get());
        payload.setMessage(request.getMessage());

        template.convertAndSend(destination, payload);
    }

}
