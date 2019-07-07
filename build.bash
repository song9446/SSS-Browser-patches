autoninja -C out/Default chrome_public_apk

mv out/Default/apks/ChromePublic.apk out/Default/apks/DodgeChrome.apk

zip -d out/Default/apks/DodgeChrome.apk META-INF/\*
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ${BASH_SOURCE%/*}/keystores/eunchul.jks out/Default/apks/DodgeChrome.apk dodgechrome
third_party/android_tools/sdk/build-tools/27.0.3/zipalign -v -p 4 out/Default/apks/DodgeChrome.apk out/Default/apks/DodgeChrome_aligned.apk


autoninja -C out/Default chrome_modern_public_apk

mv out/Default/apks/ChromeModernPublic.apk out/Default/apks/DodgeModernChrome.apk

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ${BASH_SOURCE%/*}/keystores/eunchul.jks out/Default/apks/DodgeModernChrome.apk dodgechrome
third_party/android_tools/sdk/build-tools/27.0.3/zipalign -v -p 4 out/Default/apks/DodgeModernChrome.apk out/Default/apks/DodgeModernChrome_aligned.apk


autoninja -C out/Default monochrome_public_apk

mv out/Default/apks/MonoChromePublic.apk out/Default/apks/DodgeMonoChrome.apk

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ${BASH_SOURCE%/*}/keystores/eunchul.jks out/Default/apks/DodgeMonoChrome.apk dodgechrome
third_party/android_tools/sdk/build-tools/27.0.3/zipalign -v -p 4 out/Default/apks/DodgeMonoChrome.apk out/Default/apks/DodgeMonoChrome_aligned.apk


autoninja -C out/Default trichrome_public_apk

mv out/Default/apks/TriChromePublic.apk out/Default/apks/DodgeTriChrome.apk

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ${BASH_SOURCE%/*}/keystores/eunchul.jks out/Default/apks/DodgeTriChrome.apk dodgechrome
third_party/android_tools/sdk/build-tools/27.0.3/zipalign -v -p 4 out/Default/apks/DodgeTriChrome.apk out/Default/apks/DodgeTriChrome_aligned.apk
