package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.Motor;

public class StopShooterMotor extends Command {
	private final Motor motor;
	private final Motor motor2;
	
	public StopShooterMotor(Motor motor, Motor motor2) {
		super(motor, motor2);
		this.motor = motor;
		this.motor2 = motor2;
	}
	
	@Override
	public boolean execute() {
		this.motor.setSpeed(0.0);
		this.motor2.setSpeed(0.0);
		return true;
	}
}
