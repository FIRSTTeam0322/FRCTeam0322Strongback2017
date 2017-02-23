package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;

import com.analog.adis16448.frc.ADIS16448_IMU;

public class ShootFromNearRed extends Command {
	private final TankDrive drivetrain;
	private final Motor agitatorMotor, shooterMotor;
	private final ADIS16448_IMU imu;
	private final AngleSensor leftEncoder, rightEncoder; 
	private final double speed, rightSpeed;
	private int step = 0;
	
	public ShootFromNearRed(TankDrive drivetrain, Motor shooterMotor, Motor agitatorMotor, ADIS16448_IMU imu, AngleSensor leftEncoder, AngleSensor rightEncoder, double speed) {
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
		if((this.leftEncoder.getAngle() <= 10.0 || this.rightEncoder.getAngle() <= 10.0) && step <= 1) {
			this.drivetrain.tank(speed, rightSpeed);
			step = 1;
		}else if(imu.getAngle() > 90.0 && step <= 2) {
			this.drivetrain.tank(0.5, 0.0);
			step = 2;
		}else if(this.leftEncoder.getAngle() != 0.0 && this.rightEncoder.getAngle() != 0.0 && step <= 3) {
			this.leftEncoder.zero();
			this.rightEncoder.zero();
			step = 3;
		}else if((this.leftEncoder.getAngle() <= 20.0 || this.rightEncoder.getAngle() <= 20.0) && step <= 4) {
			this.drivetrain.tank(speed, rightSpeed);
			step = 4;
		} else {
			this.shooterMotor.setSpeed(0.75);
			this.agitatorMotor.setSpeed(1.0);
			step = 5;
		}
		return true;
	}
}
