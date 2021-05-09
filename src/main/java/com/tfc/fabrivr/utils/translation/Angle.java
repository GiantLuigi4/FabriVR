package com.tfc.fabrivr.utils.translation;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class Angle {
	public static Vec3d toEulers(Quaternion qt) {
		//https://en.wikipedia.org/wiki/Conversion_between_qts_and_Euler_angles
		Vector3d angles = new Vector3d(0, 0, 0);
		
		// roll (x-axis rotation)
		double sinr_cosp = 2 * (qt.getW() * qt.getX() + qt.getY() * qt.getZ());
		double cosr_cosp = 1 - 2 * (qt.getX() * qt.getX() + qt.getY() * qt.getY());
		angles.x = Math.atan2(sinr_cosp, cosr_cosp);
		
		// pitch (y-axis rotation)
		double sinp = 2 * (qt.getW() * qt.getY() - qt.getZ() * qt.getX());
		if (Math.abs(sinp) >= 1) angles.y = Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
		else angles.y = Math.asin(sinp);
		
		// yaw (z-axis rotation)
		double siny_cosp = 2 * (qt.getW() * qt.getZ() + qt.getX() * qt.getY());
		double cosy_cosp = 1 - 2 * (qt.getY() * qt.getY() + qt.getZ() * qt.getZ());
		angles.z = Math.atan2(siny_cosp, cosy_cosp);
		
		return new Vec3d(angles.x, angles.y, angles.z);
	}
}
