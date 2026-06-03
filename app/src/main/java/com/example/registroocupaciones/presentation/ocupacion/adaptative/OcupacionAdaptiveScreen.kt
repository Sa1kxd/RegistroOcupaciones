package com.example.registroocupaciones.presentation.ocupacion.adaptative

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
import com.example.registroocupaciones.presentation.ocupacion.edit.OcupacionFormScreen
import com.example.registroocupaciones.presentation.ocupacion.list.OcupacionListScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OcupacionAdaptiveScreen() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()
    var selectedOcupacionId by remember { mutableIntStateOf(0) }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            OcupacionListScreen(
                onCreateNew = {
                    selectedOcupacionId = 0
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                },
                onNavigateToEdit = { id ->
                    selectedOcupacionId = id
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                }
            )
        },
        detailPane = {
            OcupacionFormScreen(
                ocupacionId = selectedOcupacionId,
                onBack = {
                    scope.launch {
                        navigator.navigateBack()
                    }
                }
            )
        }
    )
}