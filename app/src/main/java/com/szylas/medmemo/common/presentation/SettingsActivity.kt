package com.szylas.medmemo.common.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.szylas.medmemo.R
import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.auth.domain.managers.AuthManager
import com.szylas.medmemo.auth.domain.managers.AuthManagerConfiguration
import com.szylas.medmemo.auth.domain.managers.AuthManagerProvider
import com.szylas.medmemo.auth.domain.models.LoginCredentials
import com.szylas.medmemo.common.domain.models.PrefItem
import com.szylas.medmemo.common.presentation.components.TextInput
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            var options by remember {
                mutableStateOf(
                    listOf(
                        PrefItem(
                            name = "Simplified UI",
                            value = false
                        )
                    )
                )
            }
            MedMemoTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(10.dp)
                    ) {
                        PrefList(
                            options = options,
                            modifier = Modifier
                                .fillMaxWidth()
//                                .shadow(5.dp, RoundedCornerShape(14.dp))
//                                .clip(RoundedCornerShape(14.dp))
                                .padding(10.dp)
                        ) { item ->
                            options = mutableListOf<PrefItem>().apply {
                                addAll(options)
                                remove(item)
                                add(PrefItem(item.name, !item.value))
                            }.toList()
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun PrefList(
        options: List<PrefItem>,
        modifier: Modifier = Modifier,
        onItemChange: (PrefItem) -> Unit,
    ) {

        var newEmail by remember { mutableStateOf("") }
        val emailModalState = rememberModalBottomSheetState()
        var emailModalShow by remember {
            mutableStateOf(false)
        }

        val passModalState = rememberModalBottomSheetState()
        var passModalShow by remember {
            mutableStateOf(false)
        }

        var pass by remember { mutableStateOf("") }
        var reapetPass by remember { mutableStateOf("") }

        var authPass by remember { mutableStateOf("") }
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(options) {
                PrefListItem(
                    it,
                    onItemChange = onItemChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .shadow(5.dp, RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(10.dp)
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .shadow(5.dp, RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(10.dp)
                ) {
                    Text("Change e-mail", style = MaterialTheme.typography.titleLarge)
                    TextInput(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = stringResource(R.string.new_e_mail),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { emailModalShow = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .shadow(5.dp, RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(10.dp)
                ) {
                    Text("Change password", style = MaterialTheme.typography.titleLarge)
                    TextInput(
                        value = pass,
                        onValueChange = { pass = it },
                        label = stringResource(R.string.new_password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextInput(
                        value = reapetPass,
                        onValueChange = { reapetPass = it },
                        label = stringResource(R.string.repeat_password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (reapetPass != pass) {
                                Toast.makeText(
                                    this@SettingsActivity,
                                    getString(R.string.passwords_do_not_match),
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            passModalShow = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }
        }

        if (emailModalShow) {
            ModalBottomSheet(sheetState = emailModalState, onDismissRequest = {
                emailModalShow = false

            }) {
                val auth = AuthManagerProvider.provide(AuthManagerConfiguration.FIREBASE)
                Text("Enter password:")
                TextInput(
                    value = authPass,
                    onValueChange = { authPass = it },
                    label = stringResource(R.string.password),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        lifecycleScope.auth(
                            auth,
                            LoginCredentials(Session.email()!!, authPass),
                            onSuccess = {
                                lifecycleScope.changeEmail(
                                    auth,
                                    newEmail,
                                    onSuccess = {
                                        authPass = ""
                                        Toast.makeText(
                                            this@SettingsActivity,
                                            "Successfully changed email!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        emailModalShow = false
                                    },
                                    onError = {
                                        authPass = ""
                                        Toast.makeText(
                                            this@SettingsActivity,
                                            it,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                            },
                            onError = {
                                authPass = ""
                                Toast.makeText(
                                    this@SettingsActivity,
                                    it,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        }


        if (passModalShow) {
            ModalBottomSheet(sheetState = emailModalState, onDismissRequest = {
                passModalShow = false

            }) {
                val auth = AuthManagerProvider.provide(AuthManagerConfiguration.FIREBASE)
                Text("Enter password:")
                TextInput(
                    value = authPass,
                    onValueChange = { authPass = it },
                    label = stringResource(R.string.password),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        lifecycleScope.auth(
                            auth,
                            LoginCredentials(Session.email()!!, authPass),
                            onSuccess = {
                                lifecycleScope.changePassword(
                                    auth,
                                    pass,
                                    onSuccess = {
                                        authPass = ""
                                        Toast.makeText(
                                            this@SettingsActivity,
                                            "Successfully changed password!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        passModalShow = false
                                    },
                                    onError = {
                                        authPass = ""
                                        Toast.makeText(
                                            this@SettingsActivity,
                                            it,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        passModalShow = false
                                    })
                            },
                            onError = {
                                Log.e("AUTH", "FAIled")
                                authPass = ""
                                Toast.makeText(
                                    this@SettingsActivity,
                                    it,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        }
    }

    private fun CoroutineScope.auth(
        auth: AuthManager,
        cred: LoginCredentials,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) = launch {
        auth.login(cred, onSuccess, onError)
    }

    private fun CoroutineScope.changeEmail(
        auth: AuthManager,
        email: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) = launch {
        auth.changeEmail(email, onSuccess, onError)
    }

    private fun CoroutineScope.changePassword(
        auth: AuthManager,
        pass: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) = launch {
        auth.changePassword(pass, onSuccess, onError)
    }

    @Composable
    private fun PrefListItem(
        item: PrefItem,
        onItemChange: (PrefItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = item.value, onCheckedChange = {
                onItemChange(item)
            })
        }
    }

}