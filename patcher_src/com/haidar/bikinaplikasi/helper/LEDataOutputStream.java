package com.haidar.bikinaplikasi.helper;

/* JADX INFO: loaded from: classes.dex */
public final class LEDataOutputStream {
    protected java.io.DataOutputStream dos;

    public LEDataOutputStream(java.io.OutputStream outputStream) {
        this.dos = new java.io.DataOutputStream(outputStream);
    }

    public int size() {
        return this.dos.size();
    }

    public final void writeByte(byte b) throws java.io.IOException {
        this.dos.writeByte(b);
    }

    public final void writeChar(char c) throws java.io.IOException {
        this.dos.writeByte(c & 255);
        this.dos.writeByte((c >>> '\b') & 255);
    }

    public final void writeCharArray(char[] cArr) throws java.io.IOException {
        for (char c : cArr) {
            writeChar(c);
        }
    }

    public final void writeFully(byte[] bArr) throws java.io.IOException {
        this.dos.write(bArr, 0, bArr.length);
    }

    public final void writeFully(byte[] bArr, int i, int i2) throws java.io.IOException {
        this.dos.write(bArr, i, i2);
    }

    public final void writeInt(int i) throws java.io.IOException {
        this.dos.writeByte(i & 255);
        this.dos.writeByte((i >>> 8) & 255);
        this.dos.writeByte((i >>> 16) & 255);
        this.dos.writeByte((i >>> 24) & 255);
    }

    public final void writeIntArray(int[] iArr) throws java.io.IOException {
        writeIntArray(iArr, 0, iArr.length);
    }

    public final void writeIntArray(int[] iArr, int i, int i2) throws java.io.IOException {
        while (i < i2) {
            writeInt(iArr[i]);
            i++;
        }
    }

    public final void writeNulEndedString(java.lang.String str, int i, boolean z) throws java.io.IOException {
        char[] charArray = str.toCharArray();
        for (int i2 = 0; i2 < charArray.length && i != 0; i2++) {
            writeChar(charArray[i2]);
            i--;
        }
        if (z) {
            for (int i3 = 0; i3 < i * 2; i3++) {
                this.dos.writeByte(0);
            }
        }
    }

    public final void writeShort(short s) throws java.io.IOException {
        this.dos.writeByte(s & 255);
        this.dos.writeByte((s >>> 8) & 255);
    }
}
