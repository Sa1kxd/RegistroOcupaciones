package com.example.registroempleados.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.registroempleados.presentation.empleado.form.EmpleadoFormScreen
import com.example.registroempleados.presentation.empleado.list.EmpleadoListScreen

@Composable
fun EmpleadoNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.EmpleadoList
    ) {
        composable<Screen.EmpleadoList> {
            EmpleadoListScreen(
                onAddEmpleado = {
                    navController.navigate(Screen.EmpleadoForm(0))
                },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EmpleadoForm(id))
                }
            )
        }

        composable<Screen.EmpleadoForm> {
            EmpleadoFormScreen(
                onBack = {
                    navController.navigate(Screen.EmpleadoList) {
                        popUpTo(Screen.EmpleadoList) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}