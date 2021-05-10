package com.tfc.fabrivr.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.fabrivr.FabriVR;
import com.tfc.fabrivr.utils.openvr.Tracking;
import com.tfc.fabrivr.utils.translation.Joml2MC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class WorldRendererHandler {
	private static Vec3d guiOpenVector = new Vec3d(0,0,0);
	private static Screen lastScreen = null;
	private static Quaternion openQuaternion = new Quaternion(0,0,0,true);
	public static void setupHeadTranslation(MatrixStack matrices) {
		Tracking.update();
		Vec3d vector3d = MinecraftClient.getInstance().cameraEntity.getCameraPosVec(MinecraftClient.getInstance().getTickDelta());
		vector3d = vector3d.subtract(0, MathHelper.lerp(
				MinecraftClient.getInstance().getTickDelta(),
				MinecraftClient.getInstance().cameraEntity.lastRenderY,
				MinecraftClient.getInstance().cameraEntity.getPos().y
		), 0);
		matrices.translate(0, vector3d.y, 0);
		float height = MinecraftClient.getInstance().cameraEntity.getHeight();
		height /= 1.7;
		matrices.translate(Tracking.head.position.x * height, -Tracking.head.position.y * height, Tracking.head.position.z * height);
		if (lastScreen != MinecraftClient.getInstance().currentScreen) {
			guiOpenVector = MinecraftClient.getInstance().cameraEntity.getPos();
			Quaternionf quaternionf = Tracking.head.rotation;
			Quaternion quaternion = new Quaternion(-quaternionf.x, -quaternionf.y, -quaternionf.z, quaternionf.w);
			quaternion.normalize();
			openQuaternion = Joml2MC.fromQuatf(quaternionf);
			lastScreen = MinecraftClient.getInstance().currentScreen;
		}
	}
	
	// TODO: create a slim variation of the arms and use "AbstractClientPlayerEntity.getModelName" to choose which model to render
	private static final ModelPart leftArm;
	private static final ModelPart rightArm;
	static {
		leftArm = new ModelPart(64, 64, 0, 0);
		leftArm.mirror = true;
		leftArm.setPivot(0.0F, 0.0F, 0.0F);
		leftArm.setTextureOffset(32, 48).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		rightArm = new ModelPart(64, 64, 0, 0);
		rightArm.setPivot(0.0F, 0.0F, 0.0F);
		rightArm.setTextureOffset(40, 16).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
	}
	
	public static void renderHands(MatrixStack matrices) {
		EntityRenderer<?> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(MinecraftClient.getInstance().getCameraEntity());
		matrices.push();
		if (renderer instanceof PlayerEntityRenderer) {
			matrices.translate(
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().x,
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y,
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().z
			);
			matrices.translate(
					MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), MinecraftClient.getInstance().cameraEntity.lastRenderX, MinecraftClient.getInstance().cameraEntity.getX()),
					MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), MinecraftClient.getInstance().cameraEntity.lastRenderY, MinecraftClient.getInstance().cameraEntity.getY()),
					MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), MinecraftClient.getInstance().cameraEntity.lastRenderZ, MinecraftClient.getInstance().cameraEntity.getZ())
			);
			
			matrices.push();
			{
				float height = MinecraftClient.getInstance().cameraEntity.getHeight();
				height /= 1.7;
				matrices.translate(-Tracking.leftHand.position.x * height, Tracking.leftHand.position.y, -Tracking.leftHand.position.z * height);
				{
					Quaternionf quaternionf = Tracking.leftHand.rotation;
					Quaternion quaternion = new Quaternion(-quaternionf.x, quaternionf.y, -quaternionf.z, quaternionf.w);
					quaternion.normalize();
					matrices.multiply(quaternion);
					matrices.multiply(new Quaternion(90, -90, 0, true));
				}
				matrices.translate(0, -0.125, 0);
				matrices.scale(0.5f, 0.5f, 0.5f);
				leftArm.render(
						matrices, MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers().getBuffer(RenderLayer.getEntitySolid(MinecraftClient.getInstance().player.getSkinTexture())),
						LightmapTextureManager.pack(0,15), OverlayTexture.DEFAULT_UV
				);
			}
			matrices.pop();
			
			matrices.push();
			{
				float height = MinecraftClient.getInstance().cameraEntity.getHeight();
				height /= 1.7;
				matrices.translate(-Tracking.rightHand.position.x * height, Tracking.rightHand.position.y, -Tracking.rightHand.position.z * height);
				{
					Quaternionf quaternionf = Tracking.rightHand.rotation;
					Quaternion quaternion = new Quaternion(-quaternionf.x, quaternionf.y, -quaternionf.z, quaternionf.w);
					matrices.multiply(quaternion);
					quaternion.normalize();
					matrices.multiply(new Quaternion(90, 90, 0, true));
				}
				matrices.translate(0, -0.125, 0);
				matrices.scale(0.5f, 0.5f, 0.5f);
				rightArm.render(
						matrices, MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers().getBuffer(RenderLayer.getEntitySolid(MinecraftClient.getInstance().player.getSkinTexture())),
						LightmapTextureManager.pack(0,15), OverlayTexture.DEFAULT_UV
				);
			}
			matrices.pop();
		}
		matrices.pop();
		
		matrices.push();
		if (MinecraftClient.getInstance().currentScreen != null) {
			Matrix4f matrix4f = matrices.peek().getModel();
			VertexConsumer consumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getEntityTranslucent(FabriVRClient.textureID));
			matrices.translate(
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().x,
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y,
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().z
			);
			RenderSystem.enableAlphaTest();
			matrices.translate(guiOpenVector.x, guiOpenVector.y, guiOpenVector.z);
			matrices.multiply(new Quaternion(
					0, 180, 0, true
			));
			matrices.multiply(new Quaternion(
					openQuaternion.getX(),
					openQuaternion.getY(),
					openQuaternion.getZ(),
					openQuaternion.getW()
			));
			matrices.translate(0, 0, -2);
			RenderSystem.disableCull();
			RenderSystem.enableTexture();
			RenderSystem.bindTexture(FabriVRClient.guiFrameBuffer.getColorAttachment());
			consumer.vertex(matrix4f, -2, -2, 0)
					.color(1f, 1f, 1f, 1f)
					.texture(0, 0)
					.overlay(OverlayTexture.DEFAULT_UV)
					.light(LightmapTextureManager.pack(15, 15))
					.normal(1, 1, 1)
					.next();
			consumer.vertex(matrix4f, 2, -2, 0)
					.color(1f, 1f, 1f, 1f)
					.texture(1, 0)
					.overlay(OverlayTexture.DEFAULT_UV)
					.light(LightmapTextureManager.pack(15, 15))
					.normal(1, 1, 1)
					.next();
			consumer.vertex(matrix4f, 2, 2, 0)
					.color(1f, 1f, 1f, 1f)
					.texture(1, 1)
					.overlay(OverlayTexture.DEFAULT_UV)
					.light(LightmapTextureManager.pack(15, 15))
					.normal(1, 1, 1)
					.next();
			consumer.vertex(matrix4f, -2, 2, 0)
					.color(1f, 1f, 1f, 1f)
					.texture(0, 1)
					.overlay(OverlayTexture.DEFAULT_UV)
					.light(LightmapTextureManager.pack(15, 15))
					.normal(1, 1, 1)
					.next();
		}
		matrices.pop();
	}
}
