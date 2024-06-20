package ar.edu.uade.daos

import ar.edu.uade.models.MovimientoReclamo

interface MovimientoReclamoDAOFacade {
    suspend fun getMovimientosByDelay(): List<MovimientoReclamo>
    suspend fun getMovimientosReclamo(id: Int): List<MovimientoReclamo>
}