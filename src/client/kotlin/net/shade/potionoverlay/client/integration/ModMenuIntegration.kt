package net.shade.potionoverlay.client.integration

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.ColorControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.shade.potionoverlay.client.MainClient
import net.shade.potionoverlay.client.util.PotionOverlayConfig
import java.awt.Color


class ModMenuIntegration : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            YetAnotherConfigLib.createBuilder()
                .title(Text.of("Potion Overlay Settings"))
                .category(
                    ConfigCategory.createBuilder()
                        .name(Text.of("Display Settings"))
                        .group(
                            OptionGroup.createBuilder()
                                .name(Text.of("Appearance"))
                                .option(
                                    Option.createBuilder<Boolean>()
                                        .name(Text.of("Text Shadow"))
                                        .description(OptionDescription.of(Text.of("Should text be rendered with a shadow?")))
                                        .binding(
                                            PotionOverlayConfig.renderShadow,
                                            { PotionOverlayConfig.renderShadow },
                                            { newVal -> PotionOverlayConfig.renderShadow = newVal }
                                        )
                                        .controller { option: Option<Boolean> ->
                                            BooleanControllerBuilder.create(option).yesNoFormatter()
                                        }
                                        .build()
                                )
                                .option(
                                    Option.createBuilder<Color>()
                                        .name(Text.of("Text Color"))
                                        .description(OptionDescription.of(Text.of("The color of the potion timer text")))
                                        .binding(
                                            PotionOverlayConfig.textColor,
                                            { PotionOverlayConfig.textColor },
                                            { newVal -> PotionOverlayConfig.textColor = newVal }
                                        )
                                        .controller(ColorControllerBuilder::create)
                                        .build()
                                )
                                .option(
                                    Option.createBuilder<Boolean>()
                                        .name(Text.of("Automatically Change Icon and Text Position"))
                                        .description(OptionDescription.of(Text.of("Should the icon and the text change position automatically?")))
                                        .binding(
                                            PotionOverlayConfig.changeIconAndTextPosition,
                                            { PotionOverlayConfig.changeIconAndTextPosition },
                                            { newVal -> PotionOverlayConfig.changeIconAndTextPosition = newVal }
                                        )
                                        .controller { option: Option<Boolean> ->
                                            BooleanControllerBuilder.create(option).yesNoFormatter()
                                        }
                                        .build()
                                )
                                .build()
                        )
                        .group(
                            OptionGroup.createBuilder()
                                .name(Text.of("Timer Effects"))
                                .option(
                                    Option.createBuilder<Boolean>()
                                        .name(Text.of("Enable Blinking"))
                                        .description(OptionDescription.of(Text.of("Timer blinks when expiration is near")))
                                        .binding(
                                            PotionOverlayConfig.blinkWhenUnderATime,
                                            { PotionOverlayConfig.blinkWhenUnderATime },
                                            { newVal -> PotionOverlayConfig.blinkWhenUnderATime = newVal }
                                        )
                                        .controller { option: Option<Boolean> ->
                                            BooleanControllerBuilder.create(option).yesNoFormatter()
                                        }
                                        .build()
                                )
                                .option(
                                    Option.createBuilder<Int>()
                                        .name(Text.of("Blink Threshold"))
                                        .description(OptionDescription.of(Text.of("Time remaining (in seconds) when blinking begins")))
                                        .binding(
                                            PotionOverlayConfig.timeWhenStartBlinking,
                                            { PotionOverlayConfig.timeWhenStartBlinking },
                                            { newVal -> PotionOverlayConfig.timeWhenStartBlinking = newVal }
                                        )
                                        .controller { option: Option<Int> ->
                                            IntegerSliderControllerBuilder.create(option)
                                                .range(1, 60)
                                                .step(1)
                                        }
                                        .build()
                                )
                                .option(
                                    Option.createBuilder<Boolean>()
                                        .name(Text.of("Color Warning"))
                                        .description(OptionDescription.of(Text.of("Change timer color when expiration is near")))
                                        .binding(
                                            PotionOverlayConfig.changeColor,
                                            { PotionOverlayConfig.changeColor },
                                            { newVal -> PotionOverlayConfig.changeColor = newVal }
                                        )
                                        .controller { option: Option<Boolean> ->
                                            BooleanControllerBuilder.create(option).yesNoFormatter()
                                        }
                                        .build()
                                )
                                .option(
                                    Option.createBuilder<Color>()
                                        .name(Text.of("Warning Color"))
                                        .description(OptionDescription.of(Text.of("Color when timer is near expiration")))
                                        .binding(
                                            PotionOverlayConfig.timerColor,
                                            { PotionOverlayConfig.timerColor },
                                            { newVal -> PotionOverlayConfig.timerColor = newVal }
                                        )
                                        .controller(ColorControllerBuilder::create)
                                        .build()
                                )
                                .option(
                                    Option.createBuilder<Int>()
                                        .name(Text.of("Warning Color Threshold"))
                                        .description(OptionDescription.of(Text.of("Time remaining (in seconds) when the color changes")))
                                        .binding(
                                            PotionOverlayConfig.timeWhenChangeColor,
                                            { PotionOverlayConfig.timeWhenChangeColor },
                                            { newVal -> PotionOverlayConfig.timeWhenChangeColor = newVal }
                                        )
                                        .controller { option: Option<Int> ->
                                            IntegerSliderControllerBuilder.create(option)
                                                .range(1, 60)
                                                .step(1)
                                        }
                                        .build()
                                )
                                .build()
                        )
                        .group(
                            OptionGroup.createBuilder()
                                .name(Text.of("Visibility"))
                                .option(
                                    Option.createBuilder<Boolean>()
                                        .name(Text.of("Show Effects"))
                                        .description(OptionDescription.of(Text.of("Display active potion effects")))
                                        .binding(
                                            PotionOverlayConfig.showPotionEffect,
                                            { PotionOverlayConfig.showPotionEffect },
                                            { newVal -> PotionOverlayConfig.showPotionEffect = newVal }
                                        )
                                        .controller { option: Option<Boolean> ->
                                            BooleanControllerBuilder.create(option).yesNoFormatter()
                                        }
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .save { PotionOverlayConfig.HANDLER.save() }
                .build()
                .generateScreen(parent)
        }
    }
}