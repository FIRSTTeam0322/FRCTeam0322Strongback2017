package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.drive.TankDrive;

public class PlaceGearCenter extends Command {
	private final TankDrive drivetrain;
	private final AngleSensor leftEncoder, rightEncoder; 
	private final double speed, rightSpeed;
	
	public PlaceGearCenter(TankDrive drivetrain, AngleSensor leftEncoder, AngleSensor rightEncoder, double speed) {
		super(drivetrain);
		this.drivetrain = drivetrain;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
		this.speed = speed;
		this.rightSpeed = (speed * .8);
	}
	
	@Override
	public boolean execute() {
		if((Math.abs(leftEncoder.getAngle()) <= 78.5 || Math.abs(rightEncoder.getAngle()) <= 78.5))
			this.drivetrain.tank(speed, rightSpeed);
		else
			this.drivetrain.tank(0.0, 0.0);
		return true;
	}
}
