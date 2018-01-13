package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot
{
	private DifferentialDrive robot;
	private Joystick left, right;
	private ADXRS450_Gyro gyro;
	private Timer time;
	private double s0, s1, s2;
	
	@Override
	public void robotInit()
	{
		robot = new DifferentialDrive(new Talon(0), new Talon(1));
		left = new Joystick(0);
		right = new Joystick(1);
		gyro = new ADXRS450_Gyro();
		time = new Timer();
		s0 = s1 = s2 = 0;
		
		gyro.calibrate();
	}
	
	@Override
	public void autonomousInit()
	{
		gyro.reset();
		s0 = Double.parseDouble(SmartDashboard.getString("DB/String 0", "0"));
		s1 = Double.parseDouble(SmartDashboard.getString("DB/String 1", "0"));
		s2 = Double.parseDouble(SmartDashboard.getString("DB/String 2", "0"));
	}
	
	@Override
	public void autonomousPeriodic()
	{
		time.start();
		while (time.get() < s0)
			robot.tankDrive(.75, .75);
		time.stop();
		time.reset();
		while (gyro.getAngle() < s1)
			robot.tankDrive(-.75, .75);
		time.start();
		while (time.get() < s2)
			robot.tankDrive(-.75, -.75);
		time.stop();
		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	}
	
	@Override
	public void teleopPeriodic()
	{
		robot.tankDrive(left.getY(), right.getY());
	}
}