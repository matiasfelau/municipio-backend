package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import java.io.Serializable
import ar.edu.uade.models.Sitio.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID


data class Clasificacion(
    val id: Int,
    val nombre:String,
    val idSitio:Int
) : Serializable{
    public object Clasificaciones : Table(){
        val id = integer("id").autoIncrement()
        val nombre = varchar("nombre",40)
        val idSitio = integer("idSitio").uniqueIndex().references(Sitios.idSitio)
    }
}

