import java.io.*;
import java.util.*;
import com.muhfau.bikinaplikasi.helper.*;

public class ManifestPatcher {
    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Usage: ManifestPatcher <input_xml> <output_xml> <package_name> <app_name>");
            System.exit(1);
        }
        
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        String packageName = args[2];
        String appName = args[3];
        
        byte[] fileBytes = FileUtil.readFile(inputFile);
        AXmlEditor aXmlEditor = new AXmlEditor();
        List<String> stringTable = new ArrayList<>();
        aXmlEditor.read(stringTable, fileBytes);
        
        String joined = StringUtils.join(stringTable, "\n");
        
        // Replacements
        String replaced = joined
            .replaceFirst("com.bikinaplikasi.web", packageName)
            .replaceFirst("com.bikinaplikasi.web.mobileadsinitprovider", packageName + ".mobileadsinitprovider")
            .replaceFirst("com.bikinaplikasi.web.firebaseinitprovider", packageName + ".firebaseinitprovider")
            .replaceFirst("com.bikinaplikasi.web.permission.C2D_MESSAGE", packageName + ".permission.C2D_MESSAGE")
            .replace("Nama Aplikasi", appName)
            .replace("android:debuggable=\"true\"", "android:debuggable=\"false\"");
            
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        aXmlEditor.write(replaced, bos);
        
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(bos.toByteArray());
        fos.close();
        System.out.println("Manifest patched successfully!");
    }
}
