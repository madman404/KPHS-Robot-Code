package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.*;
//import edu.wpi.first.wpilibj.command.Command;
import extraBits.*;

public class Robot extends TimedRobot
{
	//private KPDrive drivetrain = new KPDrive(0, 1, 0, 1, 2, 3, 0, 1);
	private Joystick stick = new Joystick(0);
	private XboxController xbox = new XboxController(2);
	private ClawArm arm = new ClawArm(2, 4, 5, 6, 7, 0);
	//private Command autoCommand;
	//private SendableChooser autoChooser = new SendableChooser();
	
	@Override
	public void robotInit()
	{
		
	}
	
	@Override
	public void robotPeriodic()
	{
		SmartDashboard.putString("DB/String 5", "" + arm.getVoltage());
	}

	@Override
	public void autonomousInit()
	{
		
	}

	@Override
	public void autonomousPeriodic()
	{
		
	}
	
	@Override
	public void teleopInit()
	{
		arm.setup();
	}
	
	@Override
	public void teleopPeriodic()
	{
		/*
		if (stick.getRawButtonPressed(2))
			drivetrain.highGear();
		else
			drivetrain.lowGear();
		
		drivetrain.arcadeDrive(-stick.getY(), stick.getX());
		*/
		if (arm.getManual())
		{
			arm.manualDrive(xbox.getY(Hand.kLeft));
			/*
			if (xbox.getAButtonPressed())
				arm.toggle();
			if (xbox.getXButtonPressed())
				arm.eject();
			*/
		}
		else
		{
			if (stick.getRawButtonPressed(7))
				arm.floorPosition();
			else if (stick.getRawButtonPressed(8))
				arm.portalPosition();
			else if (stick.getRawButtonPressed(9))
				arm.switchPosition();
			else if (stick.getRawButtonPressed(10))
				arm.homePosition();
			else if (stick.getRawButtonPressed(11))
				arm.dumpPosition();
			/*
			if (stick.getRawButtonPressed(1))
				arm.toggle();
			else if (stick.getRawButtonPressed(4))
				arm.eject();
			*/
		}
		
		if (xbox.getStartButtonPressed())
			arm.manualOverride(true);
		else if (xbox.getBackButtonPressed())
			arm.manualOverride(false);
		
		arm.update();
	}
	
	@Override
	public void disabledInit()
	{
		arm.stop();
	}
}









/*
                (`.
                 \ `.
                  )  `._..---._
\`.       __...---`         o  )
 \ `._,--'           ,    ___,'
  ) ,-._          \  )   _,-'
 /,'    ``--.._____\/--''

-shimrod

               ,
             .';
         .-'` .'
       ,`.-'-.`\
      ; /     '-'
      | \       ,-,
      \  '-.__   )_`'._
       '.     ```      ``'--._
      .-' ,                   `'-.
       '-'`-._           ((   o   )
        jgs   `'--....(`- ,__..--'
                       '-'`
                       
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
   
                                 ,-
                               ,'::|
                              /::::|
                            ,'::::o\                                      _..
         ____........-------,..::?88b                                  ,-' /
 _.--"""". . . .      .   .  .  .  ""`-._                           ,-' .;'
<. - :::::o......  ...   . . .. . .  .  .""--._                  ,-'. .;'
 `-._  ` `":`:`:`::||||:::::::::::::::::.:. .  ""--._ ,'|     ,-'.  .;'
     """_=--       //'doo.. ````:`:`::::::::::.:.:.:. .`-`._-'.   .;'
         ""--.__     P(       \               ` ``:`:``:::: .   .;'
                "\""--.:-.     `.                             .:/
                  \. /    `-._   `.""-----.,-..::(--"".\""`.  `:\
                   `P         `-._ \          `-:\          `. `:\
                                   ""            "            `-._)  -Seal
                    
*/


































