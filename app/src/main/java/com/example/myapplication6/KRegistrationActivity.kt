package com.example.myapplication6

import android.os.Bundle
import android.util.Log
import android.widget.Toast;
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication6.db.DBManager
import com.example.myapplication6.ui.theme.MyApplication6Theme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

//import androidx.lifecycle.viewmodel.compose.viewModel;
//import com.example.registrationapp.ui.theme.RegistrationAppTheme;

class RegistrationActivity : ComponentActivity() {
    val dbManager = DBManager(this)
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        auth = Firebase.auth

        setContent {
            MyApplication6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistrationScreen(dbManager)
                }

            }
        }
    }

@Composable
fun RegistrationScreen(dbManager: DBManager) {
    var cur = LocalContext.current
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(12.dp),
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            modifier =  Modifier.scale(.2f).clip(CircleShape),
            contentDescription = "1",
            )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Create your account",
            fontSize = 30.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))


        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
        OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Button(onClick = {
            registerUser(email,password)
            if (name.isNotEmpty() && age.isNotEmpty() && state.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(name, age.toInt(), state, email, password)
//                userViewModel.insert(user)
                dbManager.open()
                dbManager.insert(name,email)
                dbManager.close()
                Toast.makeText(cur, "User Registered Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(cur, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Register")
        }
//        Button(onClick = {
//            dbManager.open()
//            val fetch = dbManager.fetch()
//            val z0 = fetch.getString(0)
//            val z1 = fetch.getString(0)
//            dbManager.close()
//            Toast.makeText(cur, z0+z1, Toast.LENGTH_SHORT).show()
//
//        }) {
//            Text("Select All")
//        }
    }
}
    companion object {
        private const val TAG = "EmailPassword"
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }


    }
}


class User(name: String, toInt: Int, state: String, city: String, password: String) {

}
