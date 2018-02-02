package extraBits;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ClawArm
{
	private Talon arm;
	private DoubleSolenoid claw, ejector;
	private AnalogPotentiometer ap;
	private PIDController pc;
	private DigitalInput trigger;
	private Timer time;
	public double offset = 0.0;
	private byte target = 3;
	private boolean triggerActivated = false;
	
	public ClawArm(int armPWM, int openclawPCM, int closeclawPCM, int ejectorForwardPCM, int ejectorBackwardPCM, int triggerDIO, int potentiometerAI)
	{
		arm = new Talon(armPWM);
		claw = new DoubleSolenoid(closeclawPCM, openclawPCM);
		ejector = new DoubleSolenoid(ejectorForwardPCM, ejectorBackwardPCM);
		ap = new AnalogPotentiometer(potentiometerAI, 340, 10);
		
		double p = SmartDashboard.getNumber("DB/Slider 0", 0.5),
			   i = SmartDashboard.getNumber("DB/Slider 1", 0.0),
			   d = SmartDashboard.getNumber("DB/Slider 2", 0.0),
			   f = SmartDashboard.getNumber("DB/Slider 3", 0.0);
		
		pc = new PIDController(p, i, d, f, ap, arm);
		
		trigger = new DigitalInput(triggerDIO);
	}
	
	public void open()
	{
		claw.set(DoubleSolenoid.Value.kForward);
	}
	
	public void close()
	{
		claw.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void toggle()
	{
		if (claw.get().equals(DoubleSolenoid.Value.kReverse))
		{
			if (trigger.get())
				eject();
			else
				open();
		}
		else
			close();
	}
	
	public void setFloor()
	{
		if (target == 0)
			offset = ap.get();
	}
	
	public void setTarget(double angle)
	{
		if (!pc.isEnabled())
			pc.enable();
		pc.setSetpoint(angle + offset);
	}
	
	public void floorPosition()
	{
		setTarget(0.0);
		target = 0;
	}
	
	public void portalPosition()
	{
		setTarget(28.4);
		target = 1;
	}
	
	public void switchPosition()
	{
		setTarget(42.4);
		target = 2;
	}
	
	public void homePosition()
	{
		setTarget(69.3);
		target = 3;
	}
	
	public void dumpPosition()
	{
		setTarget(102.8);
		target = 4;
	}
	
	public void position(byte pos)
	{
		switch (pos)
		{
		case 0:
			floorPosition();
		case 1:
			portalPosition();
		case 2:
			switchPosition();
		case 3:
			homePosition();
		case 4:
			dumpPosition();
		}
	}
	
	public void down()
	{
		if (target > 0)
		{
			target--;
			position(target);
		}
	}
	
	public void up()
	{
		if (target < 4)
		{
			target++;
			position(target);
		}
	}
	
	public void stop()
	{
		pc.disable();
		arm.stopMotor();
	}
	
	public void setup()
	{
		close();
		pc.enable();
		homePosition();
	}
	
	public void update()
	{
		if (!triggerActivated && trigger.get() && ejector.get().equals(DoubleSolenoid.Value.kReverse))
		{
			triggerActivated = true;
			close();
		}
		if (time.get() >= 0.75)
		{
			time.stop();
			ejector.set(DoubleSolenoid.Value.kReverse);
			triggerActivated = false;
		}
	}
	
	public void eject()
	{
		open();
		ejector.set(DoubleSolenoid.Value.kForward);
		time.start();
	}
}
