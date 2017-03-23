package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.drive.TankDrive;

public class DriveForward extends Command {
	private final TankDrive drivetrain;
	private final double speed, rightSpeed, distance;
	private final AngleSensor leftEncoder, rightEncoder;
	private int count = 0;
	
	public DriveForward(TankDrive drivetrain, AngleSensor leftEncoder, AngleSensor rightEncoder, double speed, double distance) {
		super(drivetrain);
		this.drivetrain = drivetrain;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
		this.speed = speed;
		this.rightSpeed = (speed * .8);
		this.distance = distance;
	}
	
	@Override
	public boolean execute() {
		if(count < distance) {
			drivetrain.tank(-(speed), -(rightSpeed));
			count++;
			return false;
		}else {
			drivetrain.stop();
			return true;
		}
	}
	/*public boolean execute() {
		if(Math.abs(leftEncoder.getAngle()) < distance && Math.abs(rightEncoder.getAngle()) < distance) {
			drivetrain.tank(-(speed), -(rightSpeed));
			return false;
		}else {
			drivetrain.stop();
			return true;
		}
	}*/
	/*public boolean execute() {
		for(int count=0; count < 2400 ; count++) {
			drivetrain.tank(-(speed), -(rightSpeed));
		    System.out.println(count);
		    System.out.println(distance);
		}
		drivetrain.stop();
		return true;
	}*/
}
