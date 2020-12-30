package com.tfc.fabrivr.client;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.ovr.*;

public class FabriVROculus {
	public static OVRQuatf headQuat;
	public static OVRQuatf hand1Quat;
	public static OVRQuatf hand2Quat;
	public static OVRVector3f headPos;
	public static OVRVector3f hand1Pos;
	public static OVRVector3f hand2Pos;
	public static PointerBuffer session;
	public static OVRGraphicsLuid luid;
	private static OVRTrackingState trackingState;
	
	public static void initVR() {
		OVR.ovr_Initialize(null);
		session = BufferUtils.createPointerBuffer(1);
		luid = OVRGraphicsLuid.create();
		
		if (OVR.ovr_Create(session, luid) != 0) {
			System.err.println("Couldn't create OVR!");
			System.exit(-1);
		}
		
		trackingState = OVRTrackingState.malloc();
	}
	
	public static void update() {
		OVR.ovr_GetTrackingState(
				session.get(0),
				OVR.ovr_GetTimeInSeconds(),
				true,
				trackingState
		);
		OVRPoseStatef poseState = trackingState.HeadPose();
		OVRPosef pose = poseState.ThePose();
		headQuat = pose.Orientation();
		headPos = pose.Position();
		
		for (int i = 0; i <= 1; i++) {
			try {
				pose = trackingState.HandPoses(i).ThePose();
				if (i == 0) {
					hand1Pos = pose.Position();
					hand1Quat = pose.Orientation();
				} else {
					hand2Pos = pose.Position();
					hand2Quat = pose.Orientation();
				}
			} catch (Throwable ignored) {
			}
		}
	}
	
	private static OVRRecti createRecti(int x, int y, int w, int h) {
		OVRVector2i pos = OVRVector2i.malloc();
		pos.set(x, y);
		OVRSizei size = OVRSizei.malloc();
		size.set(w, h);
		
		OVRRecti recti = OVRRecti.malloc();
		recti.set(pos, size);
		return recti;
	}
	
	public static void destroy() {
//		OVR.ovr_DestroyTextureSwapChain(session.get(0), FabriVRClient.textureSwapChain.get(0));
		OVR.ovr_Destroy(session.get(0));
		OVR.ovr_Shutdown();
	}
}
