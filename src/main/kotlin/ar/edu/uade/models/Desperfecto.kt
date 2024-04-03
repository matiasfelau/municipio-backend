package ar.edu.uade.models

import java.io.Serializable
import ar.edu.uade.models.Rubro.*
import org.jetbrains.exposed.sql.Table

data class Desperfecto(
    val idDesperfecto: Int,
    val descripcion: String,
    val idRubro: Int
): Serializable {
    public object Desperfectos : Table(){
        val idDesperfecto = integer("idDesperfecto").autoIncrement()
        val descripcion = varchar("descripcion",200)
        val idRubro = integer("idRubro").uniqueIndex().references(Rubros.idRubro)
        override val primaryKey = PrimaryKey(idDesperfecto)

    }
}
