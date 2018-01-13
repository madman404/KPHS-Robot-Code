package extraBits;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class KPDrive extends RobotDriveBase
{
	private static int instances = 0;
	private SpeedController lf, lb, rf, rb;
	private boolean m_reported = false;
	
	public KPDrive(SpeedController leftForward, SpeedController leftBackward, SpeedController rightForward, SpeedController rightBackward)
	{
		lf = leftForward;
		lb = leftBackward;
	    rf = rightForward;
	    rb = rightBackward;
	    addChild(lf);
	    addChild(lb);
	    addChild(rf);
	    addChild(rb);
	    instances++;
	    setName("KPDrive", instances);
	}
	
	public void tankDrive(double leftSpeed, double rightSpeed)
	{
		tankDrive(leftSpeed, rightSpeed, true);
	}
	
	public void tankDrive(double leftSpeed, double rightSpeed, boolean squaredInputs)
	{
		if (!m_reported)
		{
			HAL.report(tResourceType.kResourceType_RobotDrive, 2, tInstances.kRobotDrive_Tank);
			m_reported = true;
		}
		
		leftSpeed = limit(leftSpeed);
		leftSpeed = applyDeadband(leftSpeed, m_deadband);
		
		rightSpeed = limit(rightSpeed);
		rightSpeed = applyDeadband(rightSpeed, m_deadband);
		
		if (squaredInputs)
		{
			leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
			rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
		}
		
		lf.set(leftSpeed * m_maxOutput);
		lb.set(-leftSpeed * m_maxOutput);
		rf.set(-rightSpeed * m_maxOutput);
		rb.set(rightSpeed * m_maxOutput);
		
		m_safetyHelper.feed();
	}

	@Override
	public String getDescription()
	{
		return "KPDrive";
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.setSmartDashboardType("KPDrive");
		builder.addDoubleProperty("Left Motor Speed", lf::get, lf::set);
		builder.addDoubleProperty("Right Motor Speed", () -> -rf.get(), x -> rf.set(-x));
	}

	@Override
	public void stopMotor()
	{
		lf.stopMotor();
		lb.stopMotor();
		rf.stopMotor();
		rb.stopMotor();
	    m_safetyHelper.feed();
	}
}

/**
   ,|
  / ;
 /  \
: ,'(
|( `.\
: \  `\       \.
 \ `.         | `.
  \  `-._     ;   \
   \     ``-.'.. _ `._
    `. `-.            ```-...__
   .'`.        --..          ``-..____
 ,'.-'`,_-._            ((((   <o.   ,'
      `' `-.)``-._-...__````  ____.-'
  SSt    ,'    _,'.--,---------'
     _.-' _..-'   ),'
    ``--''        `
    
                (`.
                 \ `.
                  )  `._..---._
\`.       __...---`         o  )
 \ `._,--'           ,    ___,'
  ) ,-._          \  )   _,-'
 /,'    ``--.._____\/--''

-shimrod

WHYYYYYYYYY?????????????



**/