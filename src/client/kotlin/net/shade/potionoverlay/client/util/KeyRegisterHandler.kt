package net.shade.potionoverlay.client.util

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.shade.potionoverlay.Main
import net.shade.potionoverlay.client.MainClient
import org.lwjgl.glfw.GLFW

class KeyRegisterHandler {
    private lateinit var openWidgetScreenKey: KeyBinding


    private fun registerKeys() {
        openWidgetScreenKey = KeyBinding(
            "key.${Main.MOD_ID}.configure_widget",
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.${Main.MOD_ID}"
        )
    }

    private fun handleKeys()
    {
        ClientTickEvents.END_CLIENT_TICK.register {
            if (openWidgetScreenKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(MainClient.widgetScreen)
            }
        }
    }

    fun register() {
        registerKeys()
        handleKeys()
        KeyBindingHelper.registerKeyBinding(openWidgetScreenKey)
    }
}