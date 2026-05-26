package com.example.registroempleados.domain.usecase

import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

data class ReporteHorasExtras(
    val sueldoPorHora: Double,
    val horasOrdinarias: Int,
    val pagoOrdinario: Double,
    val horasExtras35: Int,
    val pagoExtra35: Double,
    val horasExtras100: Int,
    val pagoExtra100: Double,
    val recargoNocturno: Double,
    val totalPagar: Double
)

class CalcularHorasExtrasUseCase @Inject constructor() {

    operator fun invoke(
        sueldoMensual: Double,
        horasTrabajadasSemanales: Int,
        horasNocturnas: Int = 0
    ): ReporteHorasExtras {
        val sueldoPorDia = sueldoMensual / 23.83
        val sueldoPorHora = sueldoPorDia / 8.0

        val jornadaNormal = 44
        val horasOrdinarias = min(horasTrabajadasSemanales, jornadaNormal)
        val horasExtrasTotales = max(0, horasTrabajadasSemanales - jornadaNormal)

        val horasExtras35 = min(horasExtrasTotales, 24)
        val horasExtras100 = max(0, horasExtrasTotales - 24)

        val pagoOrdinario = horasOrdinarias * sueldoPorHora

        val tarifa35 = sueldoPorHora * 1.35
        val tarifa100 = sueldoPorHora * 2.00

        val pagoExtra35 = horasExtras35 * tarifa35
        val pagoExtra100 = horasExtras100 * tarifa100

        val recargoNocturno = horasNocturnas * (sueldoPorHora * 0.15)
        val totalPagar = pagoOrdinario + pagoExtra35 + pagoExtra100 + recargoNocturno

        return ReporteHorasExtras(
            sueldoPorHora = sueldoPorHora,
            horasOrdinarias = horasOrdinarias,
            pagoOrdinario = pagoOrdinario,
            horasExtras35 = horasExtras35,
            pagoExtra35 = pagoExtra35,
            horasExtras100 = horasExtras100,
            pagoExtra100 = pagoExtra100,
            recargoNocturno = recargoNocturno,
            totalPagar = totalPagar
        )
    }
}