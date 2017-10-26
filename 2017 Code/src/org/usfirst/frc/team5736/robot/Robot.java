/**
 * Final code for FRC 2017 Steamworks
 */

package org.usfirst.frc.team5736.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;

//import com.ni.vision.NIVision;
//import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.vision.USBCamera;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot
{
	ADXRS450_Gyro gyro;
	DoubleSolenoid shift, binfloor;
	Encoder lenc, renc;
	Joystick left, right, xbox;
	RobotDrive main, extra;
	Talon climb;
	
    boolean usingFront;
    VideoSink server;
    UsbCamera front, back; 

	boolean singleAuto;
	
    public void robotInit()
    {
    	main = new RobotDrive(0, 2);										// 4 Normal Motors (left pwm port 0, right pwm port 2)
    	extra = new RobotDrive(1, 3);										// 2 Inverse Motors (left port 1, left port 3)
    	climb = new Talon(4);												// Motor for climbing (pwm port 4)
    	gyro = new ADXRS450_Gyro();											// Gyro - Uses specific class for model
    	shift = new DoubleSolenoid(0, 1);									// Pneumatics for gear shifting (forward-pcm port 0, reverse-pcm port 1)
    	binfloor = new DoubleSolenoid(6, 7);								// Pneumatics for bin floor (forward-pcm port 6, reverse-pcm port 7)
    	lenc = new Encoder(0, 1);											// left encoder (channel a dio port 0, b dio port 1)
    	renc = new Encoder(2, 3);											// right encoder (channel a port 2, b port 3)
    	left = new Joystick(0);												// left joystick (driver station port 0)
    	right = new Joystick(1);											// right joystick (driver station port 1)
    	xbox = new Joystick(2);												// xbox (driver station port 2)
    	
    	//initialize camera
    	usingFront = true;
        front = CameraServer.getInstance().startAutomaticCapture(0);
        back = CameraServer.getInstance().startAutomaticCapture(1);
        server = CameraServer.getInstance().getServer();
    	
    	gyro.calibrate();		//self-explanatory
    	
    	//Insert starting values onto SmartDashboard
    	SmartDashboard.putString("DB/String 0", "      Left Encoder:");
    	SmartDashboard.putString("DB/String 1", "     Right Encoder:");
    	SmartDashboard.putString("DB/String 2", "        Gyro Angle:");
    	SmartDashboard.putString("DB/String 3", "Distance per Pulse:");
    	SmartDashboard.putString("DB/String 8", "0.00238095");	// 1 rotation per 420 impulses (low gear)
    }
    
    public void autonomousInit()
    {
    	lenc.reset();
    	renc.reset();
    	gyro.reset();
    	shift.set(DoubleSolenoid.Value.kForward);
    	lenc.setDistancePerPulse(Double.parseDouble(SmartDashboard.getString("DB/String 8", "0")));
    	renc.setDistancePerPulse(Double.parseDouble(SmartDashboard.getString("DB/String 8", "0")));
    	singleAuto = true;
    }
    
    public void autonomousPeriodic()
    {
    	// Red Boiler Side of Airship
    	if (singleAuto && !SmartDashboard.getBoolean("DB/Button 0", false) && !SmartDashboard.getBoolean("DB/Button 1", false) && SmartDashboard.getBoolean("DB/Button 2", false))
    	{
	    	while (lenc.getDistance() < 7.75 && renc.getDistance() < 7.75)	// 7.75 rotations
	    	{
	    		main.tankDrive(-.75, -.75);				// forward at 75% voltage
	    		extra.tankDrive(.75, .75);				// forward at 75% voltage
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    	}
	    	Timer.delay(.75);							// wait .75 seconds
	    	while (gyro.getAngle() > -42)				// turn 60 degrees
	    	{
	    		main.tankDrive(-.75, .75);
	    		extra.tankDrive(.75, -.75);
	    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	    	}
	    	lenc.reset(); renc.reset();
	    	Timer.delay(.75);
	    	while (lenc.getDistance() < 1.5 && renc.getDistance() < 1.5)
	    	{
	    		main.tankDrive(-.75, -.75);
	    		extra.tankDrive(.75, .75);
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	    	}
	    	singleAuto = false;
    	}
    	
    	// Red Non-Boiler Side of Airship
    	if (singleAuto && SmartDashboard.getBoolean("DB/Button 0", false) && !SmartDashboard.getBoolean("DB/Button 1", false) && !SmartDashboard.getBoolean("DB/Button 2", false))
    	{
	    	while (lenc.getDistance() < 7 && renc.getDistance() < 7)	// 7 rotations
	    	{
	    		main.tankDrive(-.75, -.75);				// forward at 75% voltage
	    		extra.tankDrive(.75, .75);				// forward at 75% voltage
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    	}
	    	Timer.delay(.75);							// wait .75 seconds
	    	while (gyro.getAngle() < 38)				// turn 60 degrees
	    	{
	    		main.tankDrive(.75, -.75);
	    		extra.tankDrive(-.75, .75);
	    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	    	}
	    	lenc.reset(); renc.reset();
	    	Timer.delay(.75);
	    	while (lenc.getDistance() < 1.5 && renc.getDistance() < 1.5)
	    	{
	    		main.tankDrive(-.75, -.75);
	    		extra.tankDrive(.75, .75);
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	    	}
	    	singleAuto = false;
    	}
    	
    	// In Front of Airship
    	if (singleAuto && !SmartDashboard.getBoolean("DB/Button 0", false) && SmartDashboard.getBoolean("DB/Button 1", false) && !SmartDashboard.getBoolean("DB/Button 2", false))
    	{
	    	while (lenc.getDistance() < 6.5 && renc.getDistance() < 6.5)
	    	{
	    		main.tankDrive(-.75, -.75);
	    		extra.tankDrive(.75, .75);
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    	}
	    	singleAuto = false;
    	}
    	
    	// Blue Boiler Side of Airship
    	if (singleAuto && !SmartDashboard.getBoolean("DB/Button 0", false) && SmartDashboard.getBoolean("DB/Button 1", false) && SmartDashboard.getBoolean("DB/Button 2", false))
    	{
	    	while (lenc.getDistance() < 7.25 && renc.getDistance() < 7.25)
	    	{
	    		main.tankDrive(-.75, -.75);
	    		extra.tankDrive(.75, .75);
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    	}
	    	Timer.delay(.75);
	    	while (gyro.getAngle() < 38)
	    	{
	    		main.tankDrive(.75, -.75);
	    		extra.tankDrive(-.75, .75);
	    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	    	}
	    	lenc.reset(); renc.reset();
	    	Timer.delay(.75);
	    	while (lenc.getDistance() < 1.5 && renc.getDistance() < 1.5)
	    	{
	    		main.tankDrive(-.75, -.75);
	    		extra.tankDrive(.75, .75);
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	    	}
	    	singleAuto = false;
    	}
    	
    	// Blue Non-Boiler Side of Airship
    	if (singleAuto && SmartDashboard.getBoolean("DB/Button 0", false) && SmartDashboard.getBoolean("DB/Button 1", false) && !SmartDashboard.getBoolean("DB/Button 2", false))
    	{
	    	while (lenc.getDistance() < 7 && renc.getDistance() < 7)
	    	{
	    		main.tankDrive(-.75, -.75);
	    		extra.tankDrive(.75, .75);
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    	}
	    	Timer.delay(.75);
	    	while (gyro.getAngle() > -42)
	    	{
	    		main.tankDrive(-.75, .75);
	    		extra.tankDrive(.75, -.75);
	    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	    	}
	    	lenc.reset(); renc.reset();
	    	Timer.delay(.75);
	    	while (lenc.getDistance() < 1.5 && renc.getDistance() < 1.5)
	    	{
	    		main.tankDrive(-.75, -.75);
	    		extra.tankDrive(.75, .75);
	    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
	    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
	    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
	    	}
	    	singleAuto = false;
    	}
    }
    public void teleopInit()
    {
    	lenc.reset();
    	renc.reset();
    	gyro.reset();
    	lenc.setDistancePerPulse(Double.parseDouble(SmartDashboard.getString("DB/String 8", "0")));
    	renc.setDistancePerPulse(Double.parseDouble(SmartDashboard.getString("DB/String 8", "0")));
    	shift.set(DoubleSolenoid.Value.kForward);
    	binfloor.set(DoubleSolenoid.Value.kForward);
    }
    
    public void teleopPeriodic()
    {
		while (isOperatorControl() && isEnabled())
		{
			// Driving
			if (right.getRawButton(2) && !SmartDashboard.getBoolean("DB/Button 3", false))
			{
		        main.tankDrive(right.getRawAxis(1), left.getRawAxis(1));
		        extra.tankDrive(-right.getRawAxis(1), -left.getRawAxis(1));
			}
			else
			{
				if (!SmartDashboard.getBoolean("DB/Button 3", false))
				{
					main.tankDrive(-left.getRawAxis(1), -right.getRawAxis(1));
					extra.tankDrive(left.getRawAxis(1), right.getRawAxis(1));
				}
				else
				{
					main.tankDrive(xbox.getRawAxis(3) - xbox.getRawAxis(2) + xbox.getRawAxis(0), xbox.getRawAxis(3) - xbox.getRawAxis(2) - xbox.getRawAxis(0));
					extra.tankDrive(xbox.getRawAxis(2) - xbox.getRawAxis(3) - xbox.getRawAxis(0), xbox.getRawAxis(2) - xbox.getRawAxis(3) + xbox.getRawAxis(0));
				}
			}
			
			// Gear Shifting
	        if (right.getRawButton(1) || xbox.getRawButton(1))
	        	shift.set(DoubleSolenoid.Value.kReverse);
	        else
	        	shift.set(DoubleSolenoid.Value.kForward);
	        
	        // Climbing
	        if ((right.getRawButton(7) && right.getRawButton(8)) || (xbox.getRawButton(5) && xbox.getRawButton(6)))
	        	climb.set(-1);
	        /*
	        else if (right.getRawButton(9) && right.getRawButton(10))
	        	climb.set(1);
	        */
	        else
	        	climb.set(0);
	        
	        // 180 Degree Turn
	        if (left.getRawButton(2) || xbox.getRawButton(3))
	        {
	        	double turnAngle = gyro.getAngle() - 165;
		        while (gyro.getAngle() > turnAngle)
		        {
		        	main.tankDrive(-1, 1);
		        	extra.tankDrive(1, -1);
		        	SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
		        }
	        }
	        
	        // 60 Degree Sweep
	        if (left.getRawButton(3) || xbox.getRawButton(10))
	        {
	        	double turnAngle = gyro.getAngle() - 45;
	        	while (gyro.getAngle() > turnAngle)
		        {
		        	main.tankDrive(-1, 1);
		        	extra.tankDrive(1, -1);
		        	SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
		        }
	        	Timer.delay(.2);
	        	double original = gyro.getAngle() + 45;
	        	while (gyro.getAngle() < original)
		        {
		        	main.tankDrive(1, -1);
		        	extra.tankDrive(-1, 1);
		        	SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
		        }
	        }
	        
	        // Bin Floor Movement
	        if (right.getRawButton(3) || xbox.getRawButton(4))
	        {
	        	binfloor.set(DoubleSolenoid.Value.kReverse);
	        	Timer.delay(.25);
	        	while (right.getRawButton(3) || xbox.getRawButton(4))			// check if we should flutter when button 3 is being held
	        	{
	        		if (right.getRawButton(5) || xbox.getRawButton(2))			// flutter the bin floor
	        		{
	        			binfloor.set(DoubleSolenoid.Value.kForward);
		        		if (right.getRawButton(5) || xbox.getRawButton(2))
		        		{
		        			Timer.delay(.1);
		        			if (right.getRawButton(5) || xbox.getRawButton(2))
		        			{
		        				binfloor.set(DoubleSolenoid.Value.kReverse);
		        				if (right.getRawButton(5) || xbox.getRawButton(2))
		        					Timer.delay(.1);
		        			}
		        		}
	        		}
	        		else
	        			binfloor.set(DoubleSolenoid.Value.kReverse);
	        	}
	        	binfloor.set(DoubleSolenoid.Value.kForward);
	        }
	        
	        // Smart Dashboard Output
    		SmartDashboard.putString("DB/String 5", "" + lenc.getDistance());
    		SmartDashboard.putString("DB/String 6", "" + renc.getDistance());
    		SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
		}
    }
}
