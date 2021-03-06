package com.github.kisaragieffective.opencommandblock.api.selector.value

import com.github.kisaragieffective.opencommandblock.api.selector.SelectorProperty
import com.github.kisaragieffective.opencommandblock.api.selector.distance.CuboidArea
import com.github.kisaragieffective.opencommandblock.api.selector.distance.IDistanceArea
import com.github.kisaragieffective.opencommandblock.api.selector.distance.SphereArea
import com.github.kisaragieffective.opencommandblock.api.selector.distance.WholeArea
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.advancement.Advancement
import org.bukkit.entity.EntityType
import org.bukkit.scoreboard.Score
import org.bukkit.scoreboard.Team
import java.util.EnumSet

class SelectorPropertySetImpl12(
        val basePoint: Location,
        val distance: IDistanceArea = WholeArea,
        val scores: List<Score> = emptyList(),
        val tags: Map<String, Boolean> = emptyMap(),
        val team: Team? = null,
        val count: Int = 2147483647,
        val level: IntRange = 0..1_000_000,
        val gameMode: Set<GameMode> = EnumSet.allOf(GameMode::class.java),
        val name: String? = null,
        val xRotation: ClosedFloatingPointRange<Double> = 0.0..180.0,
        val yRotation: ClosedFloatingPointRange<Double> = 0.0..180.0,
        val type: EntityType? = null
        ) : ISelectorPropertySet {
    override fun getDistanceType(): IDistanceArea {
        return distance
    }

    override fun getBasePoint(): SelectorProperty<Location> {
        return SelectorProperty(basePoint)
    }

    override fun getSphereDistance(): SelectorProperty<SphereArea> {
        return if (distance is SphereArea) {
            SelectorProperty(distance)
        } else {
            throw UnsupportedOperationException("Sphere area requested but doesn't match")
        }
    }

    override fun getCuboidDistance(): SelectorProperty<CuboidArea> {
        return if (distance is CuboidArea) {
            SelectorProperty(distance)
        } else {
            throw UnsupportedOperationException("Cuboid area requested but doesn't match")
        }
    }

    override fun getScores(): SelectorProperty<List<Score>> {
        return SelectorProperty(scores)
    }

    override fun getTags(): SelectorProperty<Map<String, Boolean>> {
        return SelectorProperty(tags)
    }

    override fun getTeam(): SelectorProperty<Team> {
        return SelectorProperty(team)
    }

    override fun getCount(): SelectorProperty<Int> {
        return SelectorProperty(count)
    }

    override fun getLevel(): SelectorProperty<IntRange> {
        return SelectorProperty(level)
    }

    override fun getGameMode(): SelectorProperty<Set<GameMode>> {
        return SelectorProperty(gameMode)
    }

    override fun getName(): SelectorProperty<String> {
        return SelectorProperty(name)
    }

    override fun getAxisXRotation(): SelectorProperty<ClosedFloatingPointRange<Double>> {
        return SelectorProperty(xRotation)
    }

    override fun getAxisYRotation(): SelectorProperty<ClosedFloatingPointRange<Double>> {
        return SelectorProperty(yRotation)
    }

    override fun getEntityType(): SelectorProperty<EntityType> {
        return SelectorProperty(type)
    }

    override fun getNBT(): SelectorProperty<Any> {
        throw UnsupportedOperationException("1.12 doesn't support NBT tag specify")
    }

    override fun getAdvancements(): SelectorProperty<Set<Advancement>> {
        throw UnsupportedOperationException("1.12 doesn't support advancement specify")
    }
}