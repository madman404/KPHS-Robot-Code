package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;

public class Robot extends IterativeRobot
{
	private DifferentialDrive robot;
	private Joystick left, right;
	private ADXRS450_Gyro gyro;
	private Encoder lenc, renc;
	private double s0, s1, s2;
	
	@Override
	public void robotInit()
	{
		robot = new DifferentialDrive(new Victor(0), new Victor(1));
		left = new Joystick(0);
		right = new Joystick(1);
		gyro = new ADXRS450_Gyro();
		lenc = new Encoder(0, 1);
		renc = new Encoder(2, 3);
		s0 = s1 = s2 = 0;
		
		gyro.calibrate();
		lenc.setDistancePerPulse(1/512);
		renc.setDistancePerPulse(1/512);
	}
	
	@Override
	public void autonomousInit()
	{
		gyro.reset();
		lenc.reset();
		renc.reset();
		s0 = Double.parseDouble(SmartDashboard.getString("DB/String 0", "0"));
		s1 = Double.parseDouble(SmartDashboard.getString("DB/String 1", "0"));
		s2 = Double.parseDouble(SmartDashboard.getString("DB/String 2", "0"));
	}
	
	@Override
	public void autonomousPeriodic()
	{
		while (lenc.getDistance() < s0 && renc.getDistance() < s0)
			robot.tankDrive(.75, .75);
		while (gyro.getAngle() < s1 && gyro.getAngle() > -s1)
			robot.tankDrive(-.75, .75);
		lenc.reset();
		renc.reset();
		while (lenc.getDistance() < s2 && renc.getDistance() < s2)
			robot.tankDrive(.75, .75);
		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	}
	
	@Override
	public void teleopInit()
	{
		gyro.reset();
		lenc.reset();
		renc.reset();
	}
	
	@Override
	public void teleopPeriodic()
	{
		robot.tankDrive(left.getY(), right.getY());
		
		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
		SmartDashboard.putString("DB/String 8", "" + lenc.getDistance());
		SmartDashboard.putString("DB/String 9", "" + renc.getDistance());
	}
}