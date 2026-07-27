package com.haidar.bikinaplikasi.helper;

/* JADX INFO: loaded from: classes.dex */
public class AXmlEditor implements com.haidar.bikinaplikasi.helper.Edit {
    private com.haidar.bikinaplikasi.helper.AXmlDecoder axml;

    @Override // com.haidar.bikinaplikasi.helper.Edit
    public void read(java.util.List<java.lang.String> list, byte[] bArr) throws java.io.IOException {
        this.axml = com.haidar.bikinaplikasi.helper.AXmlDecoder.read(new java.io.ByteArrayInputStream(bArr));
        this.axml.mTableStrings.getStrings(list);
    }

    @Override // com.haidar.bikinaplikasi.helper.Edit
    public void write(java.lang.String str, java.io.OutputStream outputStream) throws java.io.IOException {
        java.lang.String[] strArrSplit = str.split("\n");
        java.util.ArrayList arrayList = new java.util.ArrayList(strArrSplit.length);
        for (java.lang.String str2 : strArrSplit) {
            arrayList.add(str2);
        }
        this.axml.write(arrayList, outputStream);
    }
}
