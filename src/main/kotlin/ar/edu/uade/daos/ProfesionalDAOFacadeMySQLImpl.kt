package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Credencial.Credenciales
import ar.edu.uade.models.Profesional
import ar.edu.uade.models.Profesional.Profesionales
import ar.edu.uade.models.ImagenProfesional
import ar.edu.uade.models.ImagenProfesional.ImagenesProfesional
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.math.BigDecimal
import java.time.LocalTime

class ProfesionalDAOFacadeMySQLImpl: ProfesionalDAOFacade {
    private fun resultRowToProfesional(row: ResultRow) = Profesional(
        idProfesional = row[Profesionales.idProfesional],
        nombre = row[Profesionales.nombre],
        rubro = row[Profesionales.rubro],
        descripcion = row[Profesionales.descripcion],
        direccion = row[Profesionales.direccion],
        telefono = row[Profesionales.telefono],
        email = row[Profesionales.email],
        latitud = row[Profesionales.latitud],
        longitud = row[Profesionales.longitud],
        inicioJornada = row[Profesionales.inicioJornada],
        finJornada = row[Profesionales.finJornada],
        documento = row[Profesionales.documento],
        autorizado = row[Profesionales.autorizado]
    )

    private fun resultRowToImagenProfesional(row: ResultRow) = ImagenProfesional(
        idImagen = row[ImagenesProfesional.idImagen],
        urlImagen = row[ImagenesProfesional.urlImagen],
        idProfesional = row[ImagenesProfesional.idProfesional]
    )

    override suspend fun getCantidadElementos(): Int = dbQuery {
        Profesionales.select { Profesionales.autorizado eq true }
            .map(::resultRowToProfesional)
            .count()
    }

    override suspend fun get10Profesionales(pagina: Int): List<Profesional> = dbQuery {
        val offset = (pagina - 1) * 10
        Profesionales.select { Profesionales.autorizado eq true }
            .limit(10, offset.toLong())
            .map(::resultRowToProfesional)
    }

    override suspend fun addProfesional(nombre: String,
                                        rubro: String,
                                        descripcion: String?,
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
            it[Profesionales.rubro] = rubro
            it[Profesionales.descripcion] = descripcion
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

    override suspend fun getFotos(idProfesional: Int): List<ImagenProfesional> = dbQuery {
        ImagenesProfesional.select { ImagenesProfesional.idProfesional eq idProfesional }.map(::resultRowToImagenProfesional)
    }

    override suspend fun habilitarProfesional(idProfesional: Int): Boolean = dbQuery {
        Profesionales.update({ Profesionales.idProfesional eq idProfesional }) {
            it[Profesionales.autorizado] = true
        } > 0
    }

    override suspend fun getProfesional(idProfesional: Int): Profesional? = dbQuery {
        Profesionales.select{Profesionales.idProfesional eq idProfesional}.map(::resultRowToProfesional).singleOrNull()
    }
}