package ar.edu.uade.daos

import ar.edu.uade.models.ComercioDenunciado
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.DenunciaImagen
import ar.edu.uade.models.VecinoDenunciado

interface DenunciaDAOFacade {
    suspend fun get10Denuncias(pagina: Int, documento: String): List<Denuncia>
    suspend fun getDenunciaById(id: Int): Denuncia?
    //TODO Ver...
    suspend fun addDenuncia(denuncia: Denuncia): Denuncia?
    suspend fun getVecinoDenunciado(id: Int): VecinoDenunciado?
    suspend fun getComercioDenunciado(id: Int): ComercioDenunciado?
    suspend fun addComercioDenunciado(idDenuncia: Int, comercioDenunciado: ComercioDenunciado)
    suspend fun addVecinoDenunciado(idDenuncia: Int, vecinoDenunciado: VecinoDenunciado)
    suspend fun addImagenToDenuncia(idDenuncia: Int, urlImagen: String)
    suspend fun getAllCantidadPaginas(): Int
    suspend fun getFotosById(id: Int): List<DenunciaImagen>
}