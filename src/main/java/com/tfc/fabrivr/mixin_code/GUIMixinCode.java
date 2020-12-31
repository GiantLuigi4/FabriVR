package com.tfc.fabrivr.mixin_code;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;

public class GUIMixinCode {
	public static void renderPre(MatrixStack matrices) {
		if (!GameRendererCode.renderGUI) {
//			RenderSystem.pushMatrix();
			RenderSystem.scalef(0, 0, 0);
		}
//		Quaternion qt = new Quaternion(
//				FabriVROculus.headQuat.x(),
//				FabriVROculus.headQuat.y(),
//				FabriVROculus.headQuat.z(),
//				FabriVROculus.headQuat.w()
//		);
//		Vec3d angle = AngleHelper.toEulers(qt);
//
//		RenderSystem.translatef(
//				FabriVROculus.headPos.x()*100,
//				FabriVROculus.headPos.y()*100,
//				FabriVROculus.headPos.z()*100
//		);
//
//		RenderSystem.scalef(1,1,0);
//		RenderSystem.rotatef((float)Math.toDegrees(-angle.x),1,0,0);
//		RenderSystem.rotatef((float)Math.toDegrees(angle.y),0,1,0);
//		RenderSystem.rotatef((float)Math.toDegrees(-angle.z),0,0,1);
//
//		matrices.scale(0,0,0);
//
//		RenderSystem.scalef(1,1,0);
	}
	
	public static void renderPost(MatrixStack matrices) {
	}
}
