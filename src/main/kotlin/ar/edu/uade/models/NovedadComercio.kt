package ar.edu.uade.models

import com.sun.jdi.IntegerValue
import java.io.Serializable
import org.jetbrains.exposed.sql.Table
import ar.edu.uade.models.Comercio.*

data class NovedadComercio (
    val idNovedad : Int,
    val texto : String,
    val titulo : String,
    val idComercio : Int
) : Serializable{
    public object novedadesComercio: Table(){
        val idNovedad = integer("idNovedad").autoIncrement()
        val text = text("texto")
        val titulo = varchar("titulo",40)
        val idComercio = integer("idComercio").uniqueIndex().references(Comercios.idComercio)
    }
}
