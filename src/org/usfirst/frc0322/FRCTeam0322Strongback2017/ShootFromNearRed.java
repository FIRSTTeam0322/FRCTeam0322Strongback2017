package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;

public class ShootFromNearRed extends Command {
	private final TankDrive drivetrain;
	private final Motor agitatorMotor, shooterMotor;
	private final double speed, rightSpeed;
	private int step = 0;
	
	public ShootFromNearRed(TankDrive drivetrain, Motor shooterMotor, Motor agitatorMotor, double speed) {
		super(drivetrain, agitatorMotor, shooterMotor);
		this.drivetrain = drivetrain;
		this.agitatorMotor = agitatorMotor;
		this.shooterMotor = shooterMotor;
		this.speed = -(speed);
		this.rightSpeed = -(speed * .8);
	}
	
	@Override
	public boolean execute() {
		if((Robot.leftEncoder.getAngle() <= 10.0 || Robot.rightEncoder.getAngle() <= 10.0) && step <= 1) {
			drivetrain.tank(speed, rightSpeed);
			step = 1;
			return false;
		}else if(Robot.imu.getAngle() > 90.0 && step <= 2) {
			drivetrain.tank(0.5, 0.0);
			step = 2;
			return false;
		}else if(Robot.leftEncoder.getAngle() != 0.0 && Robot.rightEncoder.getAngle() != 0.0 && step <= 3) {
			Robot.leftEncoder.zero();
			Robot.rightEncoder.zero();
			step = 3;
			return false;
		}else if((Robot.leftEncoder.getAngle() <= 20.0 || Robot.rightEncoder.getAngle() <= 20.0) && step <= 4) {
			drivetrain.tank(speed, rightSpeed);
			step = 4;
			return false;
		} else {
			shooterMotor.setSpeed(0.75);
			agitatorMotor.setSpeed(1.0);
			step = 5;
			return true;
		}
	}
}
