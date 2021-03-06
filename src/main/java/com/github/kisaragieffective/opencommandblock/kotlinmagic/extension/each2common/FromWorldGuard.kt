package com.github.kisaragieffective.opencommandblock.kotlinmagic.extension.each2common

import com.github.kisaragieffective.opencommandblock.OpenCommandBlock
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.ActionAnswer
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.EntitiesGroup
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.EntityAction
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.IEntityGroup
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.IGlobalRegion
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.IPolygon2DRegion
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.IRectangleRegion
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.IRegion
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.RegionSetting
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.RegionStructure
import com.github.kisaragieffective.opencommandblock.exception.DevelopStepException
import com.github.kisaragieffective.opencommandblock.kotlinmagic.extension.toEnumMap
import com.github.kisaragieffective.opencommandblock.kotlinmagic.extension.worldguard.asBukkitVector
import com.github.kisaragieffective.opencommandblock.kotlinmagic.notImplemented
import com.sk89q.worldguard.bukkit.WGBukkit
import com.sk89q.worldguard.protection.flags.BooleanFlag
import com.sk89q.worldguard.protection.flags.DoubleFlag
import com.sk89q.worldguard.protection.flags.EntityTypeFlag
import com.sk89q.worldguard.protection.flags.EnumFlag
import com.sk89q.worldguard.protection.flags.IntegerFlag
import com.sk89q.worldguard.protection.flags.LocationFlag
import com.sk89q.worldguard.protection.flags.SetFlag
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.flags.StringFlag
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.EnumMap

internal fun ProtectedRegion.toFrameworkStyle(): IRegion {
    if (this is ProtectedCuboidRegion) {
        return this.toFrameworkStyle()
    }

    if (this is ProtectedPolygonalRegion) {
        return this.toFrameworkStyle()
    }

    if (this is GlobalProtectedRegion) {
        return this.toFrameworkStyle()
    }

    notImplemented()
}

private fun GlobalProtectedRegion.toFrameworkStyle(): IGlobalRegion {
    return object : IGlobalRegion {
        private val region: ProtectedRegion = this@toFrameworkStyle
        override val type: RegionStructure
            get() = RegionStructure.GLOBAL
        override val world: World
            get() = Bukkit.getWorlds().filter {
                WGBukkit.getRegionManager(it).regions.containsValue(region)
            }[0]
        override val name: String
            get() = region.id

        override fun test(actor: Entity, action: EntityAction): ActionAnswer {
            notImplemented()
        }

        override fun getEntityPermissions(): Map<Pair<IEntityGroup, EntityAction>, ActionAnswer> {
            notImplemented()
        }

        /**
         * Actually, the most key is [com.github.kisaragieffective.opencommandblock.api.wrapper.region.Setting], but to complete Dependency Injection, you'll have to check the keys.
         */
        override fun <E : Enum<E>, S : Any> getRegionSettings(): EnumMap<E, RegionSetting<S>> {
            // notImplemented()
            return emptyMap<E, RegionSetting<S>>().toEnumMap()
        }

        override val members: IEntityGroup
            get() {
                return EntitiesGroup(region.members.uniqueIds.map {
                    Bukkit.getEntity(it)
                })
            }

        override val owners: IEntityGroup
            get() {
                return EntitiesGroup(region.owners.uniqueIds.map {
                    Bukkit.getEntity(it)
                })
            }
    }
}

private fun ProtectedPolygonalRegion.toFrameworkStyle(): IPolygon2DRegion {
    return object : IPolygon2DRegion {
        private val region: ProtectedRegion = this@toFrameworkStyle
        /**
         * @inherited
         */
        override val parent: IRegion?
            get() = region.parent!!.toFrameworkStyle()
        override val world: World
            get() = Bukkit.getWorlds().filter {
                WGBukkit.getRegionManager(it).regions.containsValue(region)
            }[0]
        override val name: String
            get() = region.id

        override fun test(actor: Entity, action: EntityAction): ActionAnswer {
            notImplemented()
        }

        override operator fun contains(other: Location): Boolean {
            notImplemented()
        }

        override fun getEntityPermissions(): Map<Pair<IEntityGroup, EntityAction>, ActionAnswer> {
            notImplemented()
        }

        override fun <E : Enum<E>, S : Any> getRegionSettings(): EnumMap<E, RegionSetting<S>> {
            notImplemented()
        }

        override val members: IEntityGroup
            get() = notImplemented()
        override val owners: IEntityGroup
            get() {
                return EntitiesGroup(region.owners.uniqueIds.map {
                    Bukkit.getEntity(it)
                })
            }

        override fun getPoints(): Collection<Location> {
            TODO()
        }

    }
}

