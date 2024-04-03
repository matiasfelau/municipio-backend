package ar.edu.uade.daos

import ar.edu.uade.models.*
import org.ehcache.config.builders.*
import org.ehcache.config.units.*
import org.ehcache.impl.config.persistence.*
import java.io.*

class EmpleadoDAOFacadeCacheImpl(private val delegate: EmpleadoDAOFacade, storagePath: File) : EmpleadoDAOFacade {
    private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerPersistenceConfiguration(storagePath))
        .withCache(
            "empleadosCache",
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

    private val empleadosCache = cacheManager.getCache("empleadosCache", Int::class.javaObjectType, Empleado::class.java)

    override suspend fun findEmpleadoByLegajo(legajo: Int): Empleado? = empleadosCache[legajo] ?: delegate.findEmpleadoByLegajo(legajo)
        .also { empleado -> empleadosCache.put(legajo, empleado) }

}