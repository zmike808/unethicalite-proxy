package net.unethicalite.proxy.repository;

import com.corundumstudio.socketio.SocketIOClient;
import net.unethicalite.proxy.api.Session;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionRepository {
    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    public Optional<Session> getSession(UUID uuid) {
        return Optional.ofNullable(sessions.get(uuid));
    }

    public Optional<Session> getSession(SocketIOClient client) {
        return getSession(client.getSessionId());
    }

    public void addSession(UUID sessionId, Session session) {
        sessions.put(sessionId, session);
    }

    public void removeSession(UUID uuid) {
        sessions.remove(uuid);
    }
}
