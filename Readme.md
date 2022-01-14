# Hammered application

An android application that will help you create your favorite drinks. Browse through a collection of popular cocktails and ingredients or create your own! Share your recipes with friends and family using the app's import/export feature.

## Building the app

Clone this repository and on the repository's folder open a terminal.
```
gradlew assembleDebug
```

The preceding command will build the application.[^bignote] An **apk** file is created on "\app\build\outputs\apk\debug". You can install the apk on any android device or emulator running on API 27(Android 8.1) and above. For the best experience use a device with a resolution of around 1080 by 1920. 

[^bignote]: The build process will download several dependencies from the internet and requires an active internet connection. If you're not familiar with android and gradle command line interface, it is recommended that you use [Android Studio](https://developer.android.com/studio) to build the application.