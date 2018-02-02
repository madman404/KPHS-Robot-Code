/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot implements PIDOutput {
	private Victor l, r, a;
	private DifferentialDrive m_myRobot;
	private XboxController xbox;
	private AnalogPotentiometer pot;
	private Encoder lenc;
	private PIDController arm, drive;
	private DoubleSolenoid claw;
	private byte target = 3;

	@Override
	public void robotInit() {
		l = new Victor(0);
		r = new Victor(1);
		a = new Victor(2);
		m_myRobot = new DifferentialDrive(l, r);
		xbox = new XboxController(2);
		pot = new AnalogPotentiometer(0, 340);
		lenc = new Encoder(0, 1, true);
		arm = new PIDController(0.5, 0.0, 0.0, pot, a);
		drive = new PIDController(0.5, 0.0, 0.0, lenc, this);
		claw = new DoubleSolenoid(0, 1);
		
		lenc.setDistancePerPulse(18.85 / 512);
		arm.setInputRange(0, 340);
		drive.setAbsoluteTolerance(0.5);
		drive.setInputRange(-180, 180);
	}
	
	@Override
	public void autonomousInit()
	{
		drive.enable();
		drive.setSetpoint(SmartDashboard.getNumber("DB/Slider 0", 0.0));
	}
	
	@Override
	public void autonomousPeriodic()
	{
		
	}
	
	@Override
	public void teleopInit()
	{
		arm.enable();
		position(target);
	}

	@Override
	public void teleopPeriodic()
	{
		m_myRobot.arcadeDrive(xbox.getTriggerAxis(Hand.kRight) - xbox.getTriggerAxis(Hand.kLeft), xbox.getX(Hand.kLeft));
		
		if (xbox.getAButtonPressed() && target > 0)
			position(--target);
		else if (xbox.getYButtonPressed() && target < 4)
			position(++target);
		
		if (xbox.getXButtonPressed())
		{
			if (claw.get().equals(DoubleSolenoid.Value.kReverse))
				claw.set(DoubleSolenoid.Value.kForward);
			else
				claw.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
	@Override
	public void disabledInit()
	{
		drive.disable();
		arm.disable();
	}
	
	@Override
	public void pidWrite(double output)
	{
		l.set(output);
		r.set(-output);
	}
	
	public void position(byte target)
	{
		switch (target)
		{
		case 0:
			arm.setSetpoint(0.0);
		case 1:
			arm.setSetpoint(28.4);
		case 2:
			arm.setSetpoint(42.4);
		case 3:
			arm.setSetpoint(69.3);
		case 4:
			arm.setSetpoint(102.8);
		}
	}
}
