package ar.edu.uade.daos

import ar.edu.uade.models.Comercio
import ar.edu.uade.models.ComercioImagen
import java.math.BigDecimal
import java.time.LocalTime

interface ComercioDAOFacade {
    suspend fun getallCantidadPaginas(): Int
    suspend fun getComercioByID(id: Int): Comercio?
    suspend fun get10Comercio(pagina: Int): List<Comercio>
    suspend fun addComercio(nombre:String, documento: String?, direccion: String?, descripcion:String?, telefono: Int?, apertura: LocalTime?, cierre: LocalTime?, latitud: BigDecimal?, longitud: BigDecimal?): Comercio?
    suspend fun addImagenToComercio(urlImagen: String,idComercio: Int): ComercioImagen?
    suspend fun getFotosById(id: Int): List<ComercioImagen>
    suspend fun getComercioByNomYDir(nombre: String, direccion: String): Comercio?
    suspend fun habilitarComercio(idComercio: Int): Boolean
    suspend fun getComerciosByVecino(documentoVecino: String): List<Comercio>
}