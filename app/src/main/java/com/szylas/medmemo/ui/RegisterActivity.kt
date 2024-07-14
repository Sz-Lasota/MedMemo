package com.szylas.medmemo.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.ui.components.AppLogo
import com.szylas.medmemo.ui.components.PrimaryButton
import com.szylas.medmemo.ui.components.TextInput
import com.szylas.medmemo.ui.theme.TextStyleOption
import com.szylas.medmemo.ui.theme.TextStyleProvider
import com.szylas.medmemo.ui.ui.theme.AppBarBlackCode
import com.szylas.medmemo.ui.ui.theme.MedMemoTheme

class RegisterActivity : ComponentActivity() {

    private lateinit var eMail: MutableState<String>
    private lateinit var name: MutableState<String>
    private lateinit var password: MutableState<String>
    private lateinit var repeatPassword: MutableState<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor(AppBarBlackCode))
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
                            AppLogo(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Create new account", style = TextStyleProvider.provide(
                                    style = TextStyleOption.TITLE_LARGE
                                )
                            )
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f))
                            Inputs()
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f))
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(
                                    30.dp, Alignment.Bottom
                                )
                            ) {
                                PrimaryButton(
                                    text = stringResource(id = R.string.sing_up), onClick = {
                                        startActivity(
                                            Intent(this@RegisterActivity, LoginActivity::class.java)
                                        )
                                        finish()
                                    }, modifier = Modifier.fillMaxWidth()
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

            TextInput(
                value = password.value,
                label = stringResource(R.string.password),
                onValueChange = { password.value = it },
                modifier = Modifier.fillMaxWidth()
            )

            TextInput(
                value = repeatPassword.value,
                label = stringResource(R.string.repeatPassword),
                onValueChange = { repeatPassword.value = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
