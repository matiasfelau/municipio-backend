package ar.edu.uade.daos

import ar.edu.uade.models.Credencial


interface CredencialDAOFacade {
    suspend fun addNewCredencial(documento: String, email: String): Credencial?
    suspend fun editPrimerIngresoCredencial(documento:String, password: String?, primerIngreso: Boolean?): Boolean
    suspend fun findCredencialByDocumento(documento:String): Credencial?
    suspend fun editHabilitadoCredencial(documento:String, password: String, habilitado:Boolean, primerIngreso:Boolean): Boolean
}