package extraBits;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class KPDrive extends DifferentialDrive implements PIDOutput
{
	private static int instances = 0;
	private ADXRS450_Gyro gyro;
	private Talon l, r;
	private Encoder lenc, renc;
	private PIDController lcontrol, rcontrol, tcontrol;
	private DoubleSolenoid shift;
	private Timer time;
	
	public KPDrive(int left, int right, int leftEncoderForward, int leftEncoderBackward, int rightEncoderForward, int rightEncoderBackward, int shiftForward, int shiftBackward)
	{
	    super(new Talon(left), new Talon(right));
	    
	    gyro = new ADXRS450_Gyro();
		lenc = new Encoder(leftEncoderForward, leftEncoderBackward);
		renc = new Encoder(rightEncoderForward, rightEncoderBackward);
		lenc.setDistancePerPulse(18.85/120);
		renc.setDistancePerPulse(18.85/120);
		
		lcontrol = new PIDController(0.5, 0.0, 0.0, 0.0, lenc, l);
		lcontrol.setAbsoluteTolerance(0.5);
		lcontrol.setOutputRange(-.75, .75);
		rcontrol = new PIDController(0.5, 0.0, 0.0, 0.0, renc, r);
		rcontrol.setAbsoluteTolerance(0.5);
		rcontrol.setOutputRange(-.75, .75);
		tcontrol = new PIDController(0.5, 0.0, 0.0, 0.0, gyro, this);
		tcontrol.setAbsoluteTolerance(1.0);
		tcontrol.setContinuous(true);
		tcontrol.setInputRange(-180.0, 180.0);
		
		shift = new DoubleSolenoid(shiftForward, shiftBackward);
		
		time = new Timer();
		
	    instances++;
	    setName("KPDrive", instances);
	}
	
	public void forwardForTime(double distance, double seconds)
	{
		lenc.reset();
		renc.reset();
		lcontrol.enable();
		rcontrol.enable();
		time.start();
		lcontrol.setSetpoint(distance);
		rcontrol.setSetpoint(-distance);
		while (time.get() < seconds);
		time.stop();
		time.reset();
		lcontrol.disable();
		rcontrol.disable();
		stopMotor();
	}
	
	public void turnForTime(double angle, double seconds)
	{
		gyro.reset();
		tcontrol.enable();
		time.start();
		tcontrol.setSetpoint(angle);
		while (time.get() < seconds);
		time.stop();
		time.reset();
		tcontrol.disable();
		stopMotor();
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
	public void pidWrite(double output)
	{
		l.set(output);
		r.set(output);
	}
	
	public void lowGear()
	{
		if (!shift.get().equals(DoubleSolenoid.Value.kForward))
			shift.set(DoubleSolenoid.Value.kForward);
	}
	
	public void highGear()
	{
		if (!shift.get().equals(DoubleSolenoid.Value.kReverse))
			shift.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void shift()
	{
		if (shift.get().equals(DoubleSolenoid.Value.kReverse))
			shift.set(DoubleSolenoid.Value.kForward);
		else
			shift.set(DoubleSolenoid.Value.kReverse);
	}
}

