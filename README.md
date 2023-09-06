Lecture 2 - Networking

Let's make our lives easier by enabling LiveEdit. Cool feature that will dynamically 
update the code on the emulator device.

Creating another new activity we need to adopt the correct classes to import. Generally,
we might refer to these as dependencies and more broadly this is a form of package 
management. Without it you would need to drag source code into your project which is 
obviously a headache.

    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))

    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor") 
    
A BOM or Bill of Materials is an umbrella that allows you to set the version of associated
packages without having to specify each one repeatedly.

This networking package is called OkHttp and was developed by Square.

https://squareup.com/us/en?v=all
https://square.github.io/okhttp/

Next we add some code to HTTP GET from the Internet. It doesn't work, why?

FATAL EXCEPTION: main
Process: com.cameronpalmer.okhttptest, PID: 8386
java.lang.RuntimeException: Unable to start activity ComponentInfo{com.cameronpalmer.okhttptest/com.cameronpalmer.okhttptest.MainActivity}: android.os.NetworkOnMainThreadException

No networking on the main thread. So we need to operate on a background thread.

We could override this by manipulating the ThreadPolicy to permitAll()

https://developer.android.com/reference/kotlin/android/os/StrictMode.ThreadPolicy.Builder

However, this generally a bad plan, so let's instead adopt CoroutineScope


    fun run(url: String) {
        val request: Request = Builder().url(url).build()
        val call = client.newCall(request)

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            Log.d("OkHttp!", "Background thread")

            val response = call.execute()
            launch(Dispatchers.Main) {
                callback(response.body?.string())
            }
        }
    }

    // The callback will arrive on the main thread
    fun callback(body: String?) {
        Log.d("OkHttp!", "Callback")
        Log.d("OkHttp!", body!!)
    }
    
We never go to our breakpoint. Why? Logcat Time

FATAL EXCEPTION: DefaultDispatcher-worker-1
Process: com.cameronpalmer.okhttptest, PID: 8703
java.lang.SecurityException: Permission denied (missing INTERNET permission?)

The Android Manifest is where critical information about the app resides. Including 
the location of where the 'Main' function / Activity is and permissions the app uses.

https://developer.android.com/guide/topics/manifest/manifest-intro
https://developer.android.com/guide/topics/manifest/uses-permission-element
https://developer.android.com/guide/topics/permissions/overview
https://developer.android.com/training/basics/network-ops/connecting

It is an Install time permission:

    <uses-permission android:name="android.permission.INTERNET" />

We've downloaded the data from our remote endpoint but we did not display it on screen.
It can be found, once again in Logcat.

Briefly looking at Log
https://developer.android.com/reference/kotlin/android/util/Log

Discord Link:
[https://discord.gg/fKgkdJMF|https://discord.gg/fKgkdJMF]
