package ar.edu.uade.repository

import ar.edu.uade.model.*
import org.ehcache.config.builders.*
import org.ehcache.config.units.*
import org.ehcache.impl.config.persistence.*
import java.io.*

class RubroDAOFacadeCacheImpl(private val delegate: RubroDAOFacade, storagePath: File) : RubroDAOFacade {
    private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerPersistenceConfiguration(storagePath))
        .withCache(
            "rubroCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Int::class.javaObjectType,
                Rubro::class.java,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(1000, EntryUnit.ENTRIES)
                    .offheap(10, MemoryUnit.MB)
                    .disk(100, MemoryUnit.MB, true)
            )
        )
        .build(true)

    private val rubroCache = cacheManager.getCache("rubroCache", Int::class.javaObjectType, Rubro::class.java)

    override suspend fun allRubros(): List<Rubro> = delegate.allRubros()

}