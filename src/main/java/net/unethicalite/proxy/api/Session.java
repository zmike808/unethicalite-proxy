package net.unethicalite.proxy.api;

import lombok.Value;

@Value
public class Session {
    IsaacCipher writerIsaac;
    IsaacCipher bufferIsaac;
}
