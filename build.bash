ninja -C out/Default chrome_modern_public_apk

mv out/Default/apks/ChromeModernPublic.apk out/Default/apks/Dodgeman.apk

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ${BASH_SOURCE%/*}/keystores/eunchul.jks out/Default/apks/Dodgeman.apk dodgeman-browser
third_party/android_tools/sdk/build-tools/27.0.3/zipalign -v -p 4 out/Default/apks/Dodgeman.apk out/Default/apks/Dodgeman_aligned.apk
