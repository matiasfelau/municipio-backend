package ar.edu.uade.services

import ar.edu.uade.daos.ProfesionalDAO
import ar.edu.uade.daos.ProfesionalDAOImpl
import ar.edu.uade.daos.ReclamoDAOFacade
import ar.edu.uade.daos.ReclamoDAOFacadeMySQLImpl
import ar.edu.uade.models.Profesional
import kotlin.math.ceil

class ProfesionalService {
    private val dao: ProfesionalDAO = ProfesionalDAOImpl()

    suspend fun getCantidadPaginas(): Int {
        return ceil(dao.getCantidadPaginas().toDouble()/10).toInt()
    }

    suspend fun get10Profesionales(pagina: Int): List<Profesional> {
        return dao.get10Profesionales(pagina)
    }
}