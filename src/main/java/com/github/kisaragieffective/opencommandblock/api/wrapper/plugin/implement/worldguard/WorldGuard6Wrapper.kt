package com.github.kisaragieffective.opencommandblock.api.wrapper.plugin.implement.worldguard

import com.github.kisaragieffective.opencommandblock.OpenCommandBlock
import com.github.kisaragieffective.opencommandblock.api.common.TargetVersionRange
import com.github.kisaragieffective.opencommandblock.api.common.Version
import com.github.kisaragieffective.opencommandblock.api.wrapper.plugin.WorldGuardWrapper
import com.github.kisaragieffective.opencommandblock.api.wrapper.region.IRegion
import com.github.kisaragieffective.opencommandblock.kotlinmagic.extension.each2common.toFrameworkStyle
import com.sk89q.worldguard.bukkit.WGBukkit
import org.bukkit.Location
import org.bukkit.entity.Player


object WorldGuard6Wrapper : WorldGuardWrapper {
    override fun isBuildable(who: Player, where: Location): Boolean {
        return WGBukkit.getPlugin().canBuild(who, where)
    }

    override fun getRegions(where: Location): Set<IRegion> {
        OpenCommandBlock.instance.value!!.logger.info(where.toString())
        val ret = WGBukkit.getRegionManager(where.world).getApplicableRegions(where).map {
            println(it)
            it.toFrameworkStyle()
        }.toSet()
        OpenCommandBlock.instance.value!!.logger.info(ret.joinToString(","))
        return ret
    }

    override val applicableVersion: TargetVersionRange
        get() = TargetVersionRange(Version(6, 0), Version(7, 0))
}

