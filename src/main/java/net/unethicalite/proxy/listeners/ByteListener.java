package net.unethicalite.proxy.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import net.unethicalite.proxy.api.Buffer;
import net.unethicalite.proxy.api.PacketBuffer;
import net.unethicalite.proxy.api.Session;
import net.unethicalite.proxy.service.SessionService;

public abstract class ByteListener implements DataListener<byte[]> {
    protected final SessionService sessionService;

    protected ByteListener(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void onData(SocketIOClient client, byte[] data, AckRequest ackSender) {
        Session session = sessionService.getSession(client.getSessionId());
        notify(new PacketBuffer(data, session.getBufferIsaac()));
    }

    public abstract void notify(Buffer buffer);
}
