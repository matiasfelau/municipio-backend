package ar.edu.uade.models

import ar.edu.uade.models.Barrio.Barrios.varchar
import org.jetbrains.exposed.sql.Table
import java.io.Serializable
import ar.edu.uade.models.Vecino.*

data class Credencial(
    val documento:String,
    val password:String,
    val email:String,
    val habilitado:Boolean,
    val primerIngreso:Boolean
) : Serializable {
    public object Credenciales: Table(){
        val documento = varchar("documento",40).uniqueIndex().references(Vecinos.documento)
        val password = varchar("password",40)
        val email = varchar("email", 255)
        val habilitado = bool("habilitado").default(false)
        val primerIngreso = bool("primerIngreso").default(false) //a true cuando se habilita
        override val primaryKey = PrimaryKey(documento)
    }
}
