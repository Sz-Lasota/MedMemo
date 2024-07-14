package com.szylas.medmemo.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.ui.components.AppLogo
import com.szylas.medmemo.ui.components.PasswordInput
import com.szylas.medmemo.ui.components.PrimaryButton
import com.szylas.medmemo.ui.components.SecondaryButton
import com.szylas.medmemo.ui.components.TextInput
import com.szylas.medmemo.ui.ui.theme.AppBarBlackCode
import com.szylas.medmemo.ui.ui.theme.MedMemoTheme

class LoginActivity : ComponentActivity() {

    private lateinit var eMail: MutableState<String>
    private lateinit var password: MutableState<String>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor(AppBarBlackCode))
        )
        setContent {
            MedMemoTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                ) { _ ->
                    eMail = remember {
                        mutableStateOf("")
                    }

                    password = remember {
                        mutableStateOf("")
                    }
                    Column(
                        modifier = Modifier
                            .padding(40.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            20.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Headers()
                        Spacer(modifier = Modifier
                            .height(0.dp)
                            .weight(2f))
                        Inputs()
                        Spacer(modifier = Modifier
                            .height(0.dp)
                            .weight(1f))
                        Buttons()
                    }
                }
            }
        }
    }

    @Composable
    private fun Buttons() {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                30.dp,
                Alignment.Top
            )
        ) {
            PrimaryButton(
                text = stringResource(R.string.login),
                onClick = { Log.d("Logging in", password.value) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.don_t_have_account),
                style = TextStyle(
                    fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            SecondaryButton(
                text = stringResource(R.string.sing_up),
                onClick = {
                    Toast.makeText(this@LoginActivity, password.value, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

    @Composable
    private fun Inputs() {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                40.dp,
                Alignment.Bottom
            )
        ) {
            TextInput(
                value = eMail.value,
                label = stringResource(R.string.e_mail),
                onValueChange = { eMail.value = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
            PasswordInput(
                value = password.value,
                label = stringResource(R.string.password),
                onValueChange = { password.value = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

    @Composable
    private fun Headers() {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                20.dp,
                Alignment.Bottom
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
                style = TextStyle(
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            )
            Text(
                text = stringResource(R.string.please_sign_in_to_continue),
                style = TextStyle(
                    fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                )
            )
        }
    }

}
