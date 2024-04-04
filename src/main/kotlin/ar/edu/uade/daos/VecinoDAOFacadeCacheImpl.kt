package ar.edu.uade.daos

import ar.edu.uade.models.Empleado
import ar.edu.uade.models.Vecino
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration
import java.io.File

class VecinoDAOFacadeCacheImpl(private val delegate: VecinoDAOFacade, storagePath: File) : VecinoDAOFacade {
    private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerPersistenceConfiguration(storagePath))
        .withCache(
            "vecinosCache",
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

    private val vecinosCache = cacheManager.getCache("vecinosCache", String::class.javaObjectType, Vecino::class.java)

    override suspend fun findVecinoByDocumento(documento: String): Vecino? = vecinosCache[documento] ?: delegate.findVecinoByDocumento(documento)
        .also { vecino -> vecinosCache.put(documento, vecino) }
}