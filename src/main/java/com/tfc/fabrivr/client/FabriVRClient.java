package com.tfc.fabrivr.client;

import com.tfc.fabrivr.utils.openvr.Session;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.Framebuffer;

@Environment(EnvType.CLIENT)
public class FabriVRClient implements ClientModInitializer {
	public static Framebuffer guiFrameBuffer; //TODO
	@Override
	public void onInitializeClient() {
		Session.init();
	}
}
