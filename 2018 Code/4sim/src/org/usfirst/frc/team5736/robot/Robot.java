
package org.usfirst.frc.team5736.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	private AHRS ahrs;
	private DifferentialDrive m_myRobot;
	private Victor arm;
	private Joystick m_leftStick, m_rightStick;

	@Override
	public void robotInit() {
		ahrs = new AHRS(SPI.Port.kMXP);
		arm = new Victor(2);
		m_myRobot = new DifferentialDrive(new Victor(0), new Victor(1));
		m_leftStick = new Joystick(0);
		m_rightStick = new Joystick(1);
	}

	@Override
	public void teleopPeriodic() {
		m_myRobot.tankDrive(m_leftStick.getY(), m_rightStick.getY());
		if (m_rightStick.getRawButton(3))
			arm.set(-1);
		else if (m_rightStick.getRawButton(4))
			arm.set(1);
		else
			arm.stopMotor();
		SmartDashboard.putString("DB/String 7", "" + ahrs.getAngle());
	}
}