package com.tfc.fabrivr.utils.openvr;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openvr.OpenVR;
import org.lwjgl.openvr.VR;
import org.lwjgl.openvr.VRSystem;
import org.lwjgl.ovr.OVR;
import org.lwjgl.ovr.OVRGraphicsLuid;
import org.lwjgl.ovr.OVRTrackingState;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class Session {
	public static PointerBuffer session;
	public static OVRGraphicsLuid luid;
	protected static OVRTrackingState trackingState;
	public static final int mode;
	public static void init() {
	}
	
	static {
		int m = 0;
		if (!VR.VR_IsRuntimeInstalled()) {
			int result = OVR.ovr_Initialize(null);
			if (result <= 0) throw new RuntimeException("Runtime is not installed");
			session = BufferUtils.createPointerBuffer(1);
			luid = OVRGraphicsLuid.create();
			
			if (OVR.ovr_Create(session, luid) != 0) {
				System.err.println("Couldn't create OVR!");
				System.exit(-1);
			}
			
			trackingState = OVRTrackingState.malloc();
			
			m = 2;
		} else {
			if (!VR.VR_IsHmdPresent()) throw new RuntimeException("Hmd is not present");
			MemoryStack stack = MemoryStack.stackPush();
			IntBuffer peError = stack.mallocInt(1);
			
			int token = VR.VR_InitInternal(peError, 0);
			
			OpenVR.create(token);
			
			if (OpenVR.VRCompositor == null) {
				end();
				throw new RuntimeException("VRCompositor does not exist");
			}
			
			m = 0;
			
			boolean hasOculus = false;
			
			for (int i = 0; i < VR.k_unMaxTrackedDeviceCount; i++) {
				int classCallback = VRSystem.VRSystem_GetTrackedDeviceClass(i);
				Tracking.devices.add(new Device(i));
				if (classCallback == VR.ETrackedDeviceClass_TrackedDeviceClass_Controller || classCallback == VR.ETrackedDeviceClass_TrackedDeviceClass_GenericTracker || i == 0) {
					IntBuffer error = BufferUtils.createIntBuffer(1);
					System.out.println("Device: " + i);
					String controllerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_TrackingSystemName_String, error);
					System.out.println(controllerName);
					String controllerType = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_ControllerType_String, error);
					System.out.println(controllerType);
					String manufacturerName = VRSystem.VRSystem_GetStringTrackedDeviceProperty(i, VR.ETrackedDeviceProperty_Prop_ManufacturerName_String, error);
					System.out.println(manufacturerName);
					
					if (manufacturerName.toLowerCase().equals("oculus")) {
						hasOculus = true;
					}
				}
			}
			
//			if (hasOculus) {
//				OVR.ovr_Initialize(null);
//				session = BufferUtils.createPointerBuffer(1);
//				luid = OVRGraphicsLuid.create();
//
//				if (OVR.ovr_Create(session, luid) != 0) {
//					System.err.println("Couldn't create OVR!");
//					System.exit(-1);
//				}
//
//				trackingState = OVRTrackingState.malloc();
//
//				m++;
//			}
		}
		
		mode = m;
	}
	
	public static void end() {
		OpenVR.destroy();
	}
	
	public static String getMode() {
		return mode == 0 ? "OpenVR Only" : (mode == 1 ? "Hybrid" : "OVR Only");
	}
}
