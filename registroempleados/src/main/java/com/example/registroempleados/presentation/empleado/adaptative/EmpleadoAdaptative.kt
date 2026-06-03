package com.example.registroempleados.presentation.empleado.adaptative

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import com.example.registroempleados.presentation.empleado.form.EmpleadoFormScreen
import com.example.registroempleados.presentation.empleado.list.EmpleadoListScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun EmpleadoAdaptiveScreen(
    onNavigateToHorasExtras: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()
    var selectedEmpleadoId by remember { mutableIntStateOf(0) }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            EmpleadoListScreen(
                onAddEmpleado = {
                    selectedEmpleadoId = 0
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                },
                onNavigateToEdit = { id ->
                    selectedEmpleadoId = id
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                },
                onNavigateToHorasExtras = onNavigateToHorasExtras
            )
        },
        detailPane = {
            EmpleadoFormScreen(
                empleadoId = selectedEmpleadoId,
                onBack = {
                    scope.launch {
                        navigator.navigateBack()
                    }
                }
            )
        }
    )
}