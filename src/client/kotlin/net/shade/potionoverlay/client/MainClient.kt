package net.shade.potionoverlay.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.shade.potionoverlay.client.screens.PotionTimerWidgetScreen
import net.shade.potionoverlay.client.util.CustomHudRenderer
import net.shade.potionoverlay.client.util.KeyRegisterHandler
import net.shade.potionoverlay.client.util.PotionOverlayConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping
import net.minecraft.client.MinecraftClient

@Environment(EnvType.CLIENT)
class MainClient : ClientModInitializer {
    companion object{
        var keyRegisterHandler: KeyRegisterHandler = KeyRegisterHandler()
        var customHudRenderer: CustomHudRenderer = CustomHudRenderer()
        var widgetScreen: PotionTimerWidgetScreen = PotionTimerWidgetScreen()
    }
    override fun onInitializeClient() {
        keyRegisterHandler.register()
        customHudRenderer.render()
        PotionOverlayConfig.HANDLER.load()
        ClientLifecycleEvents.CLIENT_STOPPING.register(ClientStopping { client: MinecraftClient? ->
            PotionOverlayConfig.HANDLER.save()
        })
    }
}
