package ar.edu.uade.mappers

import ar.edu.uade.utilities.BigDecimalComponentSerializer
import ar.edu.uade.utilities.LocalTimeComponentSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalTime
@Serializable
data class MapComercio(
    val nombre: String,
    val documento: String?,
    val direccion: String?,
    val descripcion: String?,
    val telefono: Int?,
    @Serializable(with = LocalTimeComponentSerializer::class)
    val apertura: LocalTime?,
    @Serializable(with = LocalTimeComponentSerializer::class)
    val cierre: LocalTime?,
    @Serializable(with = BigDecimalComponentSerializer::class)
    val latitud: BigDecimal?,
    @Serializable(with = BigDecimalComponentSerializer::class)
    val longitud: BigDecimal?,
    val images: List<String>
)