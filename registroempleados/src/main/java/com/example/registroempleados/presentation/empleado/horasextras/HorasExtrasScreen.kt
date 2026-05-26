package com.example.registroempleados.presentation.empleado.horasextras

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorasExtrasScreen(
    viewModel: HorasExtrasViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cálculo de Horas Extras") },
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
            ExposedDropdownMenuBox(
                expanded = state.isDropdownExpanded,
                onExpandedChange = { viewModel.onEvent(HorasExtrasUiEvent.OnDropdownExpandedChanged(it)) }
            ) {
                OutlinedTextField(
                    value = state.selectedEmpleado?.nombres ?: "Seleccione un empleado",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Empleado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isDropdownExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = state.isDropdownExpanded,
                    onDismissRequest = { viewModel.onEvent(HorasExtrasUiEvent.OnDropdownExpandedChanged(false)) }
                ) {
                    state.empleados.forEach { empleado ->
                        DropdownMenuItem(
                            text = { Text("${empleado.nombres} - $${empleado.sueldo}") },
                            onClick = { viewModel.onEvent(HorasExtrasUiEvent.OnEmpleadoSelected(empleado)) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.horasSemanalesInput,
                onValueChange = { viewModel.onEvent(HorasExtrasUiEvent.OnHorasSemanalesChanged(it)) },
                label = { Text("Horas totales en la semana") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.horasSemanalesError != null,
                supportingText = state.horasSemanalesError?.let { { Text(it) } },
                enabled = state.selectedEmpleado != null
            )

            OutlinedTextField(
                value = state.horasNocturnasInput,
                onValueChange = { viewModel.onEvent(HorasExtrasUiEvent.OnHorasNocturnasChanged(it)) },
                label = { Text("De esas, ¿cuántas fueron nocturnas?") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.horasNocturnasError != null,
                supportingText = state.horasNocturnasError?.let { { Text(it) } },
                enabled = state.selectedEmpleado != null
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.onEvent(HorasExtrasUiEvent.Limpiar) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar")
                }
                Button(
                    onClick = { viewModel.onEvent(HorasExtrasUiEvent.Calcular) },
                    modifier = Modifier.weight(1f),
                    enabled = state.selectedEmpleado != null
                ) {
                    Text("Calcular")
                }
            }

            state.reporte?.let { reporte ->
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Detalle de Nómina Semanal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        HorizontalDivider()
                        DetalleRow("Sueldo por hora base:", formatDinero(reporte.sueldoPorHora))
                        DetalleRow("Horas Ordinarias (${reporte.horasOrdinarias}):", formatDinero(reporte.pagoOrdinario))
                        DetalleRow("Horas Extras 35% (${reporte.horasExtras35}):", formatDinero(reporte.pagoExtra35))
                        DetalleRow("Horas Extras 100% (${reporte.horasExtras100}):", formatDinero(reporte.pagoExtra100))
                        DetalleRow("Recargo Nocturno 15%:", formatDinero(reporte.recargoNocturno))
                        HorizontalDivider()
                        DetalleRow("TOTAL A PAGAR:", formatDinero(reporte.totalPagar), isBold = true)
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleRow(etiqueta: String, valor: String, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = etiqueta, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(text = valor, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, color = if(isBold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
    }
}

fun formatDinero(valor: Double): String {
    return String.format(Locale.US, "$%.2f", valor)
}