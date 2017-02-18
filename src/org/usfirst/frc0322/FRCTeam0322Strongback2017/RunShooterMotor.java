package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.Motor;

public class RunShooterMotor extends Command {
	private final Motor motor;
	private final Motor motor2;
	
	public RunShooterMotor(Motor motor, Motor motor2) {
		super(motor, motor2);
		this.motor = motor;
		this.motor2 = motor2;
	}
	
	@Override
	public boolean execute() {
		this.motor.setSpeed(0.6);
		this.motor2.setSpeed(1.0);
		return true;
	}
}
