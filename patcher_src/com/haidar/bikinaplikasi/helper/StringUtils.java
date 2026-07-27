package com.haidar.bikinaplikasi.helper;

/* JADX INFO: loaded from: classes.dex */
public class StringUtils {
    private StringUtils() {
    }

    public static java.lang.String join(java.util.Collection<java.lang.String> collection, java.lang.String str) {
        java.lang.StringBuffer stringBuffer = new java.lang.StringBuffer();
        java.util.Iterator<java.lang.String> it = collection.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next());
            if (it.hasNext()) {
                stringBuffer.append(str);
            }
        }
        return stringBuffer.toString();
    }
}
