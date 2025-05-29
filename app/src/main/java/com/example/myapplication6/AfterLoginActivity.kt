    package com.example.myapplication6

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.offset
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Button
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBar
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
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Settings
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.unit.dp
    import androidx.compose.material.*
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.*
    import androidx.compose.ui.window.Dialog
    import androidx.compose.ui.window.DialogProperties

    class AfterLoginActivity : ComponentActivity() {
        private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)



            enableEdgeToEdge()
            setContent {
                var showProfileDialog by remember { mutableStateOf(false) }

                if (showProfileDialog) {
                    ProfileDialog(onDismiss = { showProfileDialog = false })
                }
                MyApplication6Theme {
                    Scaffold(modifier = Modifier.fillMaxSize(),topBar = { MyTopBar(showProfileDialog = { showProfileDialog = true }) }) { innerPadding ->
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
        Column(
            modifier=Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello $name!,you are logged in",
                modifier = modifier
            )
            Button(onClick = {
                Log.d("open", "signing")
                auth.signOut()
                Log.d("open", "end signout")

                mcontext.startActivity(Intent(mcontext, MainActivity::class.java))
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
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun MyTopBar(showProfileDialog: () -> Unit) {
            TopAppBar(
                title = {
                    Text("My App")
                },
                actions = {
                    IconButton(onClick = { /* Do something for settings */ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { showProfileDialog() }) {
                        Icon(Icons.Filled.Person, contentDescription = "User Profile")
                    }
                }
            )
        }

        @Composable
        fun ProfileDialog(onDismiss: () -> Unit) {
            Dialog(
                onDismissRequest = { onDismiss() },
                properties = DialogProperties(dismissOnClickOutside = true)
            ) {
                Box(
                    modifier = Modifier.offset(x = 10.dp, y = 5.dp),
                    contentAlignment = Alignment.TopStart
                ){
                    Surface(
                        shape = MaterialTheme.shapes.medium,

//                    elevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "User Profile")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Name: John Doe")
                            Text(text = "Email: john.doe@example.com")
                            Spacer(modifier = Modifier.height(16.dp))
//                        Button(onClick = { onDismiss() }) {
//                            Text("Close")
//                        }
                        }
                    }
                }
                }

        }



    }


