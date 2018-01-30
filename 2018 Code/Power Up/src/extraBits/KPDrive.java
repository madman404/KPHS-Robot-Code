package extraBits;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class KPDrive extends RobotDriveBase implements PIDOutput
{
	private static int instances = 0;
	private KPMotorGroup l, r;
	private AHRS ahrs;
	private Encoder lenc, renc;
	private PIDController lcontrol, rcontrol, tcontrol;
	private boolean m_reported = false;
	
	public KPDrive(int leftForward, int leftBackward, int rightForward, int rightBackward, int leftEncoderForward, int leftEncoderBackward, int rightEncoderForward, int rightEncoderBackward)
	{
	    l = new KPMotorGroup(new Talon(leftForward), new Talon(leftBackward));
	    r = new KPMotorGroup(new Talon(rightForward), new Talon(rightBackward));
	    addChild(l);
	    addChild(r);
	    ahrs = new AHRS(SPI.Port.kMXP);
		lenc = new Encoder(leftEncoderForward, leftEncoderBackward);
		renc = new Encoder(rightEncoderForward, rightEncoderBackward);
		lenc.setDistancePerPulse(1.0/512);
		renc.setDistancePerPulse(1.0/512);
		lcontrol = new PIDController(0.03, 0.0, 0.0, 0.0, lenc, l);
		lcontrol.setAbsoluteTolerance(1.0);
		rcontrol = new PIDController(0.03, 0.0, 0.0, 0.0, renc, r);
		rcontrol.setAbsoluteTolerance(1.0);
		tcontrol = new PIDController(0.03, 0.0, 0.0, 0.0, ahrs, this);
		tcontrol.setAbsoluteTolerance(1.0);
		tcontrol.setContinuous(true);
		tcontrol.setInputRange(-180.0, 180.0);
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
		
		l.set(leftSpeed * m_maxOutput);
		r.set(-rightSpeed * m_maxOutput);
		
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
	    
	    l.set(leftSpeed * m_maxOutput);
	    r.set(-rightSpeed * m_maxOutput);
	    
	    m_safetyHelper.feed();
	}
	
	public void forward(double distance)
	{
		lenc.reset();
		renc.reset();
		lcontrol.enable();
		rcontrol.enable();
		lcontrol.setSetpoint(distance);
		rcontrol.setSetpoint(-distance);
		while (!(lcontrol.onTarget() && rcontrol.onTarget()));
		lcontrol.disable();
		rcontrol.disable();
		l.stopMotor();
		r.stopMotor();
	}
	
	public void turn(double angle)
	{
		ahrs.reset();
		tcontrol.enable();
		tcontrol.setSetpoint(angle);
		while (!tcontrol.onTarget());
		tcontrol.disable();
		l.stopMotor();
		r.stopMotor();
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
		builder.addDoubleProperty("Left Motor Speed", l::get, l::set);
		builder.addDoubleProperty("Right Motor Speed", () -> -r.get(), x -> r.set(-x));
	}

	@Override
	public void stopMotor()
	{
		l.stopMotor();
		r.stopMotor();
	    m_safetyHelper.feed();
	}

	@Override
	public void pidWrite(double output)
	{
		l.set(output);
		r.set(output);
	}
}

