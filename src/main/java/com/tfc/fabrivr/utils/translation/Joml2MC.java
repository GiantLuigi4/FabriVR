package com.tfc.fabrivr.utils.translation;

import net.minecraft.util.math.Quaternion;
import org.joml.Quaternionf;

public class Joml2MC {
	public static Quaternion fromQuatf(Quaternionf src) {
		return new Quaternion(src.x, src.y, src.z, src.w);
	}
}