private fun ProtectedCuboidRegion.toFrameworkStyle(): IRectangleRegion {
    return object : IRectangleRegion {
        private val region: ProtectedRegion = this@toFrameworkStyle
        override val world: World
            get() = Bukkit.getWorlds().filter {
                WGBukkit.getRegionManager(it).regions.containsValue(region)
            }[0]
        /**
         * @inherited
         */
        override val parent: IRegion?
            get() = region.parent?.toFrameworkStyle()

        override fun test(actor: Entity, action: EntityAction): ActionAnswer {
            val query = WGBukkit.getPlugin().regionContainer.createQuery()
            return when (query.queryState(min.toLocation(world), actor as Player, *region.flags.keys.filter {
                it is StateFlag
            }.map{
                it as StateFlag
            }.toTypedArray())) {
                StateFlag.State.ALLOW -> ActionAnswer.ALLOWED
                StateFlag.State.DENY -> ActionAnswer.DENIED
                else -> throw DevelopStepException("This statement should not reached")
            }
        }

        override fun contains(other: Location): Boolean {
            return region.contains(other.x.toInt(), other.y.toInt(), other.z.toInt())
        }

        override fun getEntityPermissions(): Map<Pair<IEntityGroup, EntityAction>, ActionAnswer> {
            val ret: Map<Pair<IEntityGroup, EntityAction>, ActionAnswer> = mutableMapOf()
            region.flags.entries.forEach {
                // the name is Flag#getName, the value is actual flag value.
                OpenCommandBlock.instance.value!!.logger.info("${it.key}:${it.value}")
            }
            notImplemented()
        }

        override fun <E : Enum<E>, S : Any> getRegionSettings(): EnumMap<E, RegionSetting<S>> {
            region.flags.entries.map {
                Pair(when (it.key) {
                    is IntegerFlag -> RegionSetting(it.value as Int)
                    is DoubleFlag -> RegionSetting(it.value as Double)
                    is BooleanFlag -> RegionSetting(it.value as Boolean)
                    /*
                    is CommandStringFlag -> RegionSetting(if ((it.value as CommandStringFlag) ) {
                        ActionAnswer.ALLOWED
                    } else {
                        ActionAnswer.DENIED
                    })
                    */
                    is SetFlag<*> -> RegionSetting(it.value as Set<*>)
                    is EntityTypeFlag -> RegionSetting(it.value as EntityType)
                    is LocationFlag -> RegionSetting(it.value as Location)
                    is StringFlag -> RegionSetting(it.value as String)
                    is EnumFlag<*> -> RegionSetting(it.value as Enum<*>)
                    is StateFlag -> RegionSetting(if (it.value as StateFlag.State == StateFlag.State.ALLOW) {
                        ActionAnswer.ALLOWED
                    } else {
                        ActionAnswer.DENIED
                    })
                    else -> UnsupportedOperationException("Flag ${it.key} is not supported yet.")
                }, it.value)
            }
            notImplemented()
        }

        override val members: IEntityGroup
            get() {
                return EntitiesGroup(region.members.uniqueIds.map {
                    Bukkit.getEntity(it)
                })
            }

        override val owners: IEntityGroup
            get() {
                return EntitiesGroup(region.owners.uniqueIds.map {
                    Bukkit.getEntity(it)
                })
            }

        override val min: Vector
            get() = region.minimumPoint.asBukkitVector()
        override val max: Vector
            get() = region.maximumPoint.asBukkitVector()
        override val name: String
            get() = region.id

        override fun toString(): String {
            return "${IRectangleRegion::class.java.canonicalName}{name=$name,where={world=$world,min=$min,max=$max},owner=$owners,member=$members,parent=$parent}"
        }
    }
}

