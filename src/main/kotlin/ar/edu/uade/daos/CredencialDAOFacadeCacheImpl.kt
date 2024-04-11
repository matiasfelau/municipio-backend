package ar.edu.uade.daos

import ar.edu.uade.models.Empleado
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration
import java.io.File
import ar.edu.uade.models.Credencial


class CredencialDAOFacadeCacheImpl(private val delegate: CredencialDAOFacade, storagePath: File) : CredencialDAOFacade {
    private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerPersistenceConfiguration(storagePath))
        .withCache(
            "credencialesCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Int::class.javaObjectType,
                Empleado::class.java,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(1000, EntryUnit.ENTRIES)
                    .offheap(10, MemoryUnit.MB)
                    .disk(100, MemoryUnit.MB, true)
            )
        )
        .build(true)

    private val credencialesCache = cacheManager.getCache("credencialesCache", String::class.javaObjectType, Credencial::class.java)
    override suspend fun addNewCredencial(documento: String, password: String): Credencial? = delegate.addNewCredencial(documento, password)?.also{
        credencial -> credencialesCache.put(documento,credencial)

    }

    override suspend fun findCredencialByDocumento(documento: String): Credencial? = credencialesCache[documento] ?: delegate.findCredencialByDocumento(documento)
        .also { credencial -> credencialesCache.put(documento, credencial) }
}