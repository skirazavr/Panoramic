package com.skirazavr.panoramic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(value = "panoramic")
@Mod.EventBusSubscriber(Dist.CLIENT)
public class Panoramic {
	public static final KeyMapping PANORAMA_KEY = new KeyMapping("key.panoramic.create", InputConstants.Type.KEYBOARD,
		InputConstants.KEY_F9, KeyMapping.Category.MISC);

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		while (PANORAMA_KEY.consumeClick()) {
			if (mc.level == null || mc.player == null) {
				return;
			}
			Path folder = mc.gameDirectory.toPath().resolve("panorama");
			try {
				Files.createDirectories(folder);
			} catch (IOException e) {
				return;
			}
			mc.grabPanoramixScreenshot(folder.toFile());
			Component message = Component.translatable("link.panoramic.open")
					.setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withUnderlined(true)
							.withClickEvent(new ClickEvent.OpenFile(folder.toAbsolutePath())));
			mc.player.sendSystemMessage(Component.translatable("message.panoramic.saved").append(message));
		}
	}

	@SubscribeEvent
	public static void registerBindings(RegisterKeyMappingsEvent event) {
		event.register(PANORAMA_KEY);
	}
}
