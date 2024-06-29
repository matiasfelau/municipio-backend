package ar.edu.uade.mappers.requests

import ar.edu.uade.utilities.BigDecimalComponentSerializer
import ar.edu.uade.utilities.LocalTimeComponentSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalTime

@Serializable
data class ComercioRequest(
    val idComercio: Int,
    val nombre: String,
    @Serializable(with = LocalTimeComponentSerializer::class)
    val apertura: LocalTime?,
    @Serializable(with = LocalTimeComponentSerializer::class)
    val cierre: LocalTime?,
    val direccion: String?,
    val telefono: Int?,
    val descripcion: String?,
    @Serializable(with = BigDecimalComponentSerializer::class)
    val latitud: BigDecimal?,
    @Serializable(with = BigDecimalComponentSerializer::class)
    val longitud: BigDecimal?,
    val documento: String?
)