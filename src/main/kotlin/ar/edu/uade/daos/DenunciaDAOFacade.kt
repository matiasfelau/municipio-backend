package ar.edu.uade.daos

import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.DenunciaImagen

interface DenunciaDAOFacade {
    suspend fun get10Denuncias(pagina: Int): List<Denuncia>
    suspend fun getDenunciaById(id: Int): Denuncia?
    //TODO Ver...
    suspend fun addDenuncia(denuncia: Denuncia): Denuncia?
    suspend fun addImagenToDenuncia(idDenuncia: Int, urlImagen: String)
    suspend fun getAllCantidadPaginas(): Int
    suspend fun getFotosById(id: Int): List<DenunciaImagen>
}