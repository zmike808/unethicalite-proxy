package net.unethicalite.proxy.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import net.unethicalite.proxy.api.Buffer;
import net.unethicalite.proxy.service.StringService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClanChatMessageListener implements ByteListener {
    private final StringService stringService;

    @Override
    public void onData(SocketIOClient client, byte[] data, AckRequest ackSender) {
        Buffer buffer = new Buffer(data);
        String sender = buffer.readStringCp1252NullTerminated();
        buffer.readLong();
        buffer.readUnsignedShort();
        buffer.readMedium();
        buffer.readUnsignedByte();
        String text = stringService.decompressBuffer(buffer);

        System.out.println(sender + ": " + text);
    }
}
