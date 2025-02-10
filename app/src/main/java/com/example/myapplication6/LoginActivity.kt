package com.example.myapplication6

import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication6.ui.theme.MyApplication6Theme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplication6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Login("Android")

                }

            }
        }

    }
}

@Composable
fun Login(name: String, modifier: Modifier = Modifier) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(

            text = "Hello there!",
            modifier=Modifier.fillMaxWidth(),
            fontSize = 30.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(

            text = "login now",
            modifier=Modifier.fillMaxWidth(),
            fontSize = 22.sp
        )
        Spacer(modifier=Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Login Banner",
            modifier=Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier=Modifier.height(20.dp))
        OutlinedTextField(
            value =email ,
            onValueChange ={
                email=it
            },
            label = {
                Text(text = "Email adress")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier=Modifier.height(10.dp))

        OutlinedTextField(
            value =password ,
            onValueChange ={
                password=it
            },
            label = {
                Text(text = "Password")
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier=Modifier.height(20.dp))

        Button(onClick = {

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
    }


}



@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    MyApplication6Theme {
        Login("Android")
    }
}