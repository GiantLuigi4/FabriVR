package com.tfc.fabrivr.mixin_code;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.fabrivr.AngleHelper;
import com.tfc.fabrivr.client.FabriVRClient;
import com.tfc.fabrivr.client.FabriVROculus;
import com.tfc.fabrivr.mixin.MatrixStackAccessor;
import com.tfc.fabrivr.mixin.PlayerRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class WorldRendererCode {
	private static MatrixStack tempStack;
	
	public static void renderPre(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, net.minecraft.client.render.GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci, World world) {
		float offY = FabriVRClient.offY;
//		if (world.getBlockState(camera.getBlockPos().add(0, offY+0.5f, 0)).isFullCube(MinecraftClient.getInstance().world,camera.getBlockPos().add(0,offY,0)))
//			matrices.scale(0, 0, 0);
		
		tempStack = new MatrixStack();
		
		if (MinecraftClient.getInstance().getEntityRenderDispatcher().camera != null) {
			if (MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation() != null) {
				Quaternion qt = MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation().copy();
				matrices.multiply(qt);
				if (FabriVROculus.headQuat != null) {
					qt = new Quaternion(
							FabriVROculus.headQuat.x(),
							FabriVROculus.headQuat.y(),
							FabriVROculus.headQuat.z(),
							FabriVROculus.headQuat.w()
					);
					qt.normalize();
					
					{
						Quaternion qt1 = new Quaternion(
								-FabriVROculus.hand2Quat.x(),
								FabriVROculus.hand2Quat.y(),
								-FabriVROculus.hand2Quat.z(),
								FabriVROculus.hand2Quat.w()
						);
						qt1.normalize();
						Vec3d axis = AngleHelper.toMotionVector(qt1);
						
						MinecraftClient.getInstance().world.addParticle(
								ParticleTypes.BARRIER,
								MinecraftClient.getInstance().player.getPos().x - axis.x * 2,
								MinecraftClient.getInstance().player.getPos().y - axis.y * 2,
								MinecraftClient.getInstance().player.getPos().z + axis.z * 2,
								0, 0, 0
						);
					}
					Vec3d angle = AngleHelper.toEulers(qt);
					matrices.multiply(
							new Quaternion(
									(float) angle.x,
									-(float) angle.y,
									(float) angle.z,
									false
							)
					);
				}
			}
		}
		matrices.translate(-0, -(offY), -0);
		
		((MatrixStackAccessor) tempStack).getEntries().add(matrices.peek());
	}
	
	public static void renderWorldLast(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, net.minecraft.client.render.GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci, World world) {
		PlayerEntityRenderer renderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(
				MinecraftClient.getInstance().player
		);
		matrices.translate(
				-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().x,
				-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y,
				-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().z
		);
		matrices.translate(
				MinecraftClient.getInstance().player.getX(),
				MinecraftClient.getInstance().player.getY(),
				MinecraftClient.getInstance().player.getZ()
		);
		matrices.push();
		if (FabriVROculus.hand2Pos != null) {
			matrices.translate(
//					-((FabriVROculus.hand2Pos.x()*2)),
					-((FabriVROculus.hand2Pos.x() * 2) - FabriVROculus.headPos.x()),
//					-((FabriVROculus.hand2Pos.x() - FabriVROculus.headPos.x()) * 2),
//					(((FabriVROculus.hand2Pos.y()*2))+0.5f),
//					(((FabriVROculus.hand2Pos.y()*2) - FabriVROculus.headPos.y())+0.5f),
					(((FabriVROculus.hand2Pos.y()) * 2) + 0.5f),
//					-((FabriVROculus.hand2Pos.z()*2))
					-((FabriVROculus.hand2Pos.z() * 2) - FabriVROculus.headPos.z())
//					-((FabriVROculus.hand2Pos.z() - FabriVROculus.headPos.z()) * 2)
			);
			Quaternion qt = new Quaternion(
					-FabriVROculus.hand2Quat.x(),
					FabriVROculus.hand2Quat.y(),
					-FabriVROculus.hand2Quat.z(),
					FabriVROculus.hand2Quat.w()
			);
			qt.normalize();
			matrices.multiply(qt);
			qt = new Quaternion(0, 180, 0, true);
			matrices.multiply(qt);
			qt = new Quaternion(-45, -90, 0, true);
//			matrices.translate(-0.5f+0.5f/16,-0.5f/16,0);
			matrices.translate(0, 0.25f + (2 / 16f), 0);
			matrices.multiply(qt);
			((PlayerRendererAccessor) renderer).doRenderArm(
					matrices, MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers(),
					LightmapTextureManager.pack(15, 15),
					MinecraftClient.getInstance().player,
					renderer.getModel().rightArm,
					renderer.getModel().rightSleeve
			);
////			matrices.translate(0.25f,1,0.25f);
//			matrices.translate(-0.325f,0.5,0.1f);
//			qt.scale(-1);
//			matrices.multiply(qt);
//			qt = new Quaternion(180+45,180,0,true);
//			matrices.multiply(qt);
//			matrices.translate(-0.5f,0.5f,1f);
//			if (!MinecraftClient.getInstance().player.getItemsHand().iterator().next().isEmpty()) {
//				MinecraftClient.getInstance().getHeldItemRenderer().renderItem(
//						MinecraftClient.getInstance().player,
//						MinecraftClient.getInstance().player.getItemsHand().iterator().next(),
//						ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND,
//						MinecraftClient.getInstance().player.getMainArm().equals(Hand.MAIN_HAND),
//						matrices,
//						MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(),
//						LightmapTextureManager.pack(15,15)
//				);
//			}
		}
		matrices.pop();
		matrices.push();
		if (FabriVROculus.hand1Pos != null) {
			matrices.translate(
//					-((FabriVROculus.hand1Pos.x()*2)),
					-((FabriVROculus.hand1Pos.x() * 2) - FabriVROculus.headPos.x()),
//					-((FabriVROculus.hand1Pos.x() - FabriVROculus.headPos.x())) * 2,
//					(((FabriVROculus.hand1Pos.y()*2))+0.5f),
//					(((FabriVROculus.hand1Pos.y()*2) - FabriVROculus.headPos.y())+0.5f),
					(((FabriVROculus.hand1Pos.y()) * 2) + 0.5f),
//					-((FabriVROculus.hand1Pos.z()*2))
					-((FabriVROculus.hand1Pos.z() * 2) - FabriVROculus.headPos.z())
//					-((FabriVROculus.hand1Pos.z() - FabriVROculus.headPos.z()) * 2)
			);
			Quaternion qt = new Quaternion(
					-FabriVROculus.hand1Quat.x(),
					FabriVROculus.hand1Quat.y(),
					-FabriVROculus.hand1Quat.z(),
					FabriVROculus.hand1Quat.w()
			);
			qt.normalize();
			matrices.multiply(qt);
			qt = new Quaternion(0, 180, 0, true);
			matrices.multiply(qt);
			qt = new Quaternion(-45, 90, 0, true);
//			matrices.translate(-0.5f+0.5f/16,-0.5f/16,0);
			matrices.translate(0, 0.25f + (2 / 16f), 0);
			matrices.multiply(qt);
			((PlayerRendererAccessor) renderer).doRenderArm(
					matrices, MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers(),
					LightmapTextureManager.pack(15, 15),
					MinecraftClient.getInstance().player,
					renderer.getModel().leftArm,
					renderer.getModel().leftSleeve
			);
		}
		matrices.pop();
	}
	
	public static void setupTerrain(Camera camera, Frustum frustum, boolean hasForcedFrustum, int frame, boolean spectator) {
		{
			RenderSystem.pushMatrix();
			
			MatrixStack matrices1 = new MatrixStack();

//			RenderSystem.translated(
//					MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().x*10,
//					MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y*10,
//					MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().z*10
//			);
			
			RenderSystem.rotatef(180, 0, 0, 1);
//			matrices1.multiply(
//					new Quaternion(0,0,180,true)
//			);

//			Matrix4f matrix4f1 = matrices1.peek().getModel().copy();
//			matrix4f1.multiply(matrix4f);
//			MinecraftClient.getInstance().gameRenderer.loadProjectionMatrix(
//					matrix4f1
//			);
			
			if (MinecraftClient.getInstance().currentScreen != null) {
				try {
					GameRendererCode.renderGUI = true;
					int i = (int) (MinecraftClient.getInstance().mouse.getX() * (double) MinecraftClient.getInstance().getWindow().getScaledWidth() / (double) MinecraftClient.getInstance().getWindow().getWidth());
					int j = (int) (MinecraftClient.getInstance().mouse.getY() * (double) MinecraftClient.getInstance().getWindow().getScaledHeight() / (double) MinecraftClient.getInstance().getWindow().getHeight());
					RenderSystem.translated(
							-MinecraftClient.getInstance().currentScreen.width / 2f,
							-MinecraftClient.getInstance().currentScreen.height / 2f,
							0
					);
					Quaternion qt = new Quaternion(
							FabriVROculus.headQuat.x(),
							FabriVROculus.headQuat.y(),
							FabriVROculus.headQuat.z(),
							FabriVROculus.headQuat.w()
					);
					qt.normalize();
					
					Vec3d angle = AngleHelper.toEulers(qt);
					
					RenderSystem.disableCull();
					
					RenderSystem.translated(
							-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().x * 1,
							-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y * 1,
							-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().z * 1
					);
					Vec3d openedAt = (Vec3d) MinecraftClient.getInstance().currentScreen.getClass().getMethod("getOpenedAt").invoke(MinecraftClient.getInstance().currentScreen);
//						Vec3d openedAt = ((GUIVRFieldsAccessor)MinecraftClient.getInstance().currentScreen).getOpenedAt();
					RenderSystem.translated(
							openedAt.x * 1,
							openedAt.y * 1,
							openedAt.z * 1
					);
					RenderSystem.scaled(1, 1, 0);
					
					RenderSystem.translated(
							MinecraftClient.getInstance().currentScreen.width / 2f,
							MinecraftClient.getInstance().currentScreen.height / 2f,
							0
					);
					
					RenderSystem.rotatef((float) Math.toDegrees(angle.x), 1, 0, 0);
					RenderSystem.rotatef((float) -Math.toDegrees(angle.y) + 180, 0, 1, 0);
					RenderSystem.rotatef((float) Math.toDegrees(angle.z), 0, 0, 1);
					
					RenderSystem.scalef(0.01f, 0.01f, 0);
					RenderSystem.translated(
							-MinecraftClient.getInstance().currentScreen.width / 2f,
							-MinecraftClient.getInstance().currentScreen.height / 2f,
							0
					);

//					RenderSystem.disableDepthTest();
//					RenderSystem.depthMask(false);
					
					GlStateManager.depthFunc(GL11.GL_ALWAYS);
					
					RenderSystem.disableFog();
					
					RenderSystem.fogMode(GlStateManager.FogMode.EXP);

//					RenderSystem.enableScissor(
//							0,0,
//							MinecraftClient.getInstance().currentScreen.width*2,
//							MinecraftClient.getInstance().currentScreen.height*2
//					);
					MinecraftClient.getInstance().currentScreen.render(matrices1, i, j, 0);
//					RenderSystem.disableScissor();
					GameRendererCode.renderGUI = false;
				} catch (Throwable ignored) {
				}
				
			}
			
			RenderSystem.popMatrix();
			
			RenderSystem.enableCull();
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
			RenderSystem.enableFog();
			RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
		}
	}
}
