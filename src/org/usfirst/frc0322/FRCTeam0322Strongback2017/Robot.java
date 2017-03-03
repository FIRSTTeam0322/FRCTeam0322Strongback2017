/* Created Sat Jan 14 20:17:30 EST 2017 */
package org.usfirst.frc0322.FRCTeam0322Strongback2017;

import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.command.Command;
import org.strongback.components.AngleSensor;
import org.strongback.components.CurrentSensor;
import org.strongback.components.Motor;
import org.strongback.components.VoltageSensor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

import com.ctre.CANTalon;

import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	private static final int LEFT_DRIVESTICK_PORT = 0;
	private static final int RIGHT_DRIVESTICK_PORT = 1;
	private static final int MANIPULATOR_STICK_PORT = 2;
	
	private static final int LF_MOTOR_PORT = 0;
	private static final int RF_MOTOR_PORT = 1;
	private static final int LR_MOTOR_PORT = 2;
	private static final int RR_MOTOR_PORT = 3;
	private static final int PICKUP_MOTOR_PORT = 4;
	private static final int AGITATOR_MOTOR_PORT = 5;
	private static final int INDEX_MOTOR_PORT = 6;
	
	private static final int LIFT_MOTOR_CAN = 1;
	private static final int SHOOTER_MOTOR_CAN = 2;
	
	private static final int LEFT_ENCOODER_PORT_A = 0;
	private static final int LEFT_ENCOODER_PORT_B = 1;
	private static final int RIGHT_ENCOODER_PORT_A = 2;
	private static final int RIGHT_ENCOODER_PORT_B = 3;
	
	public static final double WHEEL_DIAMETER = 6;
	public static final double PULSE_PER_REVOLUTION = 360.0;
	public static final double ENCODER_GEAR_RATIO = 1.0;
	public static final double GEAR_RATIO = 1.0;
	public static final double FUDGE_FACTOR = 1.0;
	private static final double ENCODER_PULSE_DISTANCE = 
			Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION / ENCODER_GEAR_RATIO / GEAR_RATIO * FUDGE_FACTOR;
	
	private static double autonSpeed, autonDistance, shooterSpeed;
	
	private FlightStick leftDriveStick, rightDriveStick;
	private Gamepad manipulatorStick;
	
	private TankDrive drivetrain;
	private ContinuousRange leftSpeed, rightSpeed, driveSpeed, turnSpeed;
	
	private SwitchReactor liftFwd, liftRev, liftStop, pickup, shooter, liftbrake;
	private CANTalon liftMotorCAN, shooterMotorCAN;
	private Motor liftMotor, pickupMotor, shooterMotor, agitatorMotor, indexMotor;
	
	private DriverStation ds;
	
	private int endOfMatchReady;
	
	private ADIS16448_IMU imu;
	
	private AngleSensor leftEncoder, rightEncoder;
	
	Command autonomousCommand;
	SendableChooser<Command> autoChooser;
	Preferences robotPrefs;
	
	public static UsbCamera cameraServer;

    @Override
    public void robotInit() {
    	//Setup drivetrain
    	Motor leftDriveMotors = Motor.compose(Hardware.Motors.talon(LF_MOTOR_PORT),
    											Hardware.Motors.talon(LR_MOTOR_PORT));
    	Motor rightDriveMotors = Motor.compose(Hardware.Motors.talon(RF_MOTOR_PORT),
    											Hardware.Motors.talon(RR_MOTOR_PORT));
    	drivetrain = new TankDrive(leftDriveMotors.invert(), rightDriveMotors);

    	//Setup Manipulators
    	liftMotorCAN = new CANTalon(LIFT_MOTOR_CAN);
    	shooterMotorCAN = new CANTalon(SHOOTER_MOTOR_CAN);
    	
    	liftMotor = Hardware.Motors.talonSRX(liftMotorCAN);
    	shooterMotor = Hardware.Motors.talonSRX(shooterMotorCAN);
    	pickupMotor = Hardware.Motors.talon(PICKUP_MOTOR_PORT);
    	agitatorMotor = Hardware.Motors.talon(AGITATOR_MOTOR_PORT);
    	indexMotor = Hardware.Motors.talon(INDEX_MOTOR_PORT);
    	
    	//Setup joysticks
    	leftDriveStick = Hardware.HumanInterfaceDevices.logitechAttack3D(LEFT_DRIVESTICK_PORT);
    	rightDriveStick = Hardware.HumanInterfaceDevices.logitechAttack3D(RIGHT_DRIVESTICK_PORT);
    	manipulatorStick = Hardware.HumanInterfaceDevices.xbox360(MANIPULATOR_STICK_PORT);
    	
    	//Setup sensors
    	imu = new ADIS16448_IMU();
    	imu.calibrate();
    	leftEncoder = AngleSensor.invert(Hardware.AngleSensors.encoder(LEFT_ENCOODER_PORT_A, LEFT_ENCOODER_PORT_B, ENCODER_PULSE_DISTANCE));
    	rightEncoder = Hardware.AngleSensors.encoder(RIGHT_ENCOODER_PORT_A, RIGHT_ENCOODER_PORT_B, ENCODER_PULSE_DISTANCE);
    	VoltageSensor battery = Hardware.powerPanel().getVoltageSensor();
    	CurrentSensor current = Hardware.powerPanel().getTotalCurrentSensor();
    	
    	//Setup drivetrain variables
    	ContinuousRange sensitivity = leftDriveStick.getAxis(2).invert().map(t -> (t + 1.0) / 2.0);
    	leftSpeed = leftDriveStick.getPitch().scale(sensitivity::read);
    	rightSpeed = rightDriveStick.getPitch().scale(sensitivity::read);
    	//driveSpeed = leftDriveStick.getPitch().scale(sensitivity::read);
    	//turnSpeed = leftDriveStick.getRoll().scale(sensitivity::read);
    	
    	//Setup Switches
    	liftFwd = Strongback.switchReactor();
    	liftRev = Strongback.switchReactor();
    	liftStop = Strongback.switchReactor();
    	pickup = Strongback.switchReactor();
    	shooter = Strongback.switchReactor();
    	liftbrake = Strongback.switchReactor();    	
    	
    	//Setup Defaults for Robot Preference Variables
    	autonSpeed = 0.60;
    	autonDistance = 100.0;
    	shooterSpeed = 75.0;
    	
    	//Put Auton Chooser on Dashboard
    	autoChooser = new SendableChooser<Command>();
    	autoChooser.addDefault("Default Program (Do Nothing)", new DoNothing());
    	autoChooser.addObject("Place Gear (Center)", new PlaceGearCenter(drivetrain, leftEncoder, rightEncoder, autonSpeed));
    	autoChooser.addObject("Shoot From Near Blue", new ShootFromNearBlue(drivetrain, shooterMotor, agitatorMotor, imu, leftEncoder, rightEncoder, autonSpeed));
    	autoChooser.addObject("Shoot From Near Red", new ShootFromNearRed(drivetrain, shooterMotor, agitatorMotor, imu, leftEncoder, rightEncoder, autonSpeed));
    	autoChooser.addObject("Drive Backward (Toward Gear Holder)", new DriveBackward(drivetrain, leftEncoder, rightEncoder, autonSpeed, autonDistance));
    	autoChooser.addObject("Drive Forward (Toward Ball Box)", new DriveForward(drivetrain, leftEncoder, rightEncoder, autonSpeed, autonDistance));
    	SmartDashboard.putData("Autonomous Mode Chooser", autoChooser);

    	//Setup Other Variables
    	endOfMatchReady = 0;
    	
    	//Setup Camera
    	cameraServer = CameraServer.getInstance().startAutomaticCapture();
    	cameraServer.setResolution(640, 360);
    	
    	Strongback.dataRecorder()
		.register("Battery Volts", 1000, battery::getVoltage)
		.register("Current load", 1000, current::getCurrent)
		.register("Left Motors", leftDriveMotors)
		.register("Right Motors", rightDriveMotors)
		.register("LeftDriveStick", 1000, leftSpeed::read)
		.register("RightDriveStick", 1000, rightSpeed::read)
		.register("Drive Sensitivity", 1000, sensitivity::read);
        
    	Strongback.configure().recordNoEvents().recordDataToFile("/home/lvuser/HephaestusData-<counter>.dat");
    	//Strongback.configure().recordNoEvents().recordNoData();
    }
	@Override
    public void autonomousInit() {
        // Start Strongback functions ...
        Strongback.start();
        autonomousCommand = (Command) autoChooser.getSelected();
        autonomousCommand.execute();
    }
    
	@Override
    public void autonomousPeriodic() {
		updateDashboard();
		debugPrint();
    }
    
    @Override
    public void teleopInit() {
        // Start Strongback functions ...
        Strongback.start();
        liftMotorCAN.enableBrakeMode(true);
    }

    @Override
    public void teleopPeriodic() {
    	//This line runs the drivetrain
    	drivetrain.tank(leftSpeed.read(), rightSpeed.read());
    	//drivetrain.arcade(driveSpeed.read(), turnSpeed.read());

    	//This section controls the lift
    	liftFwd.onTriggered(manipulatorStick.getA(), ()->Strongback.submit(new RunLiftMotor(liftMotor)));
    	liftFwd.onUntriggered(manipulatorStick.getA(), ()->Strongback.submit(new StopLiftMotor(liftMotor)));
    	liftRev.onTriggered(manipulatorStick.getY(), ()->Strongback.submit(new ReverseLiftMotor(liftMotor)));
    	liftRev.onUntriggered(manipulatorStick.getY(), ()->Strongback.submit(new StopLiftMotor(liftMotor)));
    	liftStop.onTriggered(manipulatorStick.getLeftBumper(), ()->Strongback.submit(new StopLiftMotor(liftMotor)));
    	liftbrake.onTriggered(manipulatorStick.getRightBumper(), ()->Strongback.submit(new LiftBrakeOff(liftMotorCAN)));
    	liftbrake.onUntriggered(manipulatorStick.getRightBumper(), ()->Strongback.submit(new LiftBrakeOn(liftMotorCAN)));
    	
    	//This section controls the pickup mechanism
    	pickup.onTriggered(manipulatorStick.getSelect(), ()->Strongback.submit(new RunPickupMotor(pickupMotor)));
    	pickup.onUntriggered(manipulatorStick.getSelect(), ()->Strongback.submit(new StopPickupMotor(pickupMotor)));
    	
    	//This section controls the shooter mechanism
    	shooter.onTriggered(manipulatorStick.getX(), ()->Strongback.submit(new RunShooterMotor(shooterMotor, agitatorMotor, shooterSpeed)));
    	shooter.onTriggered(manipulatorStick.getB(), ()->Strongback.submit(new StopShooterMotor(shooterMotor, agitatorMotor)));

    	endOfMatchReady = 1;
    	
    	updateDashboard();
    	debugPrint();
    }

    @Override
    public void disabledInit() {
    	drivetrain.stop();
    	
    	//Setup Robot Preferences
    	robotPrefs = Preferences.getInstance();
    	autonSpeed = robotPrefs.getDouble("Autonomous Speed", 0.60);
    	autonDistance = robotPrefs.getDouble("Autonomous Distance", 100.0);
    	shooterSpeed = robotPrefs.getDouble("Shooter Speed", 0.75);
    	SmartDashboard.putNumber("RobotPref Autonomous Speed", autonSpeed);
    	SmartDashboard.putNumber("RobotPref Autonomous Distance", autonDistance);
    	SmartDashboard.putNumber("RobotPref Shooter Speed", shooterSpeed);
    	
    	// Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }
    
	@Override
    public void disabledPeriodic() {
		try {
			if(ds.isFMSAttached() && ds.getMatchTime() <= 0.0 && endOfMatchReady == 1) {
				try {
					wait(2500);
				} catch (InterruptedException e) {
					liftMotorCAN.enableBrakeMode(true);
					System.err.println(e);
				}
			}
		} catch (NullPointerException n) {
			System.err.println(n);
		}
		liftMotorCAN.enableBrakeMode(false);
		updateDashboard();
		debugPrint();
    }
	
	public void debugPrint() {
		System.out.println("Gyro Angle " + imu.getAngle());
    	System.out.println();
    	System.out.println("X-Axis Acceleration " + imu.getAccelX());
    	System.out.println("Y-Axis Acceleration " + imu.getAccelY());
    	System.out.println("Z-Axis Acceleration " + imu.getAccelZ());
    	System.out.println();
    	System.out.println("Temperature " + imu.getTemperature());
    	System.out.println("Pressure  " + imu.getBarometricPressure());
    	System.out.println();
    	System.out.println("Left Distance " + leftEncoder.getAngle());
    	System.out.println("Right Distance " + rightEncoder.getAngle());
    	System.out.println();
	}
	
	public void updateDashboard() {
		SmartDashboard.putNumber("Gyro Angle", imu.getAngle());
		SmartDashboard.putNumber("X-Axis Acceleration", imu.getAccelX());
		SmartDashboard.putNumber("Y-Axis Acceleration", imu.getAccelY());
		SmartDashboard.putNumber("Z-Axis Acceleration", imu.getAccelZ());
		SmartDashboard.putNumber("Temperature", imu.getTemperature());
		SmartDashboard.putNumber("Pressure", imu.getBarometricPressure());
		SmartDashboard.putNumber("Left Distance", leftEncoder.getAngle());
		SmartDashboard.putNumber("Right Distance", rightEncoder.getAngle());
		SmartDashboard.putBoolean("Lift Brake", liftMotorCAN.getBrakeEnableDuringNeutral());
	}

}
