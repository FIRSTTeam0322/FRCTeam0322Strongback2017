package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.drive.TankDrive;

public class DriveBackward extends Command {
	private final TankDrive drivetrain;
	private final double speed, rightSpeed;
	
	public DriveBackward(TankDrive drivetrain, double speed) {
		super(drivetrain);
		this.drivetrain = drivetrain;
		this.speed = speed;
		this.rightSpeed = (speed * .8);
	}
	
	@Override
	public boolean execute() {
		this.drivetrain.tank(speed, rightSpeed);
		return true;
	}
}
