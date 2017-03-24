package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.drive.TankDrive;

public class PlaceGearCenter extends Command {
	private final TankDrive drivetrain; 
	private final double speed, rightSpeed, distance;
	
	public PlaceGearCenter(TankDrive drivetrain, double speed, double distance) {
		super(drivetrain);
		this.drivetrain = drivetrain;
		this.speed = speed;
		this.rightSpeed = (speed * .8);
		this.distance = (distance * -1);
	}
	
	@Override
	public boolean execute() {
		if(Math.abs(Robot.leftEncoder.getAngle()) < distance && Math.abs(Robot.rightEncoder.getAngle()) < distance) {
			drivetrain.tank(speed, speed);
			return false;
		}else {
			drivetrain.stop();
			return true;
		}
	}
}
