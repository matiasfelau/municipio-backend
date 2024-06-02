package ar.edu.uade.daos

import ar.edu.uade.models.Sitio

interface SitioDAOFacade {

    suspend fun getAllSitios(): List<Sitio>
}