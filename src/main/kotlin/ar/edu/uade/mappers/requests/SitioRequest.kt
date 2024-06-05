package ar.edu.uade.mappers.requests

import ar.edu.uade.utilities.BigDecimalComponentSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class SitioRequest(
    @Serializable(with = BigDecimalComponentSerializer::class)
    val latitud: BigDecimal,
    @Serializable(with = BigDecimalComponentSerializer::class)
    val longitud: BigDecimal,
    val descripcion: String
)
