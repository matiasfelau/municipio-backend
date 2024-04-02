package ar.edu.uade.model

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.Date

data class Personal(
    val legajo: Int,
    val nombre: String,
    val apellido: String,
    val password: String,
    val sector: String,
    val categoria: Int = 8,
    val fechaIngreso: Date
) {
    object Personal : Table() {
        val legajo = integer("legajo").autoIncrement()
        val nombre = varchar("nombre", 150)
        val apellido = varchar("apellido", 150)
        val password = varchar("password", 40)
        val sector = varchar("sector", 200)
        val categoria = integer("categoria")
        val fechaIngreso = datetime("fechaIngreso")
        override val primaryKey = PrimaryKey(legajo)
    }
}
