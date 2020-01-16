package datastruct.struct;

/**
 * bit map false always false, true maybe true
 * bloom filter基于bit map做k次hash, k's false always false, k's true may be true
 */
public class BitMapADT {

    private byte[] bytes;//backend
    private int nbits;//number of bit

    public BitMapADT(int nbits) {
        this.nbits = nbits;
        this.bytes = new byte[nbits / 8 + 1];
    }

    public void set(int k) {
        //set bit
        if (k > nbits) return;
        int byteIndex = k / nbits;
        int bitIndex = k % nbits;
        //boolean
        bytes[byteIndex] |= (1 << bitIndex);
    }

    public boolean get(int k) {
        //get bit
        if (k > nbits) return false;
        int byteIndex = k / nbits;
        int bitIndex = k % nbits;
        //boolean
        return (bytes[byteIndex] & (1 << bitIndex)) != 0;
    }

    public static void main(String[] args) {
        BitMapADT bitMap = new BitMapADT(10);
        for (int i = 1; i <= 5; i++) {
            bitMap.set(i);
        }
        System.out.println(bitMap.get(1));
        System.out.println(bitMap.get(5));
        System.out.println(bitMap.get(6));
        System.out.println(bitMap.get(10));
    }
}
