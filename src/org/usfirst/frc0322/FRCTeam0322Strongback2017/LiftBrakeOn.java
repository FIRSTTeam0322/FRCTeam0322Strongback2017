package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;

import com.ctre.CANTalon;

public class LiftBrakeOn extends Command {
	private final CANTalon talon;
	
	public LiftBrakeOn(CANTalon talon) {
		//super(talon);
		this.talon = talon;
	}
	
	@Override
	public boolean execute() {
		talon.enableBrakeMode(true);
		return true;
	}
}
