package com.tfc.fabrivr;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class AngleHelper {
	public static Vec3d toMotionVector(Quaternion qt) {
		//https://en.wikipedia.org/wiki/Conversion_between_qts_and_Euler_angles
//		Vector3d angles = new Vector3d(0, 0, 0);
//
//		// roll (x-axis rotation)
//		double sinr_cosp = 2 * (qt.getW() * qt.getX() + qt.getY() * qt.getZ());
//		double cosr_cosp = 1 - 2 * (qt.getX() * qt.getX() + qt.getY() * qt.getY());
//		angles.x = Math.atan2(sinr_cosp, cosr_cosp);
//
//		// pitch (y-axis rotation)
//		double sinp = 2 * (qt.getW() * qt.getY() - qt.getZ() * qt.getX());
//		if (Math.abs(sinp) >= 1) angles.y = Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
//		else angles.y = Math.asin(sinp);
//
//		// yaw (z-axis rotation)
//		double siny_cosp = 2 * (qt.getW() * qt.getZ() + qt.getX() * qt.getY());
//		double cosy_cosp = 1 - 2 * (qt.getY() * qt.getY() + qt.getZ() * qt.getZ());
//		angles.z = Math.atan2(siny_cosp, cosy_cosp);
//
//		return new Vec3d(angles.x, angles.y, angles.z);

//		return new Vec3d(
//				qt.getX()*Math.acos(qt.getW()),
//				qt.getY()*Math.acos(qt.getW()),
//				qt.getZ()*Math.acos(qt.getW())
//		);
		
		//https://stackoverflow.com/questions/21515341/rotate-a-unit-vector-by-a-given-quaternion
		float x = qt.getX();
		float y = qt.getY();
		float z = 0; // idk why but this makes it work better
		float w = qt.getW();
		Vector3d vec = new Vector3d(0, 0, 0);
		vec.x = 2 * x * z - 2 * y * w;
		vec.y = 2 * y * z + 2 * x * w;
		vec.z = 1 - 2 * x * x - 2 * y * y;
		
		Vec3d axis = new Vec3d(vec.x, vec.y, vec.z);
		Vec3d dir = new Vec3d(axis.x, 0, axis.z).normalize();
		dir = dir.add(0, axis.y * 2, 0).normalize();
		return dir;
	}
	
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
	
	public static Vec3d transform(Quaternion qt) {
		return transform(new Vec3d(1, 0, 0), qt);
	}
	
	//https://github.com/JOML-CI/JOML/blob/51fd2fe432d3d2b09a11db2ac6a79441be1d1c60/src/org/joml/Quaternionf.java#L1206-L1213
	public static Vec3d transform(Vec3d src, Quaternion qt) {
		float xx = qt.getX() * qt.getX(), yy = qt.getY() * qt.getY(), zz = qt.getZ() * qt.getZ(), ww = qt.getW() * qt.getW();
		float xy = qt.getX() * qt.getY(), xz = qt.getX() * qt.getZ(), yz = qt.getY() * qt.getZ(), xw = qt.getX() * qt.getW();
		float zw = qt.getZ() * qt.getW(), yw = qt.getY() * qt.getW(), k = 1 / (xx + yy + zz + ww);
		return new Vec3d(fma((xx - yy - zz + ww) * k, src.getX(), fma(2 * (xy - zw) * k, src.getY(), (2 * (xz + yw) * k) * src.getZ())),
				fma(2 * (xy + zw) * k, src.getX(), fma((yy - xx - zz + ww) * k, src.getY(), (2 * (yz - xw) * k) * src.getZ())),
				fma(2 * (xz - yw) * k, src.getX(), fma(2 * (yz + xw) * k, src.getX(), ((zz - xx - yy + ww) * k) * src.getZ())));
	}
	
	//https://github.com/JOML-CI/JOML/
	public static float fma(float a, float b, float c) {
		return a * b + c;
	}
	
	//https://github.com/JOML-CI/JOML/
	public static float fma(float a, float b, double c) {
		return a * b + (float) c;
	}
	
	//https://github.com/JOML-CI/JOML/
	public static float fma(float a, double b, double c) {
		return a * (float) b + (float) c;
	}
	
	//https://github.com/JOML-CI/JOML/
	public static Vec3d transformUnit(Quaternion qt) {
		return transformUnit(new Vec3d(1, 0, 0), qt);
	}
	
	//https://github.com/JOML-CI/JOML/
	public static Vec3d transformUnit(Vec3d src, Quaternion qt) {
		float xx = qt.getX() * qt.getX(), xy = qt.getX() * qt.getY(), xz = qt.getX() * qt.getZ();
		float xw = qt.getX() * qt.getW(), yy = qt.getY() * qt.getY(), yz = qt.getY() * qt.getZ();
		float yw = qt.getY() * qt.getW(), zz = qt.getZ() * qt.getZ(), zw = qt.getZ() * qt.getW();
		return new Vec3d(fma(fma(-2, yy + zz, 1), src.getX(), fma(2 * (xy - zw), src.getY(), (2 * (xz + yw)) * src.getZ())),
				fma(2 * (xy + zw), src.getX(), fma(fma(-2, xx + zz, 1), src.getY(), (2 * (yz - xw)) * src.getZ())),
				fma(2 * (xz - yw), src.getX(), fma(2 * (yz + xw), src.getY(), fma(-2, xx + yy, 1) * src.getZ())));
	}
	
	//https://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToAngle/index.htm
	public static double getAngle(Quaternion qt) {
		return 2 * Math.acos(qt.getW());
	}

//	//https://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToAngle/index.htm
//	public static Vec3d getAxis(Quaternion qt) {
//		double x = qt.getX() / Math.sqrt(1 - qt.getW() * qt.getW());
//		double y = qt.getY() / Math.sqrt(1 - qt.getW() * qt.getW());
//		double z = qt.getZ() / Math.sqrt(1 - qt.getW() * qt.getW());
//		return new Vec3d(x, y, z);
//	}
	
	public static Vec3d getAxisFromQT(Quaternion qt) {
		float w = qt.getW();
		double angle = Math.acos(w);
//		if (true) {
//			angle *= 0.017453292F;
//		}
		double sinAngle = Math.sin(angle);
		
		
		double x = qt.getX() / sinAngle;
		double y = qt.getY() / sinAngle;
		double z = qt.getZ() / sinAngle;
		
		return new Vec3d(x, y, z).normalize();
	}
}
