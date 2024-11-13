package net.shade.potionoverlay

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory


class Main : ModInitializer {

    var logger = LoggerFactory.getLogger("PotionOverlay")
    companion object {
        val MOD_ID = "potionoverlay"
    }
    override fun onInitialize() {
        logger.info("PotionOverlay mod initialized")

    }


}
