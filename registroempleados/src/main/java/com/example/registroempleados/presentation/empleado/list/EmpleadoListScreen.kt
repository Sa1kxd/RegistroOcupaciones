package com.example.registroempleados.presentation.empleado.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registroempleados.domain.model.Empleado

@Composable
fun EmpleadoListScreen(
    viewModel: EmpleadoListViewModel = hiltViewModel(),
    onAddEmpleado: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            onAddEmpleado()
            viewModel.onEvent(EmpleadoListUiEvent.Load)
        }
    }

    LaunchedEffect(state.navigateToEditId) {
        state.navigateToEditId?.let { id ->
            onNavigateToEdit(id)
        }
    }

    EmpleadoListBody(
        state = state,
        onEvent = viewModel::onEvent,
        onAddTask = { viewModel.onEvent(EmpleadoListUiEvent.CreateNew) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoListBody(
    state: EmpleadoListUiState,
    onEvent: (EmpleadoListUiEvent) -> Unit,
    onAddTask: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(EmpleadoListUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Registro de Empleados") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar empleado"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            OutlinedTextField(
                value = state.textoFiltro,
                onValueChange = { onEvent(EmpleadoListUiEvent.OnTextoFiltroChanged(it)) },
                label = { Text("Buscar...") },
                modifier = Modifier.fillMaxWidth().testTag("input_search"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.filtroSeleccionado == "Ninguno",
                    onClick = { onEvent(EmpleadoListUiEvent.OnFiltroChanged("Ninguno")) },
                    label = { Text("Todos") }
                )
                FilterChip(
                    selected = state.filtroSeleccionado == "Nombre",
                    onClick = { onEvent(EmpleadoListUiEvent.OnFiltroChanged("Nombre")) },
                    label = { Text("Nombre") }
                )
                FilterChip(
                    selected = state.filtroSeleccionado == "Fecha",
                    onClick = { onEvent(EmpleadoListUiEvent.OnFiltroChanged("Fecha")) },
                    label = { Text("Fecha") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("loading")
                    )
                } else {
                    if (state.empleados.isEmpty()) {
                        Text(
                            text = "No hay empleados registrados",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .testTag("empty_message"),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = state.empleados,
                                key = { it.empleadoId }
                            ) { empleado ->
                                EmpleadoItem(
                                    empleado = empleado,
                                    onDelete = {
                                        onEvent(EmpleadoListUiEvent.Delete(empleado.empleadoId))
                                    },
                                    onClick = {
                                        onEvent(EmpleadoListUiEvent.Edit(empleado.empleadoId))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoItem(
    empleado: Empleado,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("empleado_item_${empleado.empleadoId}"),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = empleado.nombres,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Ingreso: ${empleado.fechaIngreso}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Sexo: ${empleado.sexo} | Sueldo: $${empleado.sueldo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("btn_delete_${empleado.empleadoId}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar empleado"
                )
            }
        }
    }
}