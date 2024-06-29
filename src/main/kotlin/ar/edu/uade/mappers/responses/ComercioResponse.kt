package ar.edu.uade.mappers.responses

import java.math.BigDecimal
import java.time.LocalTime

class ComercioResponse (
    val idComercio: Int,
    val nombre: String,
    val apertura: LocalTime?,
    val cierre: LocalTime?,
    val direccion: String?,
    val telefono: Int?,
    val descripcion: String?,
    val latitud: BigDecimal?,
    val longitud: BigDecimal?,
    val documento: String?
)