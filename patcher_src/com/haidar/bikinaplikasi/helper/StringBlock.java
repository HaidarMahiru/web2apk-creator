package com.haidar.bikinaplikasi.helper;

/* JADX INFO: loaded from: classes.dex */
public class StringBlock {
    public static final int CHUNK_STRINGBLOCK = 1835009;
    public static final int IS_UTF8 = 256;
    private static final java.nio.charset.CharsetDecoder UTF16LE_DECODER = java.nio.charset.Charset.forName("UTF-16LE").newDecoder();
    private static final java.nio.charset.CharsetEncoder UTF16LE_ENCODER = java.nio.charset.Charset.forName("UTF-16LE").newEncoder();
    private static final java.nio.charset.CharsetDecoder UTF8_DECODER = java.nio.charset.Charset.forName("UTF-8").newDecoder();
    private static final java.nio.charset.CharsetEncoder UTF8_ENCODER = java.nio.charset.Charset.forName("UTF-8").newEncoder();
    private int chunkSize;
    private int flags;
    private boolean m_isUTF8;
    private int[] m_stringOffsets;
    byte[] m_strings;
    private int[] m_styleOffsets;
    private int[] m_styles;
    private int stringsOffset;
    private int styleOffsetCount;
    private int stylesOffset;

    private java.lang.String decodeString(int i, int i2) {
        try {
            return (this.m_isUTF8 ? UTF8_DECODER : UTF16LE_DECODER).decode(java.nio.ByteBuffer.wrap(this.m_strings, i, i2)).toString();
        } catch (java.nio.charset.CharacterCodingException e) {
            return null;
        }
    }

    private static final int getShort(byte[] bArr, int i) {
        return ((bArr[i + 1] & 255) << 8) | (bArr[i] & 255);
    }

    private int[] getStyle(int i) {
        int[] iArr = null;
        if (this.m_styleOffsets != null && this.m_styles != null && i < this.m_styleOffsets.length) {
            int i2 = this.m_styleOffsets[i] / 4;
            int i3 = 0;
            for (int i4 = i2; i4 < this.m_styles.length && this.m_styles[i4] != -1; i4++) {
                i3++;
            }
            if (i3 != 0 && i3 % 3 == 0) {
                iArr = new int[i3];
                int i5 = 0;
                for (int i6 = i2; i6 < this.m_styles.length && this.m_styles[i6] != -1; i6++) {
                    iArr[i5] = this.m_styles[i6];
                    i5++;
                }
            }
        }
        return iArr;
    }

    private static final int[] getVarint(byte[] bArr, int i) {
        byte b = bArr[i];
        boolean z = (b & 128) != 0;
        int i2 = b & 127;
        return !z ? new int[]{i2, 1} : new int[]{(i2 << 8) | (bArr[i + 1] & 255), 2};
    }

    private void outputStyleTag(java.lang.String str, java.lang.StringBuilder sb, boolean z) {
        java.lang.String strSubstring;
        sb.append('<');
        if (z) {
            sb.append('/');
        }
        int iIndexOf = str.indexOf(59);
        if (iIndexOf == -1) {
            sb.append(str);
        } else {
            sb.append(str.substring(0, iIndexOf));
            if (!z) {
                boolean z2 = true;
                while (z2) {
                    int iIndexOf2 = str.indexOf(61, iIndexOf + 1);
                    sb.append(' ').append(str.substring(iIndexOf + 1, iIndexOf2)).append("=\"");
                    iIndexOf = str.indexOf(59, iIndexOf2 + 1);
                    if (iIndexOf != -1) {
                        strSubstring = str.substring(iIndexOf2 + 1, iIndexOf);
                    } else {
                        z2 = false;
                        strSubstring = str.substring(iIndexOf2 + 1);
                    }
                    sb.append(strSubstring).append('\"');
                }
            }
        }
        sb.append('>');
    }

