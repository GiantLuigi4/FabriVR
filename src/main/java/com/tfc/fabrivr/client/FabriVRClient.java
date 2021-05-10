package com.tfc.fabrivr.client;

import com.tfc.fabrivr.utils.openvr.Session;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FabriVRClient implements ClientModInitializer {
	public static Framebuffer guiFrameBuffer; //TODO
	public static NativeImageBackedTexture texture;
	public static Identifier textureID;
	
	@Override
	public void onInitializeClient() {
		Session.init();
	}
}
