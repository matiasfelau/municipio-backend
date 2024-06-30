package ar.edu.uade.services

import ar.edu.uade.daos.MovimientoDenunciaDAOFacade
import ar.edu.uade.daos.MovimientoDenunciaDAOFacadeMySQL
import ar.edu.uade.models.MovimientoDenuncia

class MovimientoDenunciaService() {
    private val dao: MovimientoDenunciaDAOFacade = MovimientoDenunciaDAOFacadeMySQL()

    suspend fun getByDelay(): List<MovimientoDenuncia> = dao.getMovimientosByDelay()

    suspend fun getMovimientosDenuncia(id: Int): List<MovimientoDenuncia> = dao.getMovimientosReclamo(id)
}