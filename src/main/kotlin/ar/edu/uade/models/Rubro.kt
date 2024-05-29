package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class Rubro(
    val idRubro: Int,
    val descripcion: String?
): Serializable {
    public object Rubros: Table() {
        val idRubro = integer("idRubro").autoIncrement()
        val descripcion = varchar("descripcion", 200)
        override val primaryKey = PrimaryKey(idRubro)
    }
}
