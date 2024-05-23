package ar.edu.uade.models

import org.jetbrains.exposed.sql.Table
import java.io.Serializable


data class NovedadProfesional(
    val idNovedad: Int,
    val texto: String,
    val titulo: String,
    val idProfesional: Int
): Serializable{
    public object NovedadesProfesional: Table(){
        val idNovedad = integer("idNovedad").autoIncrement()
        val texto = text("texto")
        val titulo = varchar("titulo",40)
        val idProfesional = integer("idProfesional").uniqueIndex().references(Profesional.Profesionales.idProfesional)
        override val primaryKey = PrimaryKey(idNovedad)
    }
}