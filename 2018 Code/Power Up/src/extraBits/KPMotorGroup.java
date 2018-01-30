package extraBits;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class KPMotorGroup extends SendableBase implements SpeedController
{
	private boolean m_isInverted = false;
	private SpeedController forward, backward;
	private static int instances = 0;
	
	public KPMotorGroup(SpeedController forwardMotor, SpeedController backwardMotor)
	{
		forward = forwardMotor;
		backward = backwardMotor;
		addChild(forward);
		addChild(backward);
		instances++;
		setName("KPMotorGroup", instances);
	}

	@Override
	public void pidWrite(double output)
	{
		forward.pidWrite(output);
		backward.pidWrite(-output);
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
	    builder.setSmartDashboardType("Speed Controller");
	    builder.setSafeState(this::stopMotor);
	    builder.addDoubleProperty("Value", this::get, this::set);
	}

	@Override
	public void set(double speed)
	{
	    if (m_isInverted)
	    {
	    	forward.set(-speed);
	    	backward.set(speed);
	    }
	    else
	    {
	    	forward.set(speed);
	    	backward.set(-speed);
	    }
	}

	@Override
	public double get()
	{
		return forward.get();
	}

	@Override
	public void setInverted(boolean isInverted)
	{
		m_isInverted = isInverted;
	}

	@Override
	public boolean getInverted()
	{
		return m_isInverted;
	}

	@Override
	public void disable()
	{
		forward.disable();
		backward.disable();
	}

	@Override
	public void stopMotor()
	{
		forward.stopMotor();
		backward.stopMotor();
	}
}
