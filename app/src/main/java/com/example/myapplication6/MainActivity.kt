package com.example.myapplication6

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication6.ui.theme.MyApplication6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MyApplication6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val mcontext= LocalContext.current

    Column(
        modifier=modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(R.drawable.logo),
            modifier =  modifier.clip(CircleShape),
            contentDescription = "1"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Strat your adventure now!",
            fontSize = 30.sp,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(40.dp))


        Button(onClick = {
            Log.d("open","open login")
            mcontext.startActivity(Intent(mcontext,LoginActivity::class.java))

        }) {
            Text(
                text = "Login",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            Log.d("open","open sign up")
            mcontext.startActivity(Intent(mcontext,SignUpActivity::class.java))
        }) {
            Text(
                text = "Signup",
                modifier = Modifier.fillMaxWidth(),

                style = TextStyle(
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            Log.d("open","open sign up")
            mcontext.startActivity(Intent(mcontext,RegistrationActivity::class.java))
        }) {
            Text(
                text = "RegistrationActivity",
                modifier = Modifier.fillMaxWidth(),

                style = TextStyle(
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplication6Theme {
        Greeting("Android")
    }
}