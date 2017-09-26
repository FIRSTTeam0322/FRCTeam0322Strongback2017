package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;

public class RunArtPrizeSlideMotor extends Command {
	private final Motor motor;
	private final ContinuousRange speed;
	
	public RunArtPrizeSlideMotor(Motor motor, ContinuousRange speed) {
		super(motor);
		this.motor = motor;
		this.speed = speed;
	}
	
	@Override
	public boolean execute() {
		this.motor.setSpeed(this.speed.read());
		return true;
	}
}
