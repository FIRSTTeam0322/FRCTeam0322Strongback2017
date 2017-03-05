package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.drive.TankDrive;

public class PlaceGearCenter extends Command {
	private final TankDrive drivetrain;
	private final AngleSensor leftEncoder, rightEncoder; 
	private final double speed, rightSpeed, distance;
	
	public PlaceGearCenter(TankDrive drivetrain, AngleSensor leftEncoder, AngleSensor rightEncoder, double speed, double distance) {
		super(drivetrain);
		this.drivetrain = drivetrain;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
		this.speed = speed;
		this.rightSpeed = (speed * .8);
		this.distance = (distance * -1);
	}
	
	@Override
	public boolean execute() {
		if(leftEncoder.getAngle() > distance/* && this.rightEncoder.getAngle() >= -75.0*/)
			this.drivetrain.tank(speed, rightSpeed);
		else
			this.drivetrain.stop();
		return true;
	}
}
