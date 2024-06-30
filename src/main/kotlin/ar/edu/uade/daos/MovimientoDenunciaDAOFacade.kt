package ar.edu.uade.daos

import ar.edu.uade.models.MovimientoDenuncia

interface MovimientoDenunciaDAOFacade {
    suspend fun getMovimientosByDelay(): List<MovimientoDenuncia>
    suspend fun getMovimientosReclamo(id: Int): List<MovimientoDenuncia>
}