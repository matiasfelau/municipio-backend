package ar.edu.uade.daos

import ar.edu.uade.models.Vecino


interface VecinoDAOFacade {
    suspend fun findVecinoByDocumento(documento:String): Vecino?
}