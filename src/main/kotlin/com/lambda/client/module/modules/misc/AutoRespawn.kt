package com.lambda.client.module.modules.misc

import com.lambda.client.event.events.GuiEvent
import com.lambda.client.manager.managers.WaypointManager
import com.lambda.client.module.Category
import com.lambda.client.module.Module
import com.lambda.client.util.InfoCalculator
import com.lambda.client.util.math.CoordinateConverter.asString
import com.lambda.client.util.text.MessageSendHelper
import com.lambda.event.listener.listener
import net.minecraft.client.gui.GuiGameOver

object AutoRespawn : Module(
    name = "AutoRespawn",
    description = "Automatically respawn after dying",
    category = Category.MISC
) {
    private val respawn = setting("Respawn", true)
    private val deathCoords = setting("Save Death Coords", true)
    private val antiGlitchScreen = setting("Anti Glitch Screen", true)

    init {
        listener<GuiEvent.Displayed> {
            if (it.screen !is GuiGameOver) return@listener

            if (deathCoords.value && mc.player.health <= 0) {
                WaypointManager.add("Death - " + InfoCalculator.getServerType())
                MessageSendHelper.sendChatMessage("You died at ${mc.player.position.asString()}")
            }

            if (respawn.value || antiGlitchScreen.value && mc.player.health > 0) {
                mc.player.respawnPlayer()
                mc.displayGuiScreen(null)
            }
        }
    }
}