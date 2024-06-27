package ar.edu.uade.daos

import ar.edu.uade.models.Vecino

interface VecinoDAOFacade {
    suspend fun verifyVecino(documento: String): Boolean
    suspend fun findVecino(documento: String): Vecino?
    suspend fun getVecinoSegunNomApDir(nombre: String,apellido: String, direccion: String):Vecino?
}