package ar.edu.uade.daos

import ar.edu.uade.models.Comercio
import ar.edu.uade.models.ComercioImagen

interface ComercioDAOFacade {
    suspend fun getallCantidadPaginas(): Int

    suspend fun getComercioByID(id: Int): Comercio?
    suspend fun get10Comercio(pagina: Int): List<Comercio>
    suspend fun addComercio(comercio: Comercio): Comercio?
    suspend fun addImagenToComercio(idComercio: Int, urlImagen: String)
    suspend fun getFotosById(id: Int): List<ComercioImagen>

}