package com.muhfau.bikinaplikasi.helper;

/* JADX INFO: loaded from: classes.dex */
public final class FileUtil {
    private FileUtil() {
    }

    public static byte[] readFile(java.io.File file) throws java.io.IOException {
        return readFile(file, 0, -1);
    }

    public static byte[] readFile(java.io.File file, int i, int i2) throws java.io.IOException {
        if (!file.exists()) {
            throw new java.lang.RuntimeException(file + ": file not found");
        }
        if (!file.isFile()) {
            throw new java.lang.RuntimeException(file + ": not a file");
        }
        if (!file.canRead()) {
            throw new java.lang.RuntimeException(file + ": file not readable");
        }
        long length = file.length();
        int i3 = (int) length;
        if (i3 != length) {
            throw new java.lang.RuntimeException(file + ": file too long");
        }
        if (i2 == -1) {
            i2 = i3 - i;
        }
        if (i + i2 > i3) {
            throw new java.lang.RuntimeException(file + ": file too short");
        }
        java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
        int i4 = i;
        while (i4 > 0) {
            long jSkip = fileInputStream.skip(i4);
            if (jSkip == -1) {
                throw new java.lang.RuntimeException(file + ": unexpected EOF");
            }
            i4 = (int) (((long) i4) - jSkip);
        }
        byte[] stream = readStream(fileInputStream, i2);
        fileInputStream.close();
        return stream;
    }

    public static byte[] readFile(java.lang.String str) throws java.io.IOException {
        return readFile(new java.io.File(str));
    }

    public static byte[] readStream(java.io.InputStream inputStream, int i) throws java.io.IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i > 0) {
            int i3 = inputStream.read(bArr, i2, i);
            if (i3 == -1) {
                throw new java.lang.RuntimeException("unexpected EOF");
            }
            i2 += i3;
            i -= i3;
        }
        return bArr;
    }
}
