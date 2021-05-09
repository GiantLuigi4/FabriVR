package com.tfc.fabrivr.utils.translation;

import org.joml.Matrix4f;
import org.lwjgl.openvr.HmdMatrix34;

public class ToRawJava {
	public static float[][] matrixTo2dFloatArray(HmdMatrix34 matrix34) {
		float[][] floats = new float[3][4];
		floats[0][0] = matrix34.m(0);
		floats[0][1] = matrix34.m(1);
		floats[0][2] = matrix34.m(2);
		floats[0][3] = matrix34.m(3);
		floats[1][0] = matrix34.m(4);
		floats[1][1] = matrix34.m(5);
		floats[1][2] = matrix34.m(6);
		floats[1][3] = matrix34.m(7);
		floats[2][0] = matrix34.m(8);
		floats[2][1] = matrix34.m(9);
		floats[2][2] = matrix34.m(10);
		floats[2][3] = matrix34.m(11);
		return floats;
	}
	
	public float[][] matrixTo2dFloatArray(Matrix4f matrix4f) {
		float[][] floats = new float[4][4];
		floats[0][0] = matrix4f.get(0,0);
		floats[0][1] = matrix4f.get(0,1);
		floats[0][2] = matrix4f.get(0,2);
		floats[0][3] = matrix4f.get(0,3);
		floats[1][0] = matrix4f.get(1,0);
		floats[1][1] = matrix4f.get(1,1);
		floats[1][2] = matrix4f.get(1,2);
		floats[1][3] = matrix4f.get(1,3);
		floats[2][0] = matrix4f.get(2,0);
		floats[2][1] = matrix4f.get(2,1);
		floats[2][2] = matrix4f.get(2,2);
		floats[2][3] = matrix4f.get(2,3);
		floats[3][0] = matrix4f.get(3,0);
		floats[3][1] = matrix4f.get(3,1);
		floats[3][2] = matrix4f.get(3,2);
		floats[3][3] = matrix4f.get(3,3);
		return floats;
	}
}