    public static com.haidar.bikinaplikasi.helper.StringBlock read(com.haidar.bikinaplikasi.helper.LEDataInputStream lEDataInputStream) throws java.io.IOException {
        lEDataInputStream.skipCheckInt(CHUNK_STRINGBLOCK);
        com.haidar.bikinaplikasi.helper.StringBlock stringBlock = new com.haidar.bikinaplikasi.helper.StringBlock();
        int i = lEDataInputStream.readInt();
        stringBlock.chunkSize = i;
        java.lang.System.out.println("chunkSize " + i);
        int i2 = lEDataInputStream.readInt();
        java.lang.System.out.println("stringCount " + i2);
        int i3 = lEDataInputStream.readInt();
        stringBlock.styleOffsetCount = i3;
        java.lang.System.out.println("styleOffsetCount " + i3);
        int i4 = lEDataInputStream.readInt();
        stringBlock.flags = i4;
        int i5 = lEDataInputStream.readInt();
        stringBlock.stringsOffset = i5;
        java.lang.System.out.println("stringsOffset " + i5);
        int i6 = lEDataInputStream.readInt();
        stringBlock.stylesOffset = i6;
        java.lang.System.out.println("stylesOffset " + i6);
        stringBlock.m_isUTF8 = (i4 & 256) != 0;
        stringBlock.m_stringOffsets = lEDataInputStream.readIntArray(i2);
        if (i3 != 0) {
            stringBlock.m_styleOffsets = lEDataInputStream.readIntArray(i3);
        }
        int i7 = (i6 == 0 ? i : i6) - i5;
        if (i7 % 4 != 0) {
            throw new java.io.IOException("String data size is not multiple of 4 (" + i7 + ").");
        }
        stringBlock.m_strings = new byte[i7];
        lEDataInputStream.readFully(stringBlock.m_strings);
        if (i6 != 0) {
            int i8 = i - i6;
            if (i8 % 4 != 0) {
                throw new java.io.IOException("Style data size is not multiple of 4 (" + i8 + ").");
            }
            stringBlock.m_styles = lEDataInputStream.readIntArray(i8 / 4);
            java.lang.System.out.println("m_styles_size " + i8);
        }
        java.lang.System.out.println();
        return stringBlock;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public java.lang.String getHTML(int i) {
        int[] style;
        java.lang.String string = getString(i);
        if (string == null || (style = getStyle(i)) == null) {
            return string;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(string.length() + 32);
        int[] iArr = new int[style.length / 3];
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = -1;
            for (int i5 = 0; i5 != style.length; i5 += 3) {
                if (style[i5 + 1] != -1 && (i4 == -1 || style[i4 + 1] > style[i5 + 1])) {
                    i4 = i5;
                }
            }
            int length = i4 != -1 ? style[i4 + 1] : string.length();
            int i6 = i3 - 1;
            while (i6 >= 0) {
                int i7 = iArr[i6];
                int i8 = style[i7 + 2];
                if (i8 >= length) {
                    break;
                }
                if (i2 <= i8) {
                    sb.append(string.substring(i2, i8 + 1));
                    i2 = i8 + 1;
                }
                outputStyleTag(getString(style[i7]), sb, true);
                i6--;
            }
            int i9 = i6 + 1;
            if (i2 < length) {
                sb.append(string.substring(i2, length));
                i2 = length;
            }
            if (i4 == -1) {
                return sb.toString();
            }
            outputStyleTag(getString(style[i4]), sb, false);
            style[i4 + 1] = -1;
            iArr[i9] = i4;
            i3 = i9 + 1;
        }
    }

    public int getSize() {
        if (this.m_stringOffsets != null) {
            return this.m_stringOffsets.length;
        }
        return 0;
    }

    public java.lang.String getString(int i) {
        int i2;
        int i3;
        if (i < 0 || this.m_stringOffsets == null || i >= this.m_stringOffsets.length) {
            return null;
        }
        int i4 = this.m_stringOffsets[i];
        if (this.m_isUTF8) {
            int i5 = i4 + getVarint(this.m_strings, i4)[1];
            int[] varint = getVarint(this.m_strings, i5);
            i2 = i5 + varint[1];
            i3 = varint[0];
        } else {
            i3 = getShort(this.m_strings, i4) * 2;
            i2 = i4 + 2;
        }
        return decodeString(i2, i3);
    }

    public void getStrings(java.util.List<java.lang.String> list) {
        int size = getSize();
        for (int i = 0; i < size; i++) {
            list.add(getString(i));
        }
    }

    public void write(com.haidar.bikinaplikasi.helper.LEDataOutputStream lEDataOutputStream) throws java.io.IOException {
        java.util.ArrayList arrayList = new java.util.ArrayList(getSize());
        getStrings(arrayList);
        write(arrayList, lEDataOutputStream);
    }

    public void write(java.util.List<java.lang.String> list, com.haidar.bikinaplikasi.helper.LEDataOutputStream lEDataOutputStream) throws java.io.IOException {
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        com.haidar.bikinaplikasi.helper.LEDataOutputStream lEDataOutputStream2 = new com.haidar.bikinaplikasi.helper.LEDataOutputStream(byteArrayOutputStream);
        int size = list.size();
        int[] iArr = new int[size];
        int length = 0;
        java.io.ByteArrayOutputStream byteArrayOutputStream2 = new java.io.ByteArrayOutputStream();
        com.haidar.bikinaplikasi.helper.LEDataOutputStream lEDataOutputStream3 = new com.haidar.bikinaplikasi.helper.LEDataOutputStream(byteArrayOutputStream2);
        for (int i = 0; i < size; i++) {
            iArr[i] = length;
            char[] charArray = list.get(i).toCharArray();
            lEDataOutputStream3.writeShort((short) charArray.length);
            lEDataOutputStream3.writeCharArray(charArray);
            lEDataOutputStream3.writeShort((short) 0);
            length += (charArray.length * 2) + 4;
        }
        int size2 = byteArrayOutputStream2.size();
        int i2 = size2 % 4;
        if (i2 != 0) {
            for (int i3 = 0; i3 < 4 - i2; i3++) {
                byteArrayOutputStream2.write(0);
            }
            int i4 = size2 + (4 - i2);
        }
        byte[] byteArray = byteArrayOutputStream2.toByteArray();
        java.lang.System.out.println("string chunk size: " + this.chunkSize);
        lEDataOutputStream2.writeInt(size);
        lEDataOutputStream2.writeInt(this.styleOffsetCount);
        lEDataOutputStream2.writeInt(this.flags);
        lEDataOutputStream2.writeInt(this.stringsOffset);
        lEDataOutputStream2.writeInt(this.stylesOffset);
        lEDataOutputStream2.writeIntArray(iArr);
        if (this.styleOffsetCount != 0) {
            java.lang.System.out.println("write stylesOffset");
            lEDataOutputStream2.writeIntArray(this.m_styleOffsets);
        }
        lEDataOutputStream2.writeFully(byteArray);
        if (this.m_styles != null) {
            java.lang.System.out.println("write m_styles");
            lEDataOutputStream2.writeIntArray(this.m_styles);
        }
        lEDataOutputStream.writeInt(CHUNK_STRINGBLOCK);
        byte[] byteArray2 = byteArrayOutputStream.toByteArray();
        lEDataOutputStream.writeInt(byteArray2.length + 8);
        lEDataOutputStream.writeFully(byteArray2);
    }
}
