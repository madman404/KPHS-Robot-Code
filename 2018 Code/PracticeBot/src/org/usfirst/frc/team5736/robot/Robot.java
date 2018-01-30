package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot
{
	private DifferentialDrive robot;
	private Joystick left, right;
	private Victor lMotor, rMotor, arm;
	private SpeedControllerGroup sync;
	private ADXRS450_Gyro gyro;
	private Encoder lenc, renc;
	private PIDController controlLenc, controlRenc, controlGyro;
	private DoubleSolenoid claw;
	private double s0, s1;
	private boolean once;
	
	@Override
	public void robotInit()
	{
		lMotor = new Victor(0);
		rMotor = new Victor(1);
		robot = new DifferentialDrive(lMotor, rMotor);
		sync = new SpeedControllerGroup(lMotor, rMotor);
		left = new Joystick(0);
		right = new Joystick(1);
		arm = new Victor(2);
		gyro = new ADXRS450_Gyro();
		controlGyro = new PIDController(0.5, 0.0, 0.0, gyro, sync);
		lenc = new Encoder(0, 1, true);
		renc = new Encoder(2, 3, true);
		controlLenc = new PIDController(0.5, 0.0, 0.0, lenc, lMotor);
		controlRenc = new PIDController(0.5, 0.0, 0.0, renc, rMotor);
		claw = new DoubleSolenoid(0, 1);
		s0 = s1 = 0;
		
		gyro.calibrate();
		lMotor.setSafetyEnabled(false);
		rMotor.setSafetyEnabled(false);
		lenc.setDistancePerPulse(18.85/512);
		renc.setDistancePerPulse(18.85/512);
		controlLenc.setAbsoluteTolerance(1);
		controlRenc.setAbsoluteTolerance(1);
		controlGyro.setAbsoluteTolerance(2);
		controlLenc.setInputRange(-180.0, 180.0);
		controlRenc.setInputRange(-180.0, 180.0);
		controlGyro.setInputRange(-180.0, 180.0);
		controlLenc.setOutputRange(-.5, .5);
		controlRenc.setOutputRange(-.5, .5);
		controlGyro.setOutputRange(-.5, .5);
		controlGyro.setContinuous();
		
		
		SmartDashboard.putData("lencPID", controlLenc);
		SmartDashboard.putData("rencPID", controlRenc);
		SmartDashboard.putData("gyroPID", controlGyro);
	}
	
	@Override
	public void autonomousInit()
	{
		gyro.reset();
		lenc.reset();
		renc.reset();
		claw.set(DoubleSolenoid.Value.kReverse);
		s0 = Double.parseDouble(SmartDashboard.getString("DB/String 0", "0"));
		s1 = Double.parseDouble(SmartDashboard.getString("DB/String 1", "0"));
		once = true;
	}
	
	@Override
	public void autonomousPeriodic()
	{
		if (once)
		{
			once = false;
			if (SmartDashboard.getBoolean("DB/Button 0", false))
			{
				controlGyro.enable();
				controlGyro.setSetpoint(s1);
				while (gyro.getAngle() < s1 && gyro.getAngle() > -s1);
				Timer.delay(3);
				controlGyro.disable();
				SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
			}
			else
			{
				controlLenc.enable();
				controlRenc.enable();
				controlLenc.setSetpoint(s0);
				controlRenc.setSetpoint(-s0);
				while (!(controlLenc.onTarget() && controlRenc.onTarget()));
				Timer.delay(3);
				controlLenc.disable();
				controlRenc.disable();
				SmartDashboard.putString("DB/String 8", "" + lenc.getDistance());
				SmartDashboard.putString("DB/String 9", "" + renc.getDistance());
			}
		}
	}
	
	@Override
	public void teleopInit()
	{
		gyro.reset();
		lenc.reset();
		renc.reset();
		claw.set(DoubleSolenoid.Value.kReverse);
	}
	
	@Override
	public void teleopPeriodic()
	{
		robot.tankDrive(-left.getY(), -right.getY());
		
		if(right.getRawButton(3))
			arm.set(-.4);
		else if(right.getRawButton(4))
			arm.set(.4);
		else
			arm.set(0);
		
		if (right.getRawButtonPressed(1))
		{
			if (claw.get().equals(DoubleSolenoid.Value.kReverse))
				claw.set(DoubleSolenoid.Value.kForward);
			else
				claw.set(DoubleSolenoid.Value.kReverse);
		}
		
		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
		SmartDashboard.putString("DB/String 8", "" + lenc.getDistance());
		SmartDashboard.putString("DB/String 9", "" + renc.getDistance());
	}
	
	@Override
	public void disabledPeriodic()
	{
		if (left.getRawButtonPressed(7))
		{
			double p = SmartDashboard.getNumber("DB/Slider 0", 0.0),
				   i = SmartDashboard.getNumber("DB/Slider 1", 0.0),
				   d = SmartDashboard.getNumber("DB/Slider 2", 0.0),
				   f = SmartDashboard.getNumber("DB/Slider 3", 0.0);
			controlLenc.setP(p);
			controlLenc.setI(i);
			controlLenc.setD(d);
			controlLenc.setF(f);
			controlRenc.setP(p);
			controlRenc.setI(i);
			controlRenc.setD(d);
			controlRenc.setF(f);
		}
		if (left.getRawButtonPressed(8))
		{
			controlGyro.setP(SmartDashboard.getNumber("DB/Slider 0", 0.0));
			controlGyro.setI(SmartDashboard.getNumber("DB/Slider 1", 0.0));
			controlGyro.setD(SmartDashboard.getNumber("DB/Slider 2", 0.0));
			controlGyro.setF(SmartDashboard.getNumber("DB/Slider 3", 0.0));
		}
	}
	
	@Override
	public void testPeriodic()
	{
		
	}
}