package net.unethicalite.proxy.api;

public class PacketBuffer extends Buffer {
    private static final int[] MERSENNE_PRIME;
    private int bitIndex;

    public PacketBuffer(byte[] payload) {
        super(payload);
    }

    static {
        MERSENNE_PRIME = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1};
    }

    public void importIndex() {
        this.bitIndex = super.offset * 8;
    }

    public int readBits(int amount) {
        int var2 = this.bitIndex >> 3;
        int var3 = 8 - (this.bitIndex & 7);
        int var4 = 0;

        for (this.bitIndex += amount; amount > var3; var3 = 8) {
            var4 += (super.array[var2++] & MERSENNE_PRIME[var3]) << amount - var3;
            amount -= var3;
        }

        if (var3 == amount) {
            var4 += super.array[var2] & MERSENNE_PRIME[var3];
        } else {
            var4 += super.array[var2] >> var3 - amount & MERSENNE_PRIME[amount];
        }

        return var4;
    }

    public void exportIndex() {
        super.offset = (this.bitIndex + 7) / 8;
    }

    public int bitsRemaining(int var1) {
        return var1 * 8 - this.bitIndex;
    }
}
