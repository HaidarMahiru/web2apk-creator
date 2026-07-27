package com.haidar.bikinaplikasi.helper;

/* JADX INFO: loaded from: classes.dex */
public class AXmlDecoder {
    private static final int AXML_CHUNK_TYPE = 524291;
    java.io.ByteArrayOutputStream byteOut = new java.io.ByteArrayOutputStream();
    private final com.haidar.bikinaplikasi.helper.LEDataInputStream mIn;
    public com.haidar.bikinaplikasi.helper.StringBlock mTableStrings;

    private AXmlDecoder(com.haidar.bikinaplikasi.helper.LEDataInputStream lEDataInputStream) {
        this.mIn = lEDataInputStream;
    }

    private void checkChunk(int i, int i2) throws java.io.IOException {
        if (i != i2) {
            throw new java.io.IOException(java.lang.String.format("Invalid chunk type: expected=0x%08x, got=0x%08x", java.lang.Integer.valueOf(i2), java.lang.Short.valueOf((short) i)));
        }
    }

    public static void main(java.lang.String[] strArr) throws java.io.IOException {
        com.haidar.bikinaplikasi.helper.AXmlDecoder aXmlDecoder = read(new java.io.FileInputStream("term.xml"));
        java.util.ArrayList arrayList = new java.util.ArrayList();
        aXmlDecoder.mTableStrings.getStrings(arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            java.lang.System.out.println(i + " " + arrayList.get(i));
        }
        aXmlDecoder.write(arrayList, new java.io.FileOutputStream("test.xml"));
    }

    public static com.haidar.bikinaplikasi.helper.AXmlDecoder read(java.io.InputStream inputStream) throws java.io.IOException {
        com.haidar.bikinaplikasi.helper.AXmlDecoder aXmlDecoder = new com.haidar.bikinaplikasi.helper.AXmlDecoder(new com.haidar.bikinaplikasi.helper.LEDataInputStream(inputStream));
        aXmlDecoder.readStrings();
        return aXmlDecoder;
    }

    private void readStrings() throws java.io.IOException {
        checkChunk(this.mIn.readInt(), AXML_CHUNK_TYPE);
        this.mIn.readInt();
        this.mTableStrings = com.haidar.bikinaplikasi.helper.StringBlock.read(this.mIn);
        byte[] bArr = new byte[2048];
        while (true) {
            int i = this.mIn.read(bArr, 0, 2048);
            if (i == -1) {
                return;
            } else {
                this.byteOut.write(bArr, 0, i);
            }
        }
    }

    public void write(java.util.List<java.lang.String> list, com.haidar.bikinaplikasi.helper.LEDataOutputStream lEDataOutputStream) throws java.io.IOException {
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        com.haidar.bikinaplikasi.helper.LEDataOutputStream lEDataOutputStream2 = new com.haidar.bikinaplikasi.helper.LEDataOutputStream(byteArrayOutputStream);
        this.mTableStrings.write(list, lEDataOutputStream2);
        lEDataOutputStream2.writeFully(this.byteOut.toByteArray());
        lEDataOutputStream.writeInt(AXML_CHUNK_TYPE);
        lEDataOutputStream.writeInt(byteArrayOutputStream.size() + 8);
        lEDataOutputStream.writeFully(byteArrayOutputStream.toByteArray());
    }

    public void write(java.util.List<java.lang.String> list, java.io.OutputStream outputStream) throws java.io.IOException {
        write(list, new com.haidar.bikinaplikasi.helper.LEDataOutputStream(outputStream));
    }
}
