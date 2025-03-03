package com.example.myapplication6

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.myapplication6.ui.theme.MyApplication6Theme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AfterLoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        enableEdgeToEdge()
        setContent {
            MyApplication6Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting3(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

@Composable
fun Greeting3(modifier: Modifier = Modifier) {
    auth = Firebase.auth
    val currentUser = auth.currentUser
    val mcontext= LocalContext.current

    if (currentUser == null) {
        mcontext.startActivity(Intent(mcontext,MainActivity::class.java))

        return;
    }
    val name=currentUser.email.toString()
    Text(
        text = "Hello $name!,you are logged in",
        modifier = modifier
    )
    Button(onClick = {
        Log.d("open","signing")
        auth.signOut()
        Log.d("open","end signout")

        mcontext.startActivity(Intent(mcontext,MainActivity::class.java))
    }) {
        Text(
            text = "Signout",
            modifier = Modifier.fillMaxWidth(),

            style = TextStyle(
                fontSize = 26.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

}


