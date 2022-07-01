package net.unethicalite.proxy.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketConfig {
    @Bean
    public SocketIOServer server() {
        var configuration = new com.corundumstudio.socketio.Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(9991);
        return new SocketIOServer(configuration);
    }
}
