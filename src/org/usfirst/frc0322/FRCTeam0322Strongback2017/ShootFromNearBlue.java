package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;

import com.analog.adis16448.frc.ADIS16448_IMU;

public class ShootFromNearBlue extends Command {
	private final TankDrive drivetrain;
	private final Motor agitatorMotor, shooterMotor;
	private final ADIS16448_IMU imu;
	private final AngleSensor leftEncoder, rightEncoder; 
	private final double speed, rightSpeed;
	private int step = 0;
	
	public ShootFromNearBlue(TankDrive drivetrain, Motor shooterMotor, Motor agitatorMotor, ADIS16448_IMU imu, AngleSensor leftEncoder, AngleSensor rightEncoder, double speed) {
		super(drivetrain, agitatorMotor, shooterMotor);
		this.drivetrain = drivetrain;
		this.agitatorMotor = agitatorMotor;
		this.shooterMotor = shooterMotor;
		this.imu = imu;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
		this.speed = -(speed);
		this.rightSpeed = -(speed * .8);
	}
	
	@Override
	public boolean execute() {
		if((leftEncoder.getAngle() <= 10.0 || rightEncoder.getAngle() <= 10.0) && step <= 1) {
			drivetrain.tank(speed, rightSpeed);
			step = 1;
			return false;
		}else if(imu.getAngle() < 270.0 && step <= 2) {
			drivetrain.tank(0.0, -0.5);
			step = 2;
			return false;
		}else if(leftEncoder.getAngle() != 0.0 && rightEncoder.getAngle() != 0.0 && step <= 3) {
			leftEncoder.zero();
			rightEncoder.zero();
			step = 3;
			return false;
		}else if((leftEncoder.getAngle() <= 20.0 || rightEncoder.getAngle() <= 20.0) && step <= 4) {
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
