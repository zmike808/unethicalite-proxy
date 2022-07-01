package net.unethicalite.proxy.listeners;

import net.unethicalite.proxy.api.Buffer;
import net.unethicalite.proxy.service.SessionService;
import net.unethicalite.proxy.service.TextService;
import org.springframework.stereotype.Component;

@Component
public class ClanChatMessageListener extends ByteListener {
    private final TextService textService;

    public ClanChatMessageListener(TextService textService, SessionService sessionService) {
        super(sessionService);
        this.textService = textService;
    }

    @Override
    public void notify(Buffer buffer) {
        String sender = buffer.readStringCp1252NullTerminated();
        buffer.readLong();
        buffer.readUnsignedShort();
        buffer.readMedium();
        buffer.readUnsignedByte();
        String text = textService.decompressBuffer(buffer);

        System.out.println(sender + ": " + text);
    }
}
