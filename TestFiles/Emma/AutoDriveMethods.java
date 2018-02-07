//NOT free-standing code: meant to be included in an autonomous drive train
//with all the necessary imports

public static void forward(double string)
{
    while (rightenc.getDistance() < string && leftenc.getDistance() < string)
		{
		    drive.tankDrive(.75, .75);
				SmartDashboard.putString("DB/String 8", "" + leftenc.getDistance());
				SmartDashboard.putString("DB/String 9", "" + rightenc.getDistance());
		}
    Timer.delay(3);
}

public static void backward(double string)
{
    leftenc.reset();
		rightenc.reset();
		while (rightenc.getDistance() < string && leftenc.getDistance() < string)
		{
		    drive.tankDrive(-.75, -.75);
			  SmartDashboard.putString("DB/String 8", "" + leftenc.getDistance());
			  SmartDashboard.putString("DB/String 9", "" + rightenc.getDistance());
		}
    Timer.delay(3);
}

public static void left()
{
    while(gyro.getAngle() > -90)
		{
		    drive.tankDrive(-.75, .75);
			  SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
		}
		Timer.delay(3);
}

public static void right()
{
    while(gyro.getAngle() < 0) 
		{
		    drive.tankDrive(.75, -.75);
			  SmartDashboard.putString("DB/String 7", "" + gyro.getAngle());
		}
    Timer.delay(3);
}
