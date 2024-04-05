package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import java.io.Serializable
import java.time.LocalTime
import ar.edu.uade.models.Reclamo.*
import org.jetbrains.exposed.sql.javatime.time

data class movimientoReclamo(
    val idMovimiento: Int,
    val idReclamo: Int,
    val responsable: String,
    val causa: String,
    val fecha: LocalTime
) : Serializable{
    public object movimientosReclamo: Table(){
        val idMovimiento = integer("idMovimiento").autoIncrement()
        val idReclamo = integer("idReclamo").uniqueIndex().references(Reclamos.idReclamo)
        val responsable = varchar("responsable",150)
        val causa = varchar("causa",1000)
        val fecha = time("fecha")
    }
}
