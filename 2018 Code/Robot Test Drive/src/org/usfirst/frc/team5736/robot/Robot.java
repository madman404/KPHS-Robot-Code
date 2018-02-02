/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5736.robot;



import edu.wpi.first.wpilibj.TimedRobot; 
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
//import extrapart.Arm;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends TimedRobot {

	
	
	private  Joystick left, right;
	private DifferentialDrive drive; 
	private ADXRS450_Gyro gyro;
	private Encoder leftenc, rightenc;
	private double string0, string1, string2;
	private XboxController xbox;
	private Victor lm, rm;
	private boolean stop;
	private boolean swap;
	 

	@Override
	public void robotInit() {
		left = new Joystick(0);
		right = new Joystick(1);
		gyro = new ADXRS450_Gyro();
		drive = new DifferentialDrive(lm, rm);
		xbox = new XboxController(2);
		lm = new Victor(0);
		rm = new Victor(1);
		stop = true;
		swap = true;
		
		gyro.calibrate();
		leftenc.setDistancePerPulse(18.85/512);
		rightenc.setDistancePerPulse(18.85/512);
	}

	
	
	@Override
	public void autonomousInit() {
		
		gyro.reset();
		leftenc.reset();
		rightenc.reset();
		string0 = Double.parseDouble(SmartDashboard.getString("DB/String 0", "0"));
		string1 = Double.parseDouble(SmartDashboard.getString("DB/String 1", "0"));
		string2 = Double.parseDouble(SmartDashboard.getString("DB/String 2", "0"));
		
		
	}


	@Override
	public void autonomousPeriodic() {
		if(stop) {

			stop = false;
			//Use this for numerical data in the smart dashboard
			//SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
			//SmartDashboard.putString("DB/String 8", "" + leftenc.getDistance());
			//SmartDashboard.putString("DB/String 9", "" + rightenc.getDistance());
			
			//Forward drive based on Encoder Distance
			while (rightenc.getDistance() < string0 && leftenc.getDistance() < string0)
			{
				drive.tankDrive(.75, .75);
				SmartDashboard.putString("DB/String 8", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 9", "" + rightenc.getDistance());
			}
			Timer.delay(3);
	
		//turn left
			while(gyro.getAngle() > -90)
		    {
			drive.tankDrive(-.75, .75);
			SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
			}
		
		
			Timer.delay(3);
		//turn right
			while(gyro.getAngle() < 0) 
			{
			drive.tankDrive(.75, -.75);
			SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
			}
			Timer.delay(3);
		//move backwards
			leftenc.reset();
			rightenc.reset();
			while (rightenc.getDistance() < string0 && leftenc.getDistance() < string0)
			{
				drive.tankDrive(-.75, -.75);
				SmartDashboard.putString("DB/String 8", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 9", "" + rightenc.getDistance());
			}
			Timer.delay(3);
			
		}
	
	}
	@Override
	public void teleopInit() {

	}
	
	

	@Override
	public void teleopPeriodic() {
		if(swap) 
		{
		drive.tankDrive(left.getY(), right.getY());
		}
		else 
		{
		drive.arcadeDrive(xbox.getTriggerAxis(Hand.kRight) - xbox.getTriggerAxis(Hand.kLeft), xbox.getX(Hand.kLeft));
		}
		
	}


	@Override
	public void testPeriodic() {
	}
}
