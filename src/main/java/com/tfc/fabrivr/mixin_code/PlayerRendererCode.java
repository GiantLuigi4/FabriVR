package com.tfc.fabrivr.mixin_code;

import com.tfc.fabrivr.client.FabriVRClient;
import net.minecraft.client.util.math.MatrixStack;

public class PlayerRendererCode {
	public static void translateHandRight(MatrixStack stack) {
		if (FabriVRClient.hand1Quat != null) {
//			stack.translate(0,-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y,0);
//			stack.translate(
//					FabriVRClient.hand2Pos.x(),
//					FabriVRClient.hand2Pos.y(),
//					FabriVRClient.hand2Pos.z()
//			);
//			System.out.println(
//					FabriVRClient.hand2Pos.x()+","+
//					FabriVRClient.hand2Pos.y()+","+
//					FabriVRClient.hand2Pos.z()
//			);
//			Quaternion qt = new Quaternion(
//					-FabriVRClient.hand2Quat.y(),
//					-FabriVRClient.hand2Quat.z(),
//					FabriVRClient.hand2Quat.x(),
//					FabriVRClient.hand2Quat.w()
//			);
//			stack.multiply(
//					new Quaternion(
//							-45f,-22.5f,0,true
//					)
//			);
//			stack.multiply(qt);
			stack.scale(0,0,0);
		}
	}
}
