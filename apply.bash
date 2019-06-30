git apply ${BASH_SOURCE%/*}/patches/*.patch
cp -r ${BASH_SOURCE%/*}/src/* ./
# tools/android/roll/android_deps/fetch_all.py --update-all
