
package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends IterativeRobot {
	private DifferentialDrive m_myRobot;
	private SpeedControllerGroup m_leftSide, m_rightSide;
	private Joystick m_leftStick, m_rightStick;

	@Override
	public void robotInit() {
		m_leftSide = new SpeedControllerGroup(new Talon(0), new Talon(1));
		m_rightSide = new SpeedControllerGroup(new Talon(2), new Talon(3));
		m_myRobot = new DifferentialDrive(m_leftSide, m_rightSide);
		m_leftStick = new Joystick(0);
		m_rightStick = new Joystick(1);
	}

	@Override
	public void teleopPeriodic() {
		m_myRobot.tankDrive(m_leftStick.getY(), m_rightStick.getY());
	}
}
