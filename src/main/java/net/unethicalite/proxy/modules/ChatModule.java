package net.unethicalite.proxy.modules;

import com.corundumstudio.socketio.SocketIOServer;
import net.unethicalite.proxy.listeners.ClanChatMessageListener;
import org.springframework.stereotype.Component;

@Component
public class ChatModule {
    public ChatModule(SocketIOServer server, ClanChatMessageListener clanChatMessageListener) {
        server.addEventListener("clanChatMessage", byte[].class, clanChatMessageListener);
    }
}
