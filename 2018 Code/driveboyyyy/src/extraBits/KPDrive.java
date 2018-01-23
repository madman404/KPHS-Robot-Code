package extraBits;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class KPDrive extends RobotDriveBase
{
	private static int instances = 0;
	private SpeedController lf, lb, rf, rb;
	private ADXRS450_Gyro gyro;
	private Encoder lenc, renc;
	private boolean m_reported = false;
	
	public KPDrive(int leftForward, int leftBackward, int rightForward, int rightBackward, int leftEncoderForward, int leftEncoderBackward, int rightEncoderForward, int rightEncoderBackward)
	{
		lf = new Talon(leftForward);
		lb = new Talon(leftBackward);
	    rf = new Talon(rightForward);
	    rb = new Talon(rightBackward);
	    addChild(lf);
	    addChild(lb);
	    addChild(rf);
	    addChild(rb);
	    this.gyro = new ADXRS450_Gyro();
		lenc = new Encoder(leftEncoderForward, leftEncoderBackward);
		renc = new Encoder(rightEncoderForward, rightEncoderBackward);
		lenc.setDistancePerPulse(1);
		renc.setDistancePerPulse(1);
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
		
		leftSpeed = applyDeadband(limit(leftSpeed), m_deadband);
		rightSpeed = applyDeadband(limit(rightSpeed), m_deadband);
		
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
	
	public void XDrive(double forward, double backward, double steering)
	{
		XDrive(forward, backward, steering, true);
	}
	
	public void XDrive(double forward, double backward, double steering, boolean squaredInputs)
	{
	    if (!m_reported)
	    {
	        HAL.report(tResourceType.kResourceType_RobotDrive, 2, tInstances.kRobotDrive_ArcadeStandard);
	        m_reported = true;
	    }
	    
	    double leftSpeed = applyDeadband(limit(forward - backward + steering), m_deadband),
	    	  rightSpeed = applyDeadband(limit(forward - backward - steering), m_deadband);
	    
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
	/**	
	public void forward(double distance)
	{
		while(lenc.getDistance() < distance && renc.getDistance() < distance)
			tankDrive(0.75, 0.75);
	}
	
	public void turnLeft(double angle)
	{
		while(gyro.getAngle() > -angle)
			tankDrive(-0.75, 0.75);
		gyro.reset();
	}
	
	public void turnRight(double angle)
	{
		while(gyro.getAngle() < angle)
			tankDrive(0.75, -0.75);
		gyro.reset();
	}
	**/
	public void setup()
	{
		gyro.reset();
		lenc.reset();
		renc.reset();
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

