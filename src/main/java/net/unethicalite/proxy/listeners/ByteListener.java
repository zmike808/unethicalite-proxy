package net.unethicalite.proxy.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import net.unethicalite.proxy.api.Buffer;
import net.unethicalite.proxy.api.PacketBuffer;
import net.unethicalite.proxy.api.Session;
import net.unethicalite.proxy.service.SessionService;

import java.util.Arrays;

public abstract class ByteListener implements DataListener<byte[]> {
    protected final SessionService sessionService;
    private final int packetId;

    protected ByteListener(SessionService sessionService, int packetId) {
        this.sessionService = sessionService;
        this.packetId = packetId;
    }

    @Override
    public void onData(SocketIOClient client, byte[] data, AckRequest ackSender) {
        Session session = sessionService.getSession(client.getSessionId());
        int id = data[0];
        if (id == packetId) {
            notify(new PacketBuffer(Arrays.copyOfRange(data, 1, data.length), session.getBufferIsaac()));
        }
    }

    public abstract void notify(Buffer buffer);
}
