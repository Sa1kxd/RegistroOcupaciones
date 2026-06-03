package com.example.registroempleados.presentation.empleado.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoFormScreen(
    empleadoId: Int,
    viewModel: EmpleadoFormViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var expandedSexo by remember { mutableStateOf(false) }
    val opcionesSexo = listOf("Masculino", "Femenino", "Otro")

    LaunchedEffect(empleadoId) {
        viewModel.onEvent(EmpleadoFormUiEvent.Load(empleadoId))
    }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(state.isNew) "Nuevo Empleado" else "Editar Empleado") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
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

            OutlinedTextField(
                value = state.nombres,
                onValueChange = { viewModel.onEvent(EmpleadoFormUiEvent.NombresChanged(it)) },
                label = { Text("Nombres") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_nombres"),
                isError = state.nombresError != null,
                supportingText = state.nombresError?.let { { Text(it) } },
                singleLine = true
            )

            OutlinedTextField(
                value = state.fechaIngreso,
                onValueChange = { nuevoTexto ->
                    if (nuevoTexto.length <= 8 && nuevoTexto.all { it.isDigit() }) {
                        viewModel.onEvent(EmpleadoFormUiEvent.FechaIngresoChanged(nuevoTexto))
                    }
                },
                label = { Text("Fecha de Ingreso (DDMMAAAA)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_fecha_ingreso"),
                isError = state.fechaIngresoError != null,
                supportingText = state.fechaIngresoError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                visualTransformation = DateVisualTransformation()
            )

            ExposedDropdownMenuBox(
                expanded = expandedSexo,
                onExpandedChange = { expandedSexo = !expandedSexo },
            ) {
                OutlinedTextField(
                    value = state.sexo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSexo) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("input_sexo"),
                    isError = state.sexoError != null,
                    supportingText = state.sexoError?.let { { Text(it) } },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedSexo,
                    onDismissRequest = { expandedSexo = false }
                ) {
                    opcionesSexo.forEach { seleccion ->
                        DropdownMenuItem(
                            text = { Text(seleccion) },
                            onClick = {
                                viewModel.onEvent(EmpleadoFormUiEvent.SexoChanged(seleccion))
                                expandedSexo = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.sueldo,
                onValueChange = { viewModel.onEvent(EmpleadoFormUiEvent.SueldoChanged(it)) },
                label = { Text("Sueldo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_sueldo"),
                isError = state.sueldoError != null,
                supportingText = state.sueldoError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Button(
                onClick = { viewModel.onEvent(EmpleadoFormUiEvent.Save) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save"),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1 || i == 3) out += "/"
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}