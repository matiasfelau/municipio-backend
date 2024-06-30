package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Profesional
import org.jetbrains.exposed.sql.ResultRow
import ar.edu.uade.models.Profesional.Profesionales
import ar.edu.uade.models.ImagenProfesional
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import ar.edu.uade.models.ImagenProfesional.ImagenesProfesional
import java.math.BigDecimal
import java.time.LocalTime

class ProfesionalDAOFacadeMySQLImpl: ProfesionalDAOFacade {
    private fun resultRowToProfesional(row: ResultRow) = Profesional(
        idProfesional = row[Profesionales.idProfesional],
        nombre = row[Profesionales.nombre],
        direccion = row[Profesionales.direccion],
        telefono = row[Profesionales.telefono],
        email = row[Profesionales.email],
        latitud = row[Profesionales.latitud],
        longitud = row[Profesionales.longitud],
        inicioJornada = row[Profesionales.inicioJornada],
        finJornada = row[Profesionales.finJornada],
        documento = row[Profesionales.documento]
    )

    private fun resultRowToImagenProfesional(row: ResultRow) = ImagenProfesional(
        idImagen = row[ImagenesProfesional.idImagen],
        urlImagen = row[ImagenesProfesional.urlImagen],
        idProfesional = row[ImagenesProfesional.idProfesional]
    )

    override suspend fun getCantidadElementos(): Int = dbQuery {
        Profesionales.selectAll()
            .map(::resultRowToProfesional)
            .count()
    }

    override suspend fun get10Profesionales(pagina: Int): List<Profesional> = dbQuery {
        val offset = (pagina - 1) * 10
        Profesionales.selectAll()
            .limit(10, offset.toLong())
            .map(::resultRowToProfesional)
    }

    override suspend fun addProfesional(nombre: String,
                                        direccion: String?,
                                        telefono: Int?,
                                        email: String?,
                                        latitud: BigDecimal?,
                                        longitud: BigDecimal?,
                                        inicioJornada: LocalTime?,
                                        finJornada: LocalTime?,
                                        documento: String): Profesional? = dbQuery {
        val insertStatement = Profesionales.insert {
            it[Profesionales.nombre] = nombre
            it[Profesionales.direccion] = direccion
            it[Profesionales.telefono] = telefono
            it[Profesionales.email] = email
            it[Profesionales.latitud] = latitud
            it[Profesionales.longitud] = longitud
            it[Profesionales.inicioJornada] = inicioJornada
            it[Profesionales.finJornada] = finJornada
            it[Profesionales.documento] = documento
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToProfesional)
    }

    override suspend fun addImagenToProfesional(urlImagen: String, idProfesional: Int): ImagenProfesional? = dbQuery{
        val insertStatement = ImagenesProfesional.insert {
            it[ImagenesProfesional.urlImagen] = urlImagen
            it[ImagenesProfesional.idProfesional] = idProfesional
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToImagenProfesional)
    }
}