package extraBits;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;

public class ForkArm
{
	public final double GROUND_POS, LOAD_POS, UNLOAD_POS, TUCK_POS, DEADBAND;
	public double curPos;
	//private boolean turn = false;
	
	private Talon arm;
	private AnalogPotentiometer position;
	private DoubleSolenoid ejector;
	
	public ForkArm(int armPWM, int positionAI, int forwardDIO, int reverseDIO)
	{
		this(armPWM, positionAI, forwardDIO, reverseDIO, 0.0, 30.0, 45.0, 75.0, 2.0);
	}
	
	public ForkArm(int armPWM, int positionAI, int forwardDIO, int reverseDIO, double groundpos, double loadpos, double unloadpos, double tuckpos, double deadband)
	{
		arm = new Talon(armPWM);
		position = new AnalogPotentiometer(positionAI, 180);
		ejector = new DoubleSolenoid(forwardDIO, reverseDIO);
		GROUND_POS = groundpos;
		LOAD_POS = loadpos;
		UNLOAD_POS = unloadpos;
		curPos = TUCK_POS = tuckpos;
		DEADBAND = deadband;
	}
	
	public void setup()
	{
		retract();
		tuckPosition();
	}
	
	/*public void stop()
	{
		arm.stopMotor();
		turn = false;
	}*/
	
	public void moveUp()
	{
		switch ((int)curPos)
		{
			case 1: curPos = GROUND_POS;
				turnToAngle(LOAD_POS);
			case 2: curPos = LOAD_POS;
				turnToAngle(UNLOAD_POS);
			case 3: curPos = UNLOAD_POS;
				turnToAngle(TUCK_POS);
		}
	}
		
	public void moveDown()
	{
		switch ((int)curPos)
		{
			case 1: curPos = LOAD_POS;
				turnToAngle(GROUND_POS);
			case 2: curPos = UNLOAD_POS;
				turnToAngle(LOAD_POS);
			case 3: curPos = TUCK_POS;
				turnToAngle(LOAD_POS);
		}
	}
	
	public void turnToAngle(double angle)
	{
		curPos = angle;
		
		while(!(position.get() >= angle - DEADBAND && position.get() <= curPos + DEADBAND))
		{
			if (position.get() < angle)
				arm.set(1);
			else
				arm.set(-1);
		}
	}
	
	/*public void turnToAngle()
	{
		if (turn)
		{
			if (position.get() >= curPos - DEADBAND && position.get() <= curPos + DEADBAND)
				stop();
			else if (position.get() < curPos)
				arm.set(1);
			else
				arm.set(-1);
		}
	}
	
	public void setAngle(double angle)
	{
		curPos = angle;
		turn = true;
	}*/
	
	public void groundPosition()
	{
		turnToAngle(GROUND_POS);
	}
	
	public void loadPosition()
	{
		turnToAngle(LOAD_POS);
	}
	
	public void unloadPosition()
	{
		turnToAngle(UNLOAD_POS);
	}
	
	public void tuckPosition()
	{
		turnToAngle(TUCK_POS);
	}
	
	public void eject()
	{
		ejector.set(DoubleSolenoid.Value.kForward);
	}
	
	public void retract()
	{
		ejector.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void unload()
	{
		unloadPosition();
		eject();
	}
}
