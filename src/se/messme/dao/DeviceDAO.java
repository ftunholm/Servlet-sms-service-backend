package se.messme.dao;

import se.messme.Event;
import se.messme.entities.Device;

public interface DeviceDAO {
	
	public Device getDevice(int userId);
	
	public Event createDevice(Device device);

}
