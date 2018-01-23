package extraBits;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;

public class ClawArm
{
	private Talon arm;
	private DoubleSolenoid claw;
	private DigitalInput top, bottom;
	
	public ClawArm(int armPWM, int openclaw, int closeclaw, int topLimit, int bottomLimit)
	{
		arm = new Talon(armPWM);
		claw = new DoubleSolenoid(closeclaw, openclaw);
		top = new DigitalInput(topLimit);
		bottom = new DigitalInput(bottomLimit);
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
			open();
		else
			close();
	}
	
	public void up()
	{
		if (!top.get())
			arm.set(.5);
	}
	
	public void down()
	{
		if (!bottom.get())
			arm.set(-.5);
	}
	
	public void stop()
	{
		arm.stopMotor();
	}
	
	public void setup()
	{
		up();
		close();
	}
	
	public void safeCheck()
	{
		if (top.get() || bottom.get())
			stop();
	}
}
