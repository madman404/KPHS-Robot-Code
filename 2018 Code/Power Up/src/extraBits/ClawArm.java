package extraBits;

import edu.wpi.first.wpilibj.AnalogInput;
//import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Timer;

public class ClawArm implements PIDOutput
{
	private Talon m;
	//private DoubleSolenoid claw, ejector;
	private AnalogInput pot;
	private PIDController arm;
	//private Timer time;
	public double offset = 0.0;
	private final static double floorV = 4.2, safeV = 4.1, portalV = 4.0, switchV = 3.7, homeV = 3.3, dumpV = 2.5;
	private byte target = 0;
	private boolean isManual = false;
	
	public ClawArm(int armPWM, int openclawPCM, int closeclawPCM, int ejectorForwardPCM, int ejectorBackwardPCM, int potentiometerAI)
	{
		m = new Talon(armPWM);
		m.setInverted(true);
		//claw = new DoubleSolenoid(closeclawPCM, openclawPCM);
		//ejector = new DoubleSolenoid(ejectorForwardPCM, ejectorBackwardPCM);
		pot = new AnalogInput(potentiometerAI);
		arm = new PIDController(2.5, 0.0075, 0.0, pot, this);
		arm.setInputRange(0.0, 5.0);
		arm.setOutputRange(-0.6, 0.6);
	}
	/*
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
			open();
		else
			close();
	}
	*/
	public void setFloor()
	{
		if (target == 0)
			offset = pot.getVoltage() - floorV;
	}
	
	public void setTarget(double angle)
	{
		if (!arm.isEnabled())
			arm.enable();
		arm.setSetpoint(angle + offset);
	}
	
	public void floorPosition()
	{
		if (!isManual)
		{
			if (target == 0)
			{
				setTarget(portalV);
				target = 1;
			}
			else
			{
				setTarget(floorV);
				target = 0;
			}
		}
	}
	
	public void portalPosition()
	{
		if (!isManual && target != 2)
		{
			setTarget(portalV);
			target = 2;
		}
	}
	
	public void switchPosition()
	{
		if (!isManual && target != 3)
		{
			setTarget(switchV);
			target = 3;
		}
	}
	
	public void homePosition()
	{
		if (!isManual && target != 4)
		{
			setTarget(homeV);
			target = 4;
		}
	}
	
	public void dumpPosition()
	{
		if (!isManual && target != 5)
		{
			setTarget(dumpV);
			target = 5;
		}
	}
	
	public void stop()
	{
		arm.disable();
		m.stopMotor();
		target = 0;
	}
	
	public void setup()
	{
		//close();
		arm.enable();
		homePosition();
	}
	
	public void update()
	{
		/*
		if (time.get() >= 0.75)
		{
			time.stop();
			time.reset();
			ejector.set(DoubleSolenoid.Value.kForward);
		}
		*/
		if ((target == 0 && pot.getVoltage() >= safeV) || (target == 1 && pot.getVoltage() <= safeV))
			stop();
	}
	/*
	public void eject()
	{
		ejector.set(DoubleSolenoid.Value.kReverse);
		time.start();
	}
	*/
	public void manualOverride(boolean override)
	{
		m.stopMotor();
		arm.setEnabled(!override);
		isManual = override;
	}

	public void manualDrive(double speed)
	{
		if (isManual)
			m.set(speed * 0.6 - Math.cos((floorV - pot.getVoltage()) * 5 * Math.PI / 16) * 0.2);
	}
	
	public boolean getManual()
	{
		return isManual;
	}

	@Override
	public void pidWrite(double output)
	{
		m.set(output - Math.cos((floorV - pot.getVoltage()) * 5 * Math.PI / 16) * 0.2);
	}
	
	public double getVoltage()
	{
		return pot.getVoltage();
	}
}
