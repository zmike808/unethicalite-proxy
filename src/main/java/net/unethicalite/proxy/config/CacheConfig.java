package net.unethicalite.proxy.config;

import net.runelite.cache.fs.Store;
import net.unethicalite.proxy.api.Huffman;
import net.unethicalite.proxy.cache.HuffmanDumper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class CacheConfig {
    @Bean
    public Huffman huffman() throws IOException {
        try (Store store = new Store(new File(System.getProperty("user.home") + "/jagexcache/oldschool/LIVE"))) {
            store.load();
            HuffmanDumper dumper = new HuffmanDumper(store);
            return new Huffman(dumper.load());
        }
    }
}
