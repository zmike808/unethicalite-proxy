package net.unethicalite.proxy.api;

import lombok.Getter;
import lombok.Setter;
import net.unethicalite.proxy.service.TextService;

import java.math.BigInteger;

public class Buffer {
    @Getter
    protected final byte[] array;
    @Getter
    protected final IsaacCipher isaacCipher;

    @Setter
    @Getter
    protected int offset;

    public Buffer(byte[] var1, IsaacCipher isaacCipher) {
        this.array = var1;
        this.offset = 0;
        this.isaacCipher = isaacCipher;
    }

    public void writeByte(int var1) {
        this.array[++this.offset - 1] = (byte) var1;
    }

    public void writeShort(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
        this.array[++this.offset - 1] = (byte) var1;
    }

    public void writeMedium(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 16);
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
        this.array[++this.offset - 1] = (byte) var1;
    }

    public void writeInt(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 24);
        this.array[++this.offset - 1] = (byte) (var1 >> 16);
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
        this.array[++this.offset - 1] = (byte) var1;
    }

    public void writeLongMedium(long var1) {
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 40));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 32));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 24));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 16));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 8));
        this.array[++this.offset - 1] = (byte) ((int) var1);
    }

    public void writeLong(long var1) {
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 56));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 48));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 40));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 32));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 24));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 16));
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 8));
        this.array[++this.offset - 1] = (byte) ((int) var1);
    }

    public void writeBoolean(boolean var1) {
        this.writeByte(var1 ? 1 : 0);
    }

    public void writeStringCp1252NullTerminated(String var1) {
        int var2 = var1.indexOf(0);
        if (var2 >= 0) {
            throw new IllegalArgumentException("");
        } else {
            this.offset += TextService.encodeStringCp1252(var1, 0, var1.length(), this.array, this.offset);
            this.array[++this.offset - 1] = 0;
        }
    }

    public void writeStringCp1252NullCircumfixed(String var1) {
        int var2 = var1.indexOf(0);
        if (var2 >= 0) {
            throw new IllegalArgumentException("");
        } else {
            this.array[++this.offset - 1] = 0;
            this.offset += TextService.encodeStringCp1252(var1, 0, var1.length(), this.array, this.offset);
            this.array[++this.offset - 1] = 0;
        }
    }

    public void writeBytes(byte[] var1, int var2, int var3) {
        for (int var4 = var2; var4 < var3 + var2; ++var4) {
            this.array[++this.offset - 1] = var1[var4];
        }
    }

    public void writeBuffer(Buffer var1) {
        this.writeBytes(var1.array, 0, var1.offset);
    }

    public void writeLengthInt(int var1) {
        if (var1 < 0) {
            throw new IllegalArgumentException();
        } else {
            this.array[this.offset - var1 - 4] = (byte) (var1 >> 24);
            this.array[this.offset - var1 - 3] = (byte) (var1 >> 16);
            this.array[this.offset - var1 - 2] = (byte) (var1 >> 8);
            this.array[this.offset - var1 - 1] = (byte) var1;
        }
    }

    public void writeLengthShort(int var1) {
        if (var1 >= 0 && var1 <= 65535) {
            this.array[this.offset - var1 - 2] = (byte) (var1 >> 8);
            this.array[this.offset - var1 - 1] = (byte) var1;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void writeLengthByte(int var1) {
        if (var1 >= 0 && var1 <= 255) {
            this.array[this.offset - var1 - 1] = (byte) var1;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void writeSmartByteShort(int var1) {
        if (var1 >= 0 && var1 < 128) {
            this.writeByte(var1);
        } else if (var1 >= 0 && var1 < 32768) {
            this.writeShort(var1 + 32768);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void writeVarInt(int var1) {
        if ((var1 & -128) != 0) {
            if ((var1 & -16384) != 0) {
                if ((var1 & -2097152) != 0) {
                    if ((var1 & -268435456) != 0) {
                        this.writeByte(var1 >>> 28 | 128);
                    }

                    this.writeByte(var1 >>> 21 | 128);
                }

                this.writeByte(var1 >>> 14 | 128);
            }

            this.writeByte(var1 >>> 7 | 128);
        }

        this.writeByte(var1 & 127);
    }

    public int readUnsignedByte() {
        return this.array[++this.offset - 1] & 255;
    }

    public byte readByte() {
        return this.array[++this.offset - 1];
    }

    public int readUnsignedShort() {
        this.offset += 2;
        return (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 2] & 255) << 8);
    }

    public int readShort() {
        this.offset += 2;
        int var1 = (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 2] & 255) << 8);
        if (var1 > 32767) {
            var1 -= 65536;
        }

        return var1;
    }

    public int readMedium() {
        this.offset += 3;
        return ((this.array[this.offset - 3] & 255) << 16) + (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 2] & 255) << 8);
    }

    public int readInt() {
        this.offset += 4;
        return ((this.array[this.offset - 3] & 255) << 16) + (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 2] & 255) << 8) + ((this.array[this.offset - 4] & 255) << 24);
    }

    public long readLong() {
        long var1 = (long) this.readInt() & 4294967295L;
        long var3 = (long) this.readInt() & 4294967295L;
        return var3 + (var1 << 32);
    }

    public boolean readBoolean() {
        return (this.readUnsignedByte() & 1) == 1;
    }

    public String readStringCp1252NullTerminatedOrNull() {
        if (this.array[this.offset] == 0) {
            ++this.offset;
            return null;
        } else {
            return this.readStringCp1252NullTerminated();
        }
    }

    public String readStringCp1252NullTerminated() {
        int var1 = this.offset;

        while (this.array[++this.offset - 1] != 0) {
        }

        int var2 = this.offset - var1 - 1;
        return var2 == 0 ? "" : TextService.decodeStringCp1252(this.array, var1, var2);
    }

    public String readStringCp1252NullCircumfixed() {
        byte var1 = this.array[++this.offset - 1];
        if (var1 != 0) {
            throw new IllegalStateException("");
        } else {
            int var2 = this.offset;

            while (this.array[++this.offset - 1] != 0) {
            }

            int var3 = this.offset - var2 - 1;
            return var3 == 0 ? "" : TextService.decodeStringCp1252(this.array, var2, var3);
        }
    }

    public void readBytes(byte[] var1, int var2, int var3) {
        for (int var4 = var2; var4 < var3 + var2; ++var4) {
            var1[var4] = this.array[++this.offset - 1];
        }
    }

    public int readShortSmart() {
        int var1 = this.array[this.offset] & 255;
        return var1 < 128 ? this.readUnsignedByte() - 64 : this.readUnsignedShort() - 49152;
    }

    public int readUShortSmart() {
        int var1 = this.array[this.offset] & 255;
        return var1 < 128 ? this.readUnsignedByte() : this.readUnsignedShort() - 32768;
    }

    public int readIncrSmallSmart() {
        int var1 = 0;

        int var2;
        for (var2 = this.readUShortSmart(); var2 == 32767; var2 = this.readUShortSmart()) {
            var1 += 32767;
        }

        var1 += var2;
        return var1;
    }

    public int readLargeSmart() {
        return this.array[this.offset] < 0 ? this.readInt() & Integer.MAX_VALUE : this.readUnsignedShort();
    }

    public int readNullableLargeSmart() {
        if (this.array[this.offset] < 0) {
            return this.readInt() & Integer.MAX_VALUE;
        } else {
            int var1 = this.readUnsignedShort();
            return var1 == 32767 ? -1 : var1;
        }
    }

    public int readVarInt() {
        byte var1 = this.array[++this.offset - 1];

        int var2;
        for (var2 = 0; var1 < 0; var1 = this.array[++this.offset - 1]) {
            var2 = (var2 | var1 & 127) << 7;
        }

        return var2 | var1;
    }

    public void xteaEncryptAll(int[] var1) {
        int var2 = this.offset / 8;
        this.offset = 0;

        for (int var3 = 0; var3 < var2; ++var3) {
            int var4 = this.readInt();
            int var5 = this.readInt();
            int var6 = 0;
            int var7 = -1640531527;

            for (int var8 = 32; var8-- > 0; var5 += var4 + (var4 << 4 ^ var4 >>> 5) ^ var1[var6 >>> 11 & 3] + var6) {
                var4 += var5 + (var5 << 4 ^ var5 >>> 5) ^ var6 + var1[var6 & 3];
                var6 += var7;
            }

            this.offset -= 8;
            this.writeInt(var4);
            this.writeInt(var5);
        }
    }

    public void xteaDecryptAll(int[] var1) {
        int var2 = this.offset / 8;
        this.offset = 0;

        for (int var3 = 0; var3 < var2; ++var3) {
            int var4 = this.readInt();
            int var5 = this.readInt();
            int var6 = -957401312;
            int var7 = -1640531527;

            for (int var8 = 32; var8-- > 0; var4 -= var5 + (var5 << 4 ^ var5 >>> 5) ^ var6 + var1[var6 & 3]) {
                var5 -= var4 + (var4 << 4 ^ var4 >>> 5) ^ var1[var6 >>> 11 & 3] + var6;
                var6 -= var7;
            }

            this.offset -= 8;
            this.writeInt(var4);
            this.writeInt(var5);
        }

    }

    public void xteaEncrypt(int[] var1, int var2, int var3) {
        int var4 = this.offset;
        this.offset = var2;
        int var5 = (var3 - var2) / 8;

        for (int var6 = 0; var6 < var5; ++var6) {
            int var7 = this.readInt();
            int var8 = this.readInt();
            int var9 = 0;
            int var10 = -1640531527;

            for (int var11 = 32; var11-- > 0; var8 += var7 + (var7 << 4 ^ var7 >>> 5) ^ var1[var9 >>> 11 & 3] + var9) {
                var7 += var8 + (var8 << 4 ^ var8 >>> 5) ^ var9 + var1[var9 & 3];
                var9 += var10;
            }

            this.offset -= 8;
            this.writeInt(var7);
            this.writeInt(var8);
        }

        this.offset = var4;
    }

    public void xteaDecrypt(int[] var1, int var2, int var3) {
        int var4 = this.offset;
        this.offset = var2;
        int var5 = (var3 - var2) / 8;

        for (int var6 = 0; var6 < var5; ++var6) {
            int var7 = this.readInt();
            int var8 = this.readInt();
            int var9 = -957401312;
            int var10 = -1640531527;

            for (int var11 = 32; var11-- > 0; var7 -= var8 + (var8 << 4 ^ var8 >>> 5) ^ var9 + var1[var9 & 3]) {
                var8 -= var7 + (var7 << 4 ^ var7 >>> 5) ^ var1[var9 >>> 11 & 3] + var9;
                var9 -= var10;
            }

            this.offset -= 8;
            this.writeInt(var7);
            this.writeInt(var8);
        }

        this.offset = var4;
    }

    public void encryptRsa(BigInteger var1, BigInteger var2) {
        int var3 = this.offset;
        this.offset = 0;
        byte[] var4 = new byte[var3];
        this.readBytes(var4, 0, var3);
        BigInteger var5 = new BigInteger(var4);
        BigInteger var6 = var5.modPow(var1, var2);
        byte[] var7 = var6.toByteArray();
        this.offset = 0;
        this.writeShort(var7.length);
        this.writeBytes(var7, 0, var7.length);
    }

    public void writeByteAdd(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 + 128);
    }

    public void writeByteNeg(int var1) {
        this.array[++this.offset - 1] = (byte) (0 - var1);
    }

    public void writeByteSub(int var1) {
        this.array[++this.offset - 1] = (byte) (128 - var1);
    }

    public int readUnsignedByteAdd() {
        return this.array[++this.offset - 1] - 128 & 255;
    }

    public int readUnsignedByteNeg() {
        return 0 - this.array[++this.offset - 1] & 255;
    }

    public int readUnsignedByteSub() {
        return 128 - this.array[++this.offset - 1] & 255;
    }

    public byte readByteAdd() {
        return (byte) (this.array[++this.offset - 1] - 128);
    }

    public byte readByteNeg() {
        return (byte) (0 - this.array[++this.offset - 1]);
    }

    public byte readByteSub() {
        return (byte) (128 - this.array[++this.offset - 1]);
    }

    public void writeShortLE(int var1) {
        this.array[++this.offset - 1] = (byte) var1;
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
    }

    public void writeShortAdd(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
        this.array[++this.offset - 1] = (byte) (var1 + 128);
    }

    public void writeShortAddLE(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 + 128);
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
    }

    public int readUnsignedShortLE() {
        this.offset += 2;
        return ((this.array[this.offset - 1] & 255) << 8) + (this.array[this.offset - 2] & 255);
    }

    public int readUnsignedShortAdd() {
        this.offset += 2;
        return (this.array[this.offset - 1] - 128 & 255) + ((this.array[this.offset - 2] & 255) << 8);
    }

    public int readUnsignedShortAddLE() {
        this.offset += 2;
        return ((this.array[this.offset - 1] & 255) << 8) + (this.array[this.offset - 2] - 128 & 255);
    }

    public int readShortAdd() {
        this.offset += 2;
        int var1 = (this.array[this.offset - 1] - 128 & 255) + ((this.array[this.offset - 2] & 255) << 8);
        if (var1 > 32767) {
            var1 -= 65536;
        }

        return var1;
    }

    public int readShortAddLE() {
        this.offset += 2;
        int var1 = ((this.array[this.offset - 1] & 255) << 8) + (this.array[this.offset - 2] - 128 & 255);
        if (var1 > 32767) {
            var1 -= 65536;
        }

        return var1;
    }

    public void writeMediumLME(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
        this.array[++this.offset - 1] = (byte) (var1 >> 16);
        this.array[++this.offset - 1] = (byte) var1;
    }

    public int readUnsignedMediumRME() {
        this.offset += 3;
        return ((this.array[this.offset - 1] & 255) << 8) + ((this.array[this.offset - 3] & 255) << 16) + (this.array[this.offset - 2] & 255);
    }

    public void writeIntLE(int var1) {
        this.array[++this.offset - 1] = (byte) var1;
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
        this.array[++this.offset - 1] = (byte) (var1 >> 16);
        this.array[++this.offset - 1] = (byte) (var1 >> 24);
    }

    public void writeIntIME(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
        this.array[++this.offset - 1] = (byte) var1;
        this.array[++this.offset - 1] = (byte) (var1 >> 24);
        this.array[++this.offset - 1] = (byte) (var1 >> 16);
    }

    public void writeIntME(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 16);
        this.array[++this.offset - 1] = (byte) (var1 >> 24);
        this.array[++this.offset - 1] = (byte) var1;
        this.array[++this.offset - 1] = (byte) (var1 >> 8);
    }

    public int readUnsignedIntLE() {
        this.offset += 4;
        return (this.array[this.offset - 4] & 255) + ((this.array[this.offset - 3] & 255) << 8) + ((this.array[this.offset - 2] & 255) << 16) + ((this.array[this.offset - 1] & 255) << 24);
    }

    public int readUnsignedIntIME() {
        this.offset += 4;
        return ((this.array[this.offset - 2] & 255) << 24) + ((this.array[this.offset - 4] & 255) << 8) + (this.array[this.offset - 3] & 255) + ((this.array[this.offset - 1] & 255) << 16);
    }

    public int readUnsignedIntME() {
        this.offset += 4;
        return ((this.array[this.offset - 1] & 255) << 8) + ((this.array[this.offset - 4] & 255) << 16) + (this.array[this.offset - 2] & 255) + ((this.array[this.offset - 3] & 255) << 24);
    }

    public void readBytesReversed(byte[] var1, int var2, int var3) {
        for (int var4 = var3 + var2 - 1; var4 >= var2; --var4) {
            var1[var4] = this.array[++this.offset - 1];
        }
    }

    public void readBytesAdd(byte[] var1, int var2, int var3) {
        for (int var4 = var2; var4 < var3 + var2; ++var4) {
            var1[var4] = (byte) (this.array[++this.offset - 1] - 128);
        }
    }
}
