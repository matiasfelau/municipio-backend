package ar.edu.uade.models
import org.jetbrains.exposed.sql.Table
import java.io.Serializable
import ar.edu.uade.models.Barrio.*

data class Vecino(
    val documento:String,
    val nombre:String,
    val apellido:String,
    val direccion:String?,
    val codigoDeBarrio:Int?
): Serializable {
    public object Vecinos: Table() {
        val documento =varchar("documento",40)
        val nombre = varchar("nombre",150)
        val apellido = varchar("apellido",150)
        val direccion = varchar("direccion",250)
        val codigoDeBarrio = integer("codigoDeBarrio").uniqueIndex().references(Barrios.idBarrio)
        override val primaryKey = PrimaryKey(documento)
    }
}