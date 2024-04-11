package ar.edu.uade.daos

import ar.edu.uade.models.Credencial


interface CredencialDAOFacade {

    suspend fun addNewCredencial(documento:String, password: String): Credencial?
    suspend fun findCredencialByDocumento(documento:String): Credencial?
}