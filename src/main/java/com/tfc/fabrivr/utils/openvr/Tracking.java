package com.tfc.fabrivr.utils.openvr;

import org.joml.Vector3d;
import org.lwjgl.openvr.*;
import org.lwjgl.ovr.OVR;

import java.util.ArrayList;

public class Tracking {
	public static final TrackedPart head = new TrackedPart();
	public static final TrackedPart leftHand = new TrackedPart();
	public static final TrackedPart rightHand = new TrackedPart();
	
	public static final Device headMountedDisplayDevice = new Device(0);
	
	protected static ArrayList<Device> devices = new ArrayList<>();
	
	public static void update() {
		if (Session.session != null) {
			OVR.ovr_GetTrackingState(
					Session.session.get(0),
					OVR.ovr_GetTimeInSeconds(),
					true,
					Session.trackingState
			);
		}
		
		for (DeviceRole value : DeviceRole.values()) {
			if (value.equals(DeviceRole.RIGHT_HAND)) {
				Device device = Device.getDeviceForRole(value);
				if (device.index == 0) continue;
				if (!device.isPresent()) continue;
				fillTrackedPart(device, rightHand);
			}
			
			if (value.equals(DeviceRole.INVALID)) {
				Device device = new Device(0);
				if (!device.isPresent()) continue;
				fillTrackedPart(device, head);
			}
			
			if (value.equals(DeviceRole.LEFT_HAND)) {
				Device device = Device.getDeviceForRole(value);
				if (device.index == 0) continue;
				if (!device.isPresent()) continue;
				fillTrackedPart(device, leftHand);
			}
		}
	}
	
	private static void fillTrackedPart(Device device, TrackedPart part) {
		part.position = new Vector3d(-1000, -1000, -1000);
		HmdVector3 vector3 = device.getPosition();
		part.position.x = vector3.v(0);
		part.position.y = vector3.v(1);
		part.position.z = vector3.v(2);
		part.rotation = device.getRotation();
	}
}
