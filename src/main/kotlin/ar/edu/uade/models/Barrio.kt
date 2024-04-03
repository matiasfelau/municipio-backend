package ar.edu.uade.models

import ar.edu.uade.models.Rubro.Rubros.autoIncrement
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class Barrio(
    val idBarrio: Int,
    val nombre: String
): Serializable {
    public object Barrios : Table() {
        val idBarrio = integer("idBarrio").autoIncrement()
        val nombre = varchar("nombre",150)
        override val primaryKey = PrimaryKey(idBarrio)
    }
}
