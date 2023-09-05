package com.cameronpalmer.okhttptest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cameronpalmer.okhttptest.ui.theme.OkHttpTestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder



class MainActivity : ComponentActivity() {
    var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        run("https://pokeapi.co/api/v2/pokemon")

        setContent {
            OkHttpTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

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

    fun callback(body: String?) {
        Log.d("OkHttp!", "Callback")
        Log.d("OkHttp!", body!!)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OkHttpTestTheme {
        Greeting("Android")
    }
}