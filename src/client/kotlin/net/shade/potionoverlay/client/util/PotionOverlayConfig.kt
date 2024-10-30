package net.shade.potionoverlay.client.util

import com.google.gson.GsonBuilder
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.SerialEntry
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import net.shade.potionoverlay.Main


class PotionOverlayConfig {
    companion object {
        var HANDLER: ConfigClassHandler<PotionOverlayConfig> = ConfigClassHandler.createBuilder(PotionOverlayConfig::class.java)
            .id(Identifier.of(Main.MOD_ID, "config"))
            .serializer { config: ConfigClassHandler<PotionOverlayConfig>? ->
                GsonConfigSerializerBuilder.create<PotionOverlayConfig>(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("${Main.MOD_ID}.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build()
            }
            .build()
        @SerialEntry
        var widgetX: Int = 0

        @SerialEntry
        var widgetY: Int = 0

        @SerialEntry
        var renderShadow: Boolean = false

        @SerialEntry
        var blinkWhenUnderATime: Boolean = true

        @SerialEntry
        var timeWhenStartBlinking: Int = 10
    }
}