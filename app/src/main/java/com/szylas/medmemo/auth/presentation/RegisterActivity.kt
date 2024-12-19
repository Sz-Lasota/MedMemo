package com.szylas.medmemo.auth.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.szylas.medmemo.R
import com.szylas.medmemo.auth.domain.managers.AuthManagerConfiguration
import com.szylas.medmemo.auth.domain.managers.AuthManagerProvider
import com.szylas.medmemo.auth.domain.models.RegisterCredentials
import com.szylas.medmemo.common.presentation.components.AppLogo
import com.szylas.medmemo.common.presentation.components.PasswordInput
import com.szylas.medmemo.common.presentation.components.PrimaryButton
import com.szylas.medmemo.common.presentation.components.TextInput
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {

    private lateinit var eMail: MutableState<String>
    private lateinit var name: MutableState<String>
    private lateinit var password: MutableState<String>
    private lateinit var repeatPassword: MutableState<String>

    private val authManager =
        AuthManagerProvider.use() ?: AuthManagerProvider.provide(AuthManagerConfiguration.FIREBASE)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
        )
        setContent {
            MedMemoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    eMail = remember {
                        mutableStateOf("")
                    }

                    name = remember {
                        mutableStateOf("")
                    }

                    password = remember {
                        mutableStateOf("")
                    }

                    repeatPassword = remember {
                        mutableStateOf("")
                    }
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(40.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(
                                20.dp, Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.Start
                        ) {
//                            AppLogo(
//                                modifier = Modifier.fillMaxWidth(),
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
                            Text(
                                text = "Create new account", style = TextStyleProvider.provide(
                                    style = TextStyleOption.TITLE_LARGE
                                )
                            )
//                            Spacer(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .weight(1f)
//                            )
                            Inputs()
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(2f)
                            )
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(
                                    30.dp, Alignment.Bottom
                                )
                            ) {
                                PrimaryButton(
                                    text = stringResource(id = R.string.sing_up),
                                    onClick = {
                                        lifecycleScope.register(credentials = getRegisterCredentials(),
                                            onSuccess = {
                                                startActivity(
                                                    Intent(
                                                        this@RegisterActivity,
                                                        LoginActivity::class.java
                                                    )
                                                )
                                                finish()
                                                Toast.makeText(
                                                    this@RegisterActivity, it, Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            onError = {
                                                Toast.makeText(
                                                    this@RegisterActivity, it, Toast.LENGTH_SHORT
                                                ).show()
                                            })

                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Inputs() {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        var repeatPasswordVisible by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                20.dp, Alignment.Bottom
            )
        ) {
            TextInput(
                value = name.value,
                label = stringResource(R.string.name),
                onValueChange = { name.value = it },
                modifier = Modifier.fillMaxWidth()
            )

            TextInput(
                value = eMail.value,
                label = stringResource(R.string.e_mail),
                onValueChange = { eMail.value = it },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text(text = stringResource(R.string.password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                }
            )
            OutlinedTextField(
                value = repeatPassword.value,
                onValueChange = { repeatPassword.value = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text(text = stringResource(R.string.repeatPassword)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (repeatPasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (repeatPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {repeatPasswordVisible = !repeatPasswordVisible}){
                        Icon(imageVector  = image, description)
                    }
                }
            )
        }
    }

    private fun getRegisterCredentials(): RegisterCredentials = RegisterCredentials(
        eMail = eMail.value,
        name = name.value,
        password = password.value,
        repeatedPassword = repeatPassword.value
    )

    private fun CoroutineScope.register(
        credentials: RegisterCredentials, onSuccess: (String) -> Unit, onError: (String) -> Unit
    ) = launch {
        authManager.register(
            credentials = credentials, onSuccess = onSuccess, onError = onError
        )
    }
}
