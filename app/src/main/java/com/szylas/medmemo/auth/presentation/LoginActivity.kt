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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.presentation.components.AppLogo
import com.szylas.medmemo.common.presentation.components.PasswordInput
import com.szylas.medmemo.common.presentation.components.PrimaryButton
import com.szylas.medmemo.common.presentation.components.SecondaryButton
import com.szylas.medmemo.common.presentation.components.TextInput
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.main.presentation.MainActivity
import com.szylas.medmemo.ui.ui.theme.AppBarBlackCode
import com.szylas.medmemo.ui.ui.theme.MedMemoTheme

class LoginActivity : ComponentActivity() {

    private lateinit var eMail: MutableState<String>
    private lateinit var password: MutableState<String>

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

                    password = remember {
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
                            Headers()
                            Spacer(
                                modifier = Modifier
                                    .weight(2f)
                            )
                            Inputs()
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Buttons()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Buttons() {
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                30.dp, Alignment.Top
            )
        ) {
            PrimaryButton(
                text = stringResource(R.string.login), onClick = {
                    Toast.makeText(this@LoginActivity, password.value, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }, modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.don_t_have_account),
                style = TextStyleProvider.provide(
                    TextStyleOption.LABEL_MEDIUM
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                text = stringResource(R.string.sing_up), onClick = {
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }, modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    private fun Inputs() {
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                40.dp, Alignment.Bottom
            )
        ) {
            TextInput(
                value = eMail.value,
                label = stringResource(R.string.e_mail),
                onValueChange = { eMail.value = it },
                modifier = Modifier.fillMaxWidth()
            )
            PasswordInput(
                value = password.value,
                label = stringResource(R.string.password),
                onValueChange = { password.value = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    private fun Headers() {
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                20.dp, Alignment.Bottom
            )
        ) {
            AppLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(R.string.welcome),
                style = TextStyleProvider.provide(TextStyleOption.TITLE_LARGE)
            )
            Text(
                text = stringResource(R.string.please_sign_in_to_continue),
                style = TextStyleProvider.provide(
                    TextStyleOption.LABEL_MEDIUM
                )
            )
        }
    }

}
