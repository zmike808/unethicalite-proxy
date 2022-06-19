package net.unethicalite.proxy.service;

import lombok.RequiredArgsConstructor;
import net.unethicalite.proxy.api.Buffer;
import net.unethicalite.proxy.api.Huffman;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StringService {
    private static final char[] SPECIAL_CHARACTERS;

    private final Huffman huffman;

    static {
        SPECIAL_CHARACTERS = new char[]{'€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000', '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'};
    }

    public String decompressBuffer(Buffer buffer) {
        String text;
        try {
            int textLength = buffer.readUShortSmart();
            if (textLength > 32767) {
                textLength = 32767;
            }

            byte[] payload = new byte[textLength];
            buffer.setOffset(buffer.getOffset() + huffman.decompress(buffer.getArray(), buffer.getOffset(), payload, 0, textLength));
            text = decodeStringCp1252(payload, 0, textLength);
        } catch (Exception ex) {
            text = "Cabbage";
        }

        return text;
    }

    public static String decodeStringCp1252(byte[] buffer, int offset, int textLength) {
        char[] payload = new char[textLength];
        int index = 0;

        for (int i = 0; i < textLength; ++i) {
            int currentByte = buffer[i + offset] & 255;
            if (currentByte != 0) {
                if (currentByte >= 128 && currentByte < 160) {
                    char specialCharacter = SPECIAL_CHARACTERS[currentByte - 128];
                    if (specialCharacter == 0) {
                        specialCharacter = '?';
                    }

                    currentByte = specialCharacter;
                }

                payload[index++] = (char) currentByte;
            }
        }

        return new String(payload, 0, index);
    }

    public static int encodeStringCp1252(CharSequence text, int offset, int length, byte[] buffer, int bufferOffset) {
        int textLength = length - offset;

        for (int i = 0; i < textLength; ++i) {
            char character = text.charAt(i + offset);
            if (character > 0 && character < 128 || character >= 160 && character <= 255) {
                buffer[i + bufferOffset] = (byte) character;
            } else if (character == 8364) {
                buffer[i + bufferOffset] = -128;
            } else if (character == 8218) {
                buffer[i + bufferOffset] = -126;
            } else if (character == 402) {
                buffer[i + bufferOffset] = -125;
            } else if (character == 8222) {
                buffer[i + bufferOffset] = -124;
            } else if (character == 8230) {
                buffer[i + bufferOffset] = -123;
            } else if (character == 8224) {
                buffer[i + bufferOffset] = -122;
            } else if (character == 8225) {
                buffer[i + bufferOffset] = -121;
            } else if (character == 710) {
                buffer[i + bufferOffset] = -120;
            } else if (character == 8240) {
                buffer[i + bufferOffset] = -119;
            } else if (character == 352) {
                buffer[i + bufferOffset] = -118;
            } else if (character == 8249) {
                buffer[i + bufferOffset] = -117;
            } else if (character == 338) {
                buffer[i + bufferOffset] = -116;
            } else if (character == 381) {
                buffer[i + bufferOffset] = -114;
            } else if (character == 8216) {
                buffer[i + bufferOffset] = -111;
            } else if (character == 8217) {
                buffer[i + bufferOffset] = -110;
            } else if (character == 8220) {
                buffer[i + bufferOffset] = -109;
            } else if (character == 8221) {
                buffer[i + bufferOffset] = -108;
            } else if (character == 8226) {
                buffer[i + bufferOffset] = -107;
            } else if (character == 8211) {
                buffer[i + bufferOffset] = -106;
            } else if (character == 8212) {
                buffer[i + bufferOffset] = -105;
            } else if (character == 732) {
                buffer[i + bufferOffset] = -104;
            } else if (character == 8482) {
                buffer[i + bufferOffset] = -103;
            } else if (character == 353) {
                buffer[i + bufferOffset] = -102;
            } else if (character == 8250) {
                buffer[i + bufferOffset] = -101;
            } else if (character == 339) {
                buffer[i + bufferOffset] = -100;
            } else if (character == 382) {
                buffer[i + bufferOffset] = -98;
            } else if (character == 376) {
                buffer[i + bufferOffset] = -97;
            } else {
                buffer[i + bufferOffset] = 63;
            }
        }

        return textLength;
    }
}
