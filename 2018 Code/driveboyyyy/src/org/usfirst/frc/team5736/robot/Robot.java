package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import extraBits.*;

public class Robot extends TimedRobot
{
	private KPDrive robot;
	private XboxController xbox;
	private ClawArm pincer;
	
	@Override
	public void robotInit()
	{
		robot = new KPDrive(0, 1, 2, 3, 4, 5, 6, 7);
		xbox = new XboxController(2);
		pincer = new ClawArm(4, 0, 1, 2, 3);
	}
	
	private void safeCheck()
	{
		pincer.safeCheck();
	}

	@Override
	public void autonomousInit()
	{
		robot.setup();
	}

	@Override
	public void autonomousPeriodic()
	{
		//encoder and gyro framework below
		/**
		while(lenc.getDistance() < 5 && renc.getDistance() < 5)
		{
			robot.tankDrive(0.75, 0.75);
		}
		Timer.delay(0.75);
		
		while(gyro.getAngle() < 90)
		{
			robot.tankDrive(-0.75, 0.75);
		}
		Timer.delay(0.75);
		
		while(lenc.getDistance() < 3 && renc.getDistance() < 3)
		{
			robot.tankDrive(0.75, 0.75);
		}
		**/
	}
	
	@Override
	public void teleopInit()
	{
		pincer.setup();
		robot.setup();
	}
	
	@Override
	public void teleopPeriodic()
	{
		robot.XDrive(xbox.getTriggerAxis(Hand.kRight), xbox.getTriggerAxis(Hand.kLeft), xbox.getX(Hand.kLeft));
		
		if (xbox.getAButtonPressed())
			pincer.down();
		else if (xbox.getYButtonPressed())
			pincer.up();
		
		if (xbox.getXButtonPressed())
			pincer.toggle();
		
		safeCheck();
	}
}





























/**

*this goes in main for using two joysticks instead of xbox*

robot.tankDrive(left.getY(), right.getY());

if (right.getRawButton(3))
	pincer.down();
else if (right.getRawButton(5))
	pincer.up();

if (right.getRawButton(2))
	if (!held)
	{
		held = true;
		pincer.toggle();
	}
else if (held)
	held = false;
**/





















