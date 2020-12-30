package com.tfc.fabrivr.mixin_code;

import com.tfc.fabrivr.client.FabriVRClient;
import com.tfc.fabrivr.client.FabriVROculus;
import com.tfc.fabrivr.mixin.PlayerRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class WorldRendererCode {
	public static void renderPre(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, net.minecraft.client.render.GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci, World world) {
		float offY = FabriVRClient.offY;
		if (world.getBlockState(camera.getBlockPos().add(0, offY+0.5f, 0)).isFullCube(MinecraftClient.getInstance().world,camera.getBlockPos().add(0,offY,0)))
			matrices.scale(0, 0, 0);
		
		if (MinecraftClient.getInstance().getEntityRenderDispatcher().camera != null) {
			if (MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation() != null) {
				Quaternion qt = MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation().copy();
				matrices.multiply(qt);
				if (FabriVROculus.headQuat != null) {
					matrices.multiply(
							new Quaternion(
									FabriVROculus.headQuat.x(),
									-FabriVROculus.headQuat.y(),
									-FabriVROculus.headQuat.z(),
									FabriVROculus.headQuat.w()
							)
					);
				}
			}
		}
		matrices.translate(-0, -offY, -0);
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
//					-((FabriVROculus.hand2Pos.x()*2) - FabriVROculus.headPos.x()),
					-((FabriVROculus.hand2Pos.x() - FabriVROculus.headPos.x()) * 2),
//					(((FabriVROculus.hand2Pos.y()*2))+0.5f),
//					(((FabriVROculus.hand2Pos.y()*2) - FabriVROculus.headPos.y())+0.5f),
					(((FabriVROculus.hand2Pos.y()) * 2) + 0.5f),
//					-((FabriVROculus.hand2Pos.z()*2))
//					-((FabriVROculus.hand2Pos.z()*2) - FabriVROculus.headPos.z())
					-((FabriVROculus.hand2Pos.z() - FabriVROculus.headPos.z()) * 2)
			);
			Quaternion qt = new Quaternion(
					-FabriVROculus.hand2Quat.x(),
					FabriVROculus.hand2Quat.y(),
					-FabriVROculus.hand2Quat.z(),
					FabriVROculus.hand2Quat.w()
			);
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
//					-((FabriVROculus.hand1Pos.x()*2) - FabriVROculus.headPos.x()),
					-((FabriVROculus.hand1Pos.x() - FabriVROculus.headPos.x())) * 2,
//					(((FabriVROculus.hand1Pos.y()*2))+0.5f),
//					(((FabriVROculus.hand1Pos.y()*2) - FabriVROculus.headPos.y())+0.5f),
					(((FabriVROculus.hand1Pos.y()) * 2) + 0.5f),
//					-((FabriVROculus.hand1Pos.z()*2))
//					-((FabriVROculus.hand1Pos.z()*2) - FabriVROculus.headPos.z())
					-((FabriVROculus.hand1Pos.z() - FabriVROculus.headPos.z()) * 2)
			);
			Quaternion qt = new Quaternion(
					-FabriVROculus.hand1Quat.x(),
					FabriVROculus.hand1Quat.y(),
					-FabriVROculus.hand1Quat.z(),
					FabriVROculus.hand1Quat.w()
			);
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
}
