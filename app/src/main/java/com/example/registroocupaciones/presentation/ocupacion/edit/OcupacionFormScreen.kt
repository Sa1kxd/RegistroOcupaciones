package com.example.registroocupaciones.presentation.ocupacion.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcupacionFormScreen(
    viewModel: OcupacionViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(state.isNew) "Nueva Ocupación" else "Editar Ocupación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atras")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("text_error_general")
                )
            }

            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { viewModel.onEvent(OcupacionUiEvent.DescripcionChanged(it)) },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_description"),
                isError = state.descripcionError != null,
                supportingText = state.descripcionError?.let { { Text(it) } },
                singleLine = false,
                minLines = 3,
                maxLines = 5
            )

            OutlinedTextField(
                value = state.sueldo,
                onValueChange = { viewModel.onEvent(OcupacionUiEvent.SueldoChanged(it)) },
                label = { Text("Sueldo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_sueldo"),
                isError = state.sueldoError != null,
                supportingText = state.sueldoError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Button(
                onClick = { viewModel.onEvent(OcupacionUiEvent.Save) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save"),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar")
                }
            }

            if (!state.isNew) {
                OutlinedButton(
                    onClick = { viewModel.onEvent(OcupacionUiEvent.Delete) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_delete"),
                    enabled = !state.isDeleting,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    if (state.isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text("Eliminar Ocupación")
                    }
                }
            }
        }
    }
}