package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable as KotlinSerializable
import ar.edu.uade.utilities.LocalDateTimeComponentSerializable
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MovimientoReclamoResponse (
    val idMovimiento: Int,
    val responsable: String?,
    val causa: String?,
   @KotlinSerializable(with = LocalDateTimeComponentSerializable::class)
    val fecha: LocalDateTime,
    val idReclamo: Int
)