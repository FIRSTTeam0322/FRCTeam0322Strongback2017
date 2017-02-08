package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.command.*;

import com.ctre.CANTalon;

public class LiftBrakeOff extends Command {
	private final CANTalon talon;
	
	public LiftBrakeOff(CANTalon talon) {
		//super(talon);
		this.talon = talon;
	}
	
	@Override
	public boolean execute() {
		talon.enableBrakeMode(false);
		return true;
	}
}
