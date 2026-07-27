package com.haidar.bikinaplikasi.helper;

/* JADX INFO: loaded from: classes.dex */
public final class LEDataInputStream implements java.io.DataInput {
    protected final java.io.DataInputStream dis;
    int end;
    protected final java.io.InputStream is;
    int s;
    protected final byte[] work = new byte[8];

    public LEDataInputStream(java.io.InputStream inputStream) {
        this.is = inputStream;
        this.dis = new java.io.DataInputStream(inputStream);
    }

    public int read(byte[] bArr, int i, int i2) throws java.io.IOException {
        return this.dis.read(bArr, i, i2);
    }

    @Override // java.io.DataInput
    public final boolean readBoolean() throws java.io.IOException {
        return this.dis.readBoolean();
    }

    @Override // java.io.DataInput
    public final byte readByte() throws java.io.IOException {
        return this.dis.readByte();
    }

    @Override // java.io.DataInput
    public final char readChar() throws java.io.IOException {
        this.dis.readFully(this.work, 0, 2);
        return (char) (((this.work[1] & 255) << 8) | (this.work[0] & 255));
    }

    @Override // java.io.DataInput
    public final double readDouble() throws java.io.IOException {
        return java.lang.Double.longBitsToDouble(readLong());
    }

    @Override // java.io.DataInput
    public final float readFloat() throws java.io.IOException {
        return java.lang.Float.intBitsToFloat(readInt());
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] bArr) throws java.io.IOException {
        this.dis.readFully(bArr, 0, bArr.length);
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] bArr, int i, int i2) throws java.io.IOException {
        this.dis.readFully(bArr, i, i2);
    }

    @Override // java.io.DataInput
    public final int readInt() throws java.io.IOException {
        this.dis.readFully(this.work, 0, 4);
        return (this.work[3] << 24) | ((this.work[2] & 255) << 16) | ((this.work[1] & 255) << 8) | (this.work[0] & 255);
    }

    public int[] readIntArray(int i) throws java.io.IOException {
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = readInt();
        }
        return iArr;
    }

    @Override // java.io.DataInput
    public final java.lang.String readLine() throws java.io.IOException {
        return this.dis.readLine();
    }

    @Override // java.io.DataInput
    public final long readLong() throws java.io.IOException {
        this.dis.readFully(this.work, 0, 8);
        return (this.work[7] << 56) | ((this.work[6] & 255) << 48) | ((this.work[5] & 255) << 40) | ((this.work[4] & 255) << 32) | ((this.work[3] & 255) << 24) | ((this.work[2] & 255) << 16) | ((this.work[1] & 255) << 8) | (this.work[0] & 255);
    }

    public java.lang.String readNulEndedString(int i, boolean z) throws java.io.IOException {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(16);
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 == 0) {
                break;
            }
            short s = readShort();
            this.end += 2;
            if (s == 0) {
                break;
            }
            sb.append((char) s);
        }
        if (z) {
            skipBytes(i * 2);
            this.end += i * 2;
        }
        return sb.toString();
    }

    @Override // java.io.DataInput
    public final short readShort() throws java.io.IOException {
        this.dis.readFully(this.work, 0, 2);
        return (short) (((this.work[1] & 255) << 8) | (this.work[0] & 255));
    }

    @Override // java.io.DataInput
    public final java.lang.String readUTF() throws java.io.IOException {
        return this.dis.readUTF();
    }

    @Override // java.io.DataInput
    public final int readUnsignedByte() throws java.io.IOException {
        return this.dis.readUnsignedByte();
    }

    @Override // java.io.DataInput
    public final int readUnsignedShort() throws java.io.IOException {
        this.dis.readFully(this.work, 0, 2);
        return ((this.work[1] & 255) << 8) | (this.work[0] & 255);
    }

    public int size() {
        return this.end;
    }

    @Override // java.io.DataInput
    public final int skipBytes(int i) throws java.io.IOException {
        return this.dis.skipBytes(i);
    }

    public void skipCheckByte(byte b) throws java.io.IOException {
        byte b2 = readByte();
        if (b2 != b) {
            throw new java.io.IOException(java.lang.String.format("Expected: 0x%08x, got: 0x%08x", java.lang.Byte.valueOf(b), java.lang.Byte.valueOf(b2)));
        }
    }

    public void skipCheckInt(int i) throws java.io.IOException {
        int i2 = readInt();
        if (i2 != i) {
            throw new java.io.IOException(java.lang.String.format("Expected: 0x%08x, got: 0x%08x", java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2)));
        }
    }

    public void skipCheckShort(short s) throws java.io.IOException {
        short s2 = readShort();
        if (s2 != s) {
            throw new java.io.IOException(java.lang.String.format("Expected: 0x%08x, got: 0x%08x", java.lang.Short.valueOf(s), java.lang.Short.valueOf(s2)));
        }
    }

    public void skipInt() throws java.io.IOException {
        skipBytes(4);
    }
}
