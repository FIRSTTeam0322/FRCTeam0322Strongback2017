package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.AngleSensor;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.ctre.CANTalon;
import com.sun.org.apache.bcel.internal.generic.IMUL;

public class ShootFromNearBlue extends Command {
	private final TankDrive drivetrain;
	private final Motor agitatorMotor, shooterMotor;
	private final CANTalon shooterMotorCAN;
	private final ADIS16448_IMU imu;
	private final AngleSensor leftEncoder, rightEncoder; 
	private final double speed, rightSpeed;
	private int step;
	
	public ShootFromNearBlue(TankDrive drivetrain, Motor shooterMotor, Motor agitatorMotor, CANTalon shooterMotorCAN, ADIS16448_IMU imu, AngleSensor leftEncoder, AngleSensor rightEncoder, double speed) {
		super(drivetrain, agitatorMotor, shooterMotor);
		this.drivetrain = drivetrain;
		this.agitatorMotor = agitatorMotor;
		this.shooterMotor = shooterMotor;
		this.shooterMotorCAN = shooterMotorCAN;
		this.imu = imu;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
		this.speed = -(speed);
		this.rightSpeed = -(speed * .8);
		step = 0;
	}
	
	@Override
	public boolean execute() {
		if((leftEncoder.getAngle() <= 10.0 || rightEncoder.getAngle() <= 10.0) && step <= 1) {
			this.drivetrain.tank(speed, rightSpeed);
			step = 1;
		}else if(imu.getAngle() > 270.0 && step <= 2) {
			this.drivetrain.tank(0.0, -0.5);
			step = 2;
		}else if((leftEncoder.getAngle() <= 20.0 || rightEncoder.getAngle() <= 20.0) && step <= 3) {
			this.drivetrain.tank(speed, rightSpeed);
			step = 3;
		} else {
			this.shooterMotor.setSpeed(0.75);
			this.agitatorMotor.setSpeed(1.0);
			step = 4;
		}
		return true;
	}
}
