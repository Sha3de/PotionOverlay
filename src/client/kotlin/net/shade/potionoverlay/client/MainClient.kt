package net.shade.potionoverlay.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.shade.potionoverlay.client.screens.PotionTimerWidgetScreen
import net.shade.potionoverlay.client.util.CustomHudRenderer
import net.shade.potionoverlay.client.util.KeyRegisterHandler

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
    }
}
