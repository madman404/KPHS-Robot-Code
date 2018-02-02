package org.usfirst.frc.team5736.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import extraBits.*;

public class Robot extends TimedRobot
{
	private KPDrive drivetrain;
	private XboxController xbox;
	private ClawArm pincer;
	
	@Override
	public void robotInit()
	{
		drivetrain = new KPDrive(0, 1, 2, 3, 0, 1, 2, 3, 0, 1);
		xbox = new XboxController(2);
		pincer = new ClawArm(4, 4, 5, 6, 7, 8, 2);
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
		pincer.setup();
	}
	
	@Override
	public void teleopPeriodic()
	{
		if (xbox.getAButton())
			drivetrain.highGear();
		else
			drivetrain.lowGear();
		
		drivetrain.XDrive(xbox.getTriggerAxis(Hand.kRight), xbox.getTriggerAxis(Hand.kLeft), xbox.getX(Hand.kLeft));
		
		if (xbox.getBButtonPressed())
			pincer.down();
		else if (xbox.getYButtonPressed())
			pincer.up();
		
		if (xbox.getXButtonPressed())
			pincer.toggle();
		
		pincer.update();
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


































