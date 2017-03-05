package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.drive.TankDrive;

public class DriveBackward extends Command {
	private final TankDrive drivetrain;
	private final double speed, rightSpeed, distance;
	private AngleSensor leftEncoder, rightEncoder;
	//private int count = 0;
	
	public DriveBackward(TankDrive drivetrain, AngleSensor leftEncoder, AngleSensor rightEncoder, double speed, double distance) {
		super(drivetrain);
		this.drivetrain = drivetrain;
		this.speed = speed;
		this.rightSpeed = (speed * .75);
		this.distance = Math.abs(distance);
	}
	
	@Override
	public boolean execute() {
		System.out.println("this");
		try {
			for(int count=0; count < 2400/*distance && Math.abs(this.rightEncoder.getAngle()) <= distance*/; count++) {
				this.drivetrain.tank(speed, rightSpeed);
		    	System.out.println(count);
		    	System.out.println(distance);
				} this.drivetrain.tank(0,0);
			} catch(NullPointerException n) {
				System.err.println(n);
			}
			return true;
	}
}
