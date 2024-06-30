package ar.edu.uade.mappers

import ar.edu.uade.utilities.BigDecimalComponentSerializer
import ar.edu.uade.utilities.LocalTimeComponentSerializer
import ar.edu.uade.utilities.UriComponentSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.net.URI
import java.time.LocalTime

@Serializable
data class MapProfesional(
    val nombre: String,
    val direccion: String?,
    val telefono: Int?,
    val email: String?,
    @Serializable(with = BigDecimalComponentSerializer::class)
    val latitud: BigDecimal?,
    @Serializable(with = BigDecimalComponentSerializer::class)
    val longitud: BigDecimal?,
    @Serializable(with = LocalTimeComponentSerializer::class)
    val inicioJornada: LocalTime?,
    @Serializable(with = LocalTimeComponentSerializer::class)
    val finJornada: LocalTime?,
    val documento: String,
    val images: List<String>
) {}