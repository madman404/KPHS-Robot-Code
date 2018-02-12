/*----------------------------------------------------------------------------*/
/*   							  |											  */
/*								  |											  */
/*								  + \										  */
/*								  \\.G_.*=.									  */
/*								   `( '/.\|									  */
/*								    .>' (_--.								  */
/*								 _=/d   ,^\									  */
/*								~~ \)-'   '									  */
/*								   / |										  */
/*								  '  '                                        */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
//import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.cscore.UsbCamera;

public class Robot extends TimedRobot {
	
	private double position = 3;
	private Joystick stick = new Joystick(0);
	private Talon lm = new Talon(0), 
				  rm = new Talon(1), 
				  m = new Talon(2);
	private DifferentialDrive drive = new DifferentialDrive(lm, rm);
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	private Encoder leftenc = new Encoder(0, 1), 
					rightenc = new Encoder(2, 3);
	private double string0, string1, string2;
	private AnalogInput pot = new AnalogInput(0);
	private boolean stop;
	private SpeedControllerGroup turn = new SpeedControllerGroup(lm, rm);
	private PIDController le = new PIDController(.25, 0, 0, leftenc, lm), 
						  re = new PIDController(.25, 0, 0, rightenc, rm), 
						  pidgyro = new PIDController(.025, 0, 0, gyro, turn), 
						  arm = new PIDController(2, 0.2, 0.0, pot, m);
	private Timer time = new Timer();
	private DoubleSolenoid shift = new DoubleSolenoid(0, 1),
                           grab = new DoubleSolenoid(2, 3),
                           punch = new DoubleSolenoid(4, 5);
	private Solenoid hug = new Solenoid(6);
	//private UsbCamera cam;
	//private CameraServer server;
	
	@Override
	public void robotInit()
    {
		//settings
		leftenc.setDistancePerPulse(18.85/117);
		rightenc.setDistancePerPulse(18.85/117);
		le.setAbsoluteTolerance(.5);
		re.setAbsoluteTolerance(.5);
		pidgyro.setAbsoluteTolerance(1);
		arm.setAbsoluteTolerance(0.5);
		lm.setSafetyEnabled(false);
		rm.setSafetyEnabled(false);
		m.setSafetyEnabled(false);
		drive.setSafetyEnabled(false);
		
		leftenc.setPIDSourceType(PIDSourceType.kDisplacement);
		rightenc.setPIDSourceType(PIDSourceType.kDisplacement);
		gyro.setPIDSourceType(PIDSourceType.kDisplacement);
		pot.setPIDSourceType(PIDSourceType.kDisplacement);
		le.setInputRange(-1000, 1000);
		re.setInputRange(-1000, 1000);
		pidgyro.setInputRange(-1000, 1000);
		arm.setInputRange(0.0, 5.0);
		le.setOutputRange(-.5, .5);
		re.setOutputRange(-.5, .5);
		pidgyro.setOutputRange(-0.55, 0.55);
		arm.setOutputRange(-.8, .4);
		m.setInverted(true);
		/*
		server = CameraServer.getInstance();
		cam = new UsbCamera("cam0", 0);
		cam.setFPS(15);
		cam.setResolution(160, 120);
		server.addCamera(cam);
		server.startAutomaticCapture();
		*/
	}

	@Override
	public void autonomousInit() 
	{
		stop = true;
		gyro.reset();
		leftenc.reset();
		rightenc.reset();
		shift.set(DoubleSolenoid.Value.kForward);
		grab.set(DoubleSolenoid.Value.kForward);
		hug.set(false);
		
		string0 = Double.parseDouble(SmartDashboard.getString("DB/String 0", "12"));
		string1 = Double.parseDouble(SmartDashboard.getString("DB/String 1", "32"));
		string2 = Double.parseDouble(SmartDashboard.getString("DB/String 2", "100"));
	}

