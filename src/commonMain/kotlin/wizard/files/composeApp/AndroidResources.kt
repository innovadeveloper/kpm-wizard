package wizard.files.composeApp

import wizard.ProjectFile
import wizard.ProjectInfo

class AndroidManifest(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/AndroidManifest.xml"
    else
        "${info.moduleName}/src/androidMain/AndroidManifest.xml"

    override val content = """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android">
        
            <!-- Permisos comunes -->
            <uses-permission android:name="android.permission.INTERNET"/>
            <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
            
            
            <application
                android:name=".AppApplication"
                android:icon="@mipmap/ic_launcher"
                android:label="${info.name}"
                android:theme="@android:style/Theme.Material.NoActionBar">
                <activity
                    android:name="${if (isOnlyAndroid) ".MainActivity" else ".AppActivity"}"
                    android:configChanges="orientation|screenSize|screenLayout|keyboardHidden"
                    android:launchMode="singleInstance"
                    android:windowSoftInputMode="adjustPan"
                    android:exported="true">
                    <intent-filter>
                        <action android:name="android.intent.action.MAIN" />
                        <category android:name="android.intent.category.LAUNCHER" />
                    </intent-filter>
                </activity>
            </application>
        
        </manifest>
    """.trimIndent()
}

class SimpleAndroidManifest(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/AndroidManifest.xml"
    else
        "${info.moduleName}/src/androidMain/AndroidManifest.xml"

    override val content = """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android">
            <!-- Permisos comunes -->
            <uses-permission android:name="android.permission.INTERNET"/>
            <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
        
            <application
                android:icon="@mipmap/ic_launcher"
                android:label="${info.name}"
                android:theme="@android:style/Theme.Material.NoActionBar">
                <activity
                    android:name=".MainActivity"
                    android:configChanges="orientation|screenSize|screenLayout|keyboardHidden"
                    android:launchMode="singleInstance"
                    android:windowSoftInputMode="adjustPan"
                    android:exported="true">
                    <intent-filter>
                        <action android:name="android.intent.action.MAIN" />
                        <category android:name="android.intent.category.LAUNCHER" />
                    </intent-filter>
                </activity>
            </application>
        
        </manifest>
    """.trimIndent()
}


