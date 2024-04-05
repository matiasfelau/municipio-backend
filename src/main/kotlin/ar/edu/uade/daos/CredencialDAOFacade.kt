package ar.edu.uade.daos

import ar.edu.uade.models.Credencial


interface CredencialDAOFacade {
    suspend fun findCredencialByDocumento(documento:String): Credencial?
}