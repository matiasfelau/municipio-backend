package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Profesional
import ar.edu.uade.models.Reclamo
import ar.edu.uade.models.Reclamo.Reclamos
import org.jetbrains.exposed.sql.ResultRow
import ar.edu.uade.models.Profesional.Profesionales
import org.jetbrains.exposed.sql.selectAll

class ProfesionalDAOImpl: ProfesionalDAO {
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

    override suspend fun getCantidadPaginas(): Int = dbQuery {
        Profesionales.selectAll()
            .map(::resultRowToProfesional)
            .count()
    }

    override suspend fun get10Profesionales(pagina: Int): List<Profesional> = dbQuery {
        val offset = (pagina - 1) * 10
        Profesionales.selectAll()
            .limit(10,offset.toLong())
            .map(::resultRowToProfesional)
    }
}