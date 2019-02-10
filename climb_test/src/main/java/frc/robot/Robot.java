package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Robot extends TimedRobot {
  XboxController joy;
  Climb climb;
  LimitSwitch fm, fe;
  LimitSwitch bm, be;

  @Override
  public void robotInit() {
    joy = new XboxController(0);
    climb = new Climb();
    // fm = new LimitSwitch(5);
    // fe = new LimitSwitch(3);
    // bm = new LimitSwitch(4);
    // be = new LimitSwitch(2);
  }

  @Override
  public void robotPeriodic() {
    climb.getLimitSwitches();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    climb.moveFront(0.3*deadzone(joy.getY(Hand.kRight)));
    climb.moveBack(0.3*deadzone(joy.getY(Hand.kLeft)));
    if (joy.getAButton()) climb.moveBoost();
    //climb.getLimitSwitches();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

  }

  public double deadzone(double input) {
    return Math.abs(input) < RobotMap.DEADZONE ? 0 : input;
  }
}
