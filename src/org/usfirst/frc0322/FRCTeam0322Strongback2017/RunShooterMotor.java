package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.Motor;

public class RunShooterMotor extends Command {
	private final Motor shooterMotor;
	private final Motor agitatorMotor;
	private final double shooterSpeed;
	
	public RunShooterMotor(Motor shooterMotor, Motor agitatorMotor, double shooterSpeed) {
		super(shooterMotor, agitatorMotor);
		this.shooterMotor = shooterMotor;
		this.agitatorMotor = agitatorMotor;
		this.shooterSpeed = shooterSpeed;
	}
	
	@Override
	public boolean execute() {
		this.shooterMotor.setSpeed(shooterSpeed);
		this.agitatorMotor.setSpeed(1.0);
		return true;
	}
}
