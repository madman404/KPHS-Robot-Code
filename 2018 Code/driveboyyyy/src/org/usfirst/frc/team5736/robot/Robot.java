package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import extraBits.*;

public class Robot extends TimedRobot
{
	private KPDrive robot;
	private Joystick leftStick, rightStick;
	private Talon arm;
	private DoubleSolenoid claw;
	
	@Override
	public void robotInit()
	{
		robot = new KPDrive(new Talon(0), new Talon(1), new Talon(2), new Talon(3));
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
		arm = new Talon(4);
		claw = new DoubleSolenoid(0, 1);
	}

	@Override
	public void autonomousInit()
	{

	}

	@Override
	public void autonomousPeriodic()
	{
		
	}

	@Override
	public void teleopInit()
	{
		
	}
	
	@Override
	public void teleopPeriodic()
	{
		robot.tankDrive(leftStick.getY(), rightStick.getY());
		
		
	}
}























