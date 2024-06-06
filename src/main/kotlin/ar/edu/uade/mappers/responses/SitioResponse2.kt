package ar.edu.uade.mappers.responses

import ar.edu.uade.utilities.BigDecimalComponentSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class SitioResponse2(
    val idSitio: Int?,
    @kotlinx.serialization.Serializable(with = BigDecimalComponentSerializer::class)
    val latitud: BigDecimal,
    @kotlinx.serialization.Serializable(with = BigDecimalComponentSerializer::class)
    val longitud: BigDecimal
)
