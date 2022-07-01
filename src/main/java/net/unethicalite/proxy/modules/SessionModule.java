package net.unethicalite.proxy.modules;

import com.corundumstudio.socketio.SocketIOServer;
import net.unethicalite.proxy.service.SessionService;
import org.springframework.stereotype.Component;

@Component
public class SessionModule {
    public SessionModule(SocketIOServer server, SessionService sessionService) {
        server.addEventListener("login", int[].class, (client, data, ackSender) -> sessionService.connect(client, data));
        server.addEventListener("disconnect", void.class, (client, data, ackSender) -> sessionService.disconnect(client));
    }
}
