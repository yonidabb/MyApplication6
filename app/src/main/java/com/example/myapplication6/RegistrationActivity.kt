package com.example.myapplication6


import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import androidx.compose.foundation.layout.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.myapplication6.db.DBManager
import com.example.myapplication6.ui.theme.MyApplication6Theme

//import androidx.lifecycle.viewmodel.compose.viewModel;
//import com.example.registrationapp.ui.theme.RegistrationAppTheme;

class RegistrationActivity : ComponentActivity() {
    val dbManager = DBManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContent {
            MyApplication6Theme {
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
    var city by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column() {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name1") })
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
        OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") })
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Button(onClick = {
            if (name.isNotEmpty() && age.isNotEmpty() && state.isNotEmpty() && city.isNotEmpty() && password.isNotEmpty()) {
                val user = User(name, age.toInt(), state, city, password)
//                userViewModel.insert(user)
                dbManager.open()
                dbManager.insert(name,city)
                dbManager.close()
                Toast.makeText(cur, "User Registered Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(cur, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Register")
        }
        Button(onClick = {
            dbManager.open()
            val fetch = dbManager.fetch()
            val z0 = fetch.getString(0)
            val z1 = fetch.getString(0)
            dbManager.close()
            Toast.makeText(cur, z0+z1, Toast.LENGTH_SHORT).show()

        }) {
            Text("Select All")
        }
    }
}

class User(name: String, toInt: Int, state: String, city: String, password: String) {

}
