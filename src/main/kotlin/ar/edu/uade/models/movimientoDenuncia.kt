package ar.edu.uade.models

import ar.edu.uade.mappers.LocalDateTimeComponentSerializable
import ar.edu.uade.mappers.LocalTimeComponentSerializer
import org.jetbrains.exposed.sql.Table
import java.io.Serializable
import java.time.LocalTime
import ar.edu.uade.models.Denuncia.*
import org.jetbrains.exposed.sql.javatime.time

data class movimientoDenuncia(
    val idMovimiento: Int,
    val idDenuncias: Int,
    val responsable: String,
    val causa: String,
    @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
    val fecha: LocalTime
): Serializable{
    public object movimientosDenuncia: Table(){
        val idMovimiento = integer("idMovimiento").autoIncrement()
        val idDenuncias = integer("idDenuncias").uniqueIndex().references(Denuncias.idDenuncias)
        val responsable = varchar("responsable",150)
        val causa = varchar("causa",1000)
        val fecha = time("fecha")
    }
}
