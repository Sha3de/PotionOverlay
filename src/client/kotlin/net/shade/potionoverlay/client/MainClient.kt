package net.shade.potionoverlay.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.shade.potionoverlay.client.util.CustomHudRenderer
import net.shade.potionoverlay.client.util.KeyRegisterHandler


class MainClient : ClientModInitializer {

    override fun onInitializeClient() {
        KeyRegisterHandler().register()
        CustomHudRenderer().render()
    }
}