	@Override
	public void autonomousPeriodic() 
	{
		if (stop)
		{
			
			stop = false;
			
			le.enable();
			re.enable();
			arm.enable();
			le.setSetpoint(-string0);
			re.setSetpoint(string0);
			arm.setSetpoint(3.9);
			time.start();
			while (time.get() < 1.5)
			{
				SmartDashboard.putString("DB/String 7", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 8", "" + rightenc.getDistance());
			}
			time.stop();
			time.reset();
			le.disable();
			re.disable();
			
			pidgyro.enable();
			pidgyro.setSetpoint(90 * (SmartDashboard.getBoolean("DB/Button 0", true) ? -1 : 1));
			arm.setSetpoint(4.0);
			time.start();
			while (time.get() < 3);
			time.stop();
			time.reset();
			pidgyro.disable();
			gyro.reset();
			leftenc.reset();
			rightenc.reset();
			
			le.enable();
			re.enable();
			le.setSetpoint(-string1);
			re.setSetpoint(string1);
			arm.setSetpoint(4.1);
			time.start();
			while (time.get() < 1.5)
			{
				SmartDashboard.putString("DB/String 7", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 8", "" + rightenc.getDistance());
			}
			time.stop();
			time.reset();
			le.disable();
			re.disable();
			
			pidgyro.enable();
			pidgyro.setSetpoint(90 * (SmartDashboard.getBoolean("DB/Button 0", true) ? 1 : -1));
			arm.setSetpoint(4.2);
			time.start();
			while (time.get() < 3);
			time.stop();
			time.reset();
			pidgyro.disable();
			gyro.reset();
			leftenc.reset();
			rightenc.reset();
			
			le.enable();
			re.enable();
			le.setSetpoint(-string2);
			re.setSetpoint(string2);
			arm.setSetpoint(4.3);
			time.start();
			while (time.get() < 3)
			{
				SmartDashboard.putString("DB/String 7", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 8", "" + rightenc.getDistance());
			}
			time.stop();
			time.reset();
			le.disable();
			re.disable();
			
			grab.set(DoubleSolenoid.Value.kReverse);
			punch.set(DoubleSolenoid.Value.kReverse);
			time.start();
			while (time.get() < 1);
			time.stop();
			time.reset();
			punch.set(DoubleSolenoid.Value.kForward);
			
			/*
			//moving forward x feet based on string0 value
			while (rightenc.getDistance() < string0 && leftenc.getDistance() < string0)
			{
				le.setSetpoint(-string0);
				re.setSetpoint(string0);
				SmartDashboard.putString("DB/String 8", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 9", "" + rightenc.getDistance());
			}
			le.disable();
			re.disable();
			leftenc.reset();
			rightenc.reset();
			gyro.reset();
			Timer.delay(1);
			
				
			pidgyro.enable();
			//turning right
			while (gyro.getAngle() < 90) 
			{   
				pidgyro.setSetpoint(90);
				SmartDashboard.putString("DB/String 4", "" + gyro.getAngle());
			}
			pidgyro.disable();
			Timer.delay(1);
			
			leftenc.reset();
			rightenc.reset();
			
			le.enable();
			re.enable();
			//moving forward again...
			while (rightenc.getDistance() < string1 && leftenc.getDistance() < string1)
			{
				le.setSetpoint(-string1);
				re.setSetpoint(string1);
				SmartDashboard.putString("DB/String 8", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 9", "" + rightenc.getDistance());
			}
			le.disable();
			re.disable();
			gyro.reset();
			leftenc.reset();
			rightenc.reset();
			Timer.delay(1);
			
			pidgyro.enable();
			//turning left
			while (gyro.getAngle() > -90)
			{
				pidgyro.setSetpoint(-90);
				SmartDashboard.putString("DB/String 4", "" + gyro.getAngle());
			}
			pidgyro.disable();
			gyro.reset();
			leftenc.reset();
			rightenc.reset();
			Timer.delay(1);
				
			le.enable();
			re.enable();
			//moving forward to goal and parking
			while (rightenc.getDistance() < string2 && leftenc.getDistance() < string2)
			{
				le.setSetpoint(-string2);
				re.setSetpoint(string2);
				SmartDashboard.putString("DB/String 8", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 9", "" + rightenc.getDistance());
			}
			le.disable();
			re.disable();
			Timer.delay(1);
			*/
		}
	}

	@Override
	public void teleopInit() 
	{
		leftenc.reset();
		rightenc.reset();
		gyro.reset();
		shift.set(DoubleSolenoid.Value.kForward);
		grab.set(DoubleSolenoid.Value.kReverse);
		punch.set(DoubleSolenoid.Value.kForward);
		hug.set(false);
		arm.enable();
		arm.setSetpoint(3.9);
		position = 4;
	}
	
	@Override
	public void teleopPeriodic() 
	{
		//put values of encoders, gyro, and arm
		SmartDashboard.putString("DB/String 5", "" + pot.getVoltage());
		SmartDashboard.putString("DB/String 6", "" + gyro.getAngle());
		SmartDashboard.putString("DB/String 7", "" + leftenc.getDistance());
		SmartDashboard.putString("DB/String 8", "" + rightenc.getDistance());
		
		drive.arcadeDrive(stick.getY(), stick.getX());
		
		if (stick.getRawButtonPressed(2))
			shift.set(DoubleSolenoid.Value.kReverse);
		else if (stick.getRawButtonReleased(2))
			shift.set(DoubleSolenoid.Value.kForward);
		
		if (stick.getRawButtonPressed(1))
		{
			if (grab.get().equals(DoubleSolenoid.Value.kReverse))
				grab.set(DoubleSolenoid.Value.kForward);
			else
			{
				grab.set(DoubleSolenoid.Value.kReverse);
				time.start();
				punch.set(DoubleSolenoid.Value.kReverse);
			}
		}
		if (time.get() > 1)
		{
			punch.set(DoubleSolenoid.Value.kForward);
			time.stop();
			time.reset();
		}	
		
		
		if (stick.getRawButtonPressed(7) && position != 1)
		{
			if (!arm.isEnabled())
				arm.enable();
			arm.setSetpoint(4.8);
			position = 1;
		}
		else if (stick.getRawButtonPressed(8) && position != 2)
		{
			if (!arm.isEnabled())
				arm.enable();
			arm.setSetpoint(4.6);
			position = 2;
		}
		else if (stick.getRawButtonPressed(9) && position != 3)
		{
			if (!arm.isEnabled())
				arm.enable();
			arm.setSetpoint(4.3);
			position = 3;
		}
		else if (stick.getRawButtonPressed(10) && position != 4)
		{
			if (!arm.isEnabled())
				arm.enable();
			arm.setSetpoint(3.9);
			position = 4;
		}
		else if (stick.getRawButtonPressed(11) && position != 5)
		{
			if (!arm.isEnabled())
				arm.enable();
			arm.setSetpoint(3.1);
			position = 5;
		}
		
		if (position == 1 && pot.getVoltage() >= 4.76)
			arm.disable();
		
		SmartDashboard.putString("DB/String 9", "" + pot.getVoltage());
		
	}

	@Override
	public void testPeriodic() 
	{
		
	}
	
	@Override
	public void disabledInit() 
	{
		le.disable();
		re.disable();
		pidgyro.disable();
	}
	
	@Override
	public void disabledPeriodic() 
	{
		
	}
	
}
