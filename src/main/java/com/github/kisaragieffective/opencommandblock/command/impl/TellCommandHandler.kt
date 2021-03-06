package com.github.kisaragieffective.opencommandblock.command.impl

import com.github.kisaragieffective.opencommandblock.OpenCommandBlock
import com.github.kisaragieffective.opencommandblock.api.LogAccessor
import com.github.kisaragieffective.opencommandblock.api.PlayerCommandInputOperation
import com.github.kisaragieffective.opencommandblock.api.commandblock.CommandBlockAccessor
import com.github.kisaragieffective.opencommandblock.command.CommandArgumentType
import com.github.kisaragieffective.opencommandblock.command.CommandTemplate
import com.github.kisaragieffective.opencommandblock.command.OCBTemplateAvailable
import com.github.kisaragieffective.opencommandblock.command.PlayerCommandListener
import com.github.kisaragieffective.opencommandblock.kotlinmagic.extension.each2common.toFrameworkStyle
import com.github.kisaragieffective.opencommandblock.kotlinmagic.extension.foot
import org.bukkit.command.Command
import org.bukkit.entity.Player

object TellCommandHandler : PlayerCommandListener, OCBTemplateAvailable {
    override fun template(): CommandTemplate {
        return CommandTemplate("")
    }

    override fun firedCommand(sender: Player, command: Command?, alias: String?, args: Array<out String>): Boolean {
        val location = sender.foot
        LogAccessor.addInputLog(PlayerCommandInputOperation(location, sender.uniqueId))
        val baseCommand = "tell @p[r=${OpenCommandBlock.applicablePersonalRange}]"
        CommandBlockAccessor.getCurrentVersionDriver(location).setCommand(
                baseCommand
                        + " "
            +   args.joinToString(" ")
        )
        return true
    }

    override val triggerCommand: Command
        get() = OpenCommandBlock.instance.value!!.getCommand("cbtell")
    override val applicableArguments: List<List<CommandArgumentType>>
        get() = listOf(listOf(CommandArgumentType.STRING), listOf(CommandArgumentType.URL))
    override val requiredCommandBlockOnFoot: Boolean
        get() = true
}