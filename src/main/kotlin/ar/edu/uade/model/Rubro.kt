package ar.edu.uade.model

import org.jetbrains.exposed.sql.Table

data class Rubro(val idRubro: Int,
                 val descripcion: String
) {
    public object Rubros : Table() {
        val idRubro = integer("idRubro").autoIncrement()
        val descripcion = varchar("descripcion", 200)
        override val primaryKey = PrimaryKey(idRubro)
    }
}
