package ar.edu.uade.services

import ar.edu.uade.daos.MovimientoReclamoDAOFacade
import ar.edu.uade.daos.MovimientoReclamoDAOFacadeMySQLImpl
import ar.edu.uade.models.MovimientoReclamo

class MovimientoReclamoService {
    private val dao: MovimientoReclamoDAOFacade = MovimientoReclamoDAOFacadeMySQLImpl()

    suspend fun getByDelay(): List<MovimientoReclamo> = dao.getMovimientosByDelay()

    suspend fun getMovimientosReclamo(id: Int): List<MovimientoReclamo> = dao.getMovimientosReclamo(id)
}