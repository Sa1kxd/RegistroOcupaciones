package com.example.registroempleados.presentation.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.registroempleados.presentation.empleado.adaptative.EmpleadoAdaptiveScreen
import com.example.registroempleados.presentation.empleado.horasextras.HorasExtrasScreen

@Composable
fun EmpleadoNavHost(
    navController: NavHostController = rememberNavController()
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    NavHost(
        navController = navController,
        startDestination = Screen.EmpleadoList
    ) {
        composable<Screen.EmpleadoList> {
            EmpleadoAdaptiveScreen(
                onNavigateToHorasExtras = {
                    navController.navigate(Screen.HorasExtras)
                }
            )
        }

        composable<Screen.HorasExtras> {
            HorasExtrasScreen(
                windowSizeClass = windowSizeClass,
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}