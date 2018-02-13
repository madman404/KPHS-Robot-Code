
package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot implements PIDOutput
{
	private Victor l = new Victor(0),
				  r = new Victor(1);
	private DifferentialDrive robot = new DifferentialDrive(l, r);
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	private Encoder lenc = new Encoder(0, 1),
					renc = new Encoder(2, 3);
	private PIDController sync = new PIDController(0.025, 0.0, 0.0, gyro, this);
	
	private Joystick stick = new Joystick(0);
	
	private double distAuto = 0.0,
				   angle = 20.0,
				   limit = 5.0;
	
	private boolean stop;
	
	private Timer time = new Timer();
	
	private DoubleSolenoid shift = new DoubleSolenoid(0, 1);
	
	@Override
	public void robotInit()
	{
		l.setSafetyEnabled(false);
		r.setSafetyEnabled(false);
		robot.setSafetyEnabled(false);
		
		gyro.calibrate();
		lenc.setDistancePerPulse(18.85/117);
		renc.setDistancePerPulse(18.85/117);
		
		sync.setInputRange(-180.0, 180.0);
		sync.setOutputRange(-0.3, 0.3);
		sync.setContinuous();
	}
	
	@Override
	public void autonomousInit()
	{
		shift.set(DoubleSolenoid.Value.kForward);
		
		angle = Double.parseDouble(SmartDashboard.getString("DB/String 0", "20.0"));
		distAuto = SmartDashboard.getNumber("DB/Slider 0", 48.0);
		double p = SmartDashboard.getNumber("DB/Slider 1", 0.025),
			   i = SmartDashboard.getNumber("DB/Slider 2", 0.0),
			   d = SmartDashboard.getNumber("DB/Slider 3", 0.0);
		sync.setPID(p, i, d);
		
		gyro.reset();
		lenc.reset();
		renc.reset();
		
		stop = true;
	}
	
	@Override
	public void autonomousPeriodic()
	{
		if (stop)
		{
			stop = false;
			
			sync.enable();
			time.start();
			double dist1, dist2;
			
			sync.setSetpoint(SmartDashboard.getBoolean("DB/Button 0", false) ? -angle : angle);
			while (((dist1 = lenc.getDistance()) - (dist2 = renc.getDistance())) * 3 / 2 < distAuto && time.get() < limit);
			
			lenc.reset();
			renc.reset();
			dist1 *= -2;
			dist2 *= -2;
			
			sync.setSetpoint(0.0);
			while((dist2 > lenc.getDistance() || dist1 < renc.getDistance()) && time.get() < limit);
			
			time.stop();
			time.reset();
			sync.disable();
			l.stopMotor();
			r.stopMotor();
			gyro.reset();
			lenc.reset();
			renc.reset();
		}
	}
	
	@Override
	public void teleopInit()
	{
		shift.set(DoubleSolenoid.Value.kForward);
	}
	
	@Override
	public void teleopPeriodic()
	{
		robot.arcadeDrive(-stick.getY(), stick.getX());
		if (stick.getRawButtonPressed(2))
			shift.set(DoubleSolenoid.Value.kReverse);
		else if (stick.getRawButtonReleased(2))
			shift.set(DoubleSolenoid.Value.kForward);
	}

	@Override
	public void pidWrite(double output)
	{
		l.set(output + 0.6);
		r.set(output - 0.6);
		SmartDashboard.putString("DB/String 4", "" + output);
	}
}
