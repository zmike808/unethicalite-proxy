package net.unethicalite.proxy.service;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.unethicalite.proxy.api.IsaacCipher;
import net.unethicalite.proxy.api.Session;
import net.unethicalite.proxy.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {
    private final SessionRepository sessionRepository;

    public void connect(SocketIOClient client, int[] isaacSeed) {
        IsaacCipher writerIsaac = new IsaacCipher(isaacSeed);
        int[] bufferSeed = new int[4];
        for (int i = 0; i < 4; ++i) {
            bufferSeed[i] = isaacSeed[i] + 50;
        }

        IsaacCipher bufferIsaac = new IsaacCipher(bufferSeed);

        sessionRepository.addSession(client.getSessionId(), new Session(writerIsaac, bufferIsaac));

        log.debug("User {} connected", client.getSessionId());
    }

    public void disconnect(SocketIOClient client) {
        sessionRepository.removeSession(client.getSessionId());
    }

    public Session getSession(UUID sessionId) {
        return sessionRepository.getSession(sessionId)
                .orElseThrow(() -> new IllegalStateException("No session, please relog."));
    }
}
