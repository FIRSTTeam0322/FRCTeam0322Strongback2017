package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.drive.TankDrive;

public class DriveForward extends Command {
	private final TankDrive drivetrain;
	private final double speed, rightSpeed, distance;
	private final AngleSensor leftEncoder, rightEncoder;
	
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
		if (this.leftEncoder.getAngle() <= this.distance && this.rightEncoder.getAngle() <= distance) {
			this.drivetrain.tank(-speed, -rightSpeed);
		} else
			this.drivetrain.tank(0.0, 0.0);
		return true;
	}
}
