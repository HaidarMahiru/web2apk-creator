const fs = require('fs');
const { execSync } = require('child_process');
const path = require('path');

// Parse CLI Arguments
const args = process.argv.slice(2);
if (args.length < 4) {
    console.log("\n========================================================");
    console.log("APK Generator CLI Tool (Lightweight Version)");
    console.log("========================================================");
    console.log("Usage:");
    console.log("  node build_apk.js <AppName> <URL> <PackageName> <IconPath> [OutputApkPath]");
    console.log("\nExample:");
    console.log("  node build_apk.js \"HaidarOTP\" \"https://haidarshop.my.id\" \"com.haidar.otp\" \"foto.jpg\"");
    console.log("========================================================\n");
    process.exit(1);
}

const appName = args[0];
const url = args[1];
const packageName = args[2];
const iconFile = args[3];
const outputApk = args[4] || `${appName.replace(/[^a-zA-Z0-9]/g, "")}.apk`;

// Validate inputs
if (!fs.existsSync("template/NamaAplikasi.zip") || !fs.existsSync("template/AndroidManifest.xml") || !fs.existsSync("template/mmdfauzan.key")) {
    console.error("Error: Template files missing in 'template/' directory.");
    process.exit(1);
}
if (!fs.existsSync(iconFile)) {
    console.error(`Error: Icon file '${iconFile}' not found.`);
    process.exit(1);
}
if (!packageName.match(/^[a-zA-Z][a-zA-Z0-9_]*(\.[a-zA-Z][a-zA-Z0-9_]*)+$/)) {
    console.error(`Error: Invalid package name format: '${packageName}'`);
    process.exit(1);
}

console.log("--- Starting APK Generation ---");
console.log(`App Name:     ${appName}`);
console.log(`URL:          ${url}`);
console.log(`Package Name: ${packageName}`);
console.log(`Icon:         ${iconFile}`);
console.log(`Output:       ${outputApk}`);
console.log("-------------------------------");

const cleanCmd = "rm -rf temp_template.apk temp_AndroidManifest.xml temp_mmdfauzan.key patched_AndroidManifest.xml unsigned.apk extracted_apk";

try {
    // 1. Clean up old files
    execSync(cleanCmd);

    // 2. Copy template resources
    console.log("[1] Copying template resources...");
    fs.copyFileSync("template/NamaAplikasi.zip", "temp_template.apk");
    fs.copyFileSync("template/AndroidManifest.xml", "temp_AndroidManifest.xml");
    fs.copyFileSync("template/mmdfauzan.key", "temp_mmdfauzan.key");

    // 3. Patch binary AndroidManifest.xml
    console.log("[2] Patching AndroidManifest.xml...");
    execSync(`java -cp build_patcher ManifestPatcher temp_AndroidManifest.xml patched_AndroidManifest.xml ${packageName} "${appName}"`);

    // 4. Decompress template to a temporary folder
    console.log("[3] Decompressing template...");
    fs.mkdirSync("extracted_apk");
    execSync("unzip -q temp_template.apk -d extracted_apk");

    // 5. Remove original signature META-INF
    console.log("[4] Removing original signatures...");
    execSync("rm -rf extracted_apk/META-INF");

    // 6. Inject modified files
    console.log("[5] Injecting configurations and icon...");
    fs.writeFileSync("extracted_apk/assets/ad", url);
    fs.writeFileSync("extracted_apk/assets/ads", JSON.stringify({ app_id: "", unit_id: "" }));
    fs.writeFileSync("extracted_apk/assets/pref", JSON.stringify({ ptr: 1 })); // ptr: 1 is Swipe to Refresh Enabled
    fs.copyFileSync("patched_AndroidManifest.xml", "extracted_apk/AndroidManifest.xml");
    
    fs.mkdirSync("extracted_apk/res/drawable", { recursive: true });
    fs.copyFileSync(iconFile, "extracted_apk/res/drawable/ico.png");

    // 7. Compress back to APK using jar
    console.log("[6] Compiling back to APK...");
    execSync("jar cf unsigned.apk -C extracted_apk .");

    // 8. Sign APK
    console.log("[7] Signing the APK...");
    execSync(`apksigner sign --ks temp_mmdfauzan.key --ks-pass pass:mdmdky --key-pass pass:mdmdky --out "${outputApk}" unsigned.apk`);
    console.log("APK signed successfully!");

    // Cleanup
    execSync(cleanCmd);
    console.log(`\n--- SUCCESS: Generated APK saved to ${outputApk} ---`);
} catch (error) {
    console.error("\nError occurred during APK generation:");
    console.error(error.message);
    if (error.stdout) console.log(error.stdout.toString());
    if (error.stderr) console.error(error.stderr.toString());
    // Cleanup on failure
    try { execSync(cleanCmd); } catch(e) {}
}
