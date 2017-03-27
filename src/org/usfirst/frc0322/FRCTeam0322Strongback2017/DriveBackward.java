package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.drive.TankDrive;

public class DriveBackward extends Command {
	private final TankDrive drivetrain;
	private final double speed, rightSpeed, distance;
	private int count = 0;
	
	public DriveBackward(TankDrive drivetrain, double speed, double distance) {
		super(drivetrain);
		this.drivetrain = drivetrain;
		this.speed = speed;
		this.rightSpeed = (speed * .75);
		this.distance = distance;
	}
	
	@Override
	public boolean execute() {
		if(count < distance) {
			drivetrain.tank(speed, speed);
			count++;
			return false;
		}else {
			drivetrain.stop();
			return true;
		}
	}
	/*public boolean execute() {
		if(Math.abs(Robot.leftEncoder.getAngle()) < distance && Math.abs(Robot.rightEncoder.getAngle()) < distance) {
			drivetrain.tank(speed, rightSpeed);
			return false;
		}else {
			drivetrain.stop();
			return true;
		}
	}*/
	/*public boolean execute() {
		for(int count=0; count < 2400 ; count++) {
			drivetrain.tank(speed, rightSpeed);
		    System.out.println(count);
		    System.out.println(distance);
		}
		drivetrain.stop();
		return true;
	}*/
}
