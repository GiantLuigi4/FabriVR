package com.tfc.fabrivr.client;

import com.tfc.fabrivr.utils.openvr.Tracking;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import org.joml.Quaternionf;

public class GameRendererHandler {
	public static void preLoadMatrix(Matrix4f matrix4f) {
		{
			Quaternionf quaternionf = Tracking.head.rotation;
			Quaternion quaternion = new Quaternion(-quaternionf.x, -quaternionf.y, -quaternionf.z, quaternionf.w);
			quaternion.normalize();
			matrix4f.multiply(quaternion);
		}
	}
}
