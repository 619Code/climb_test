package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.RobotMap.Manipulators;
import frc.robot.LimitSwitch;

public class Climb extends CommandGroup{
    //ratio is 1:16
    WPI_TalonSRX _front, _back;
    LimitSwitch _frontMiddle,_frontEnd, _backEnd, _backMiddle;
    Solenoid _zoop;
    DoubleSolenoid _boost;
    private final boolean ZOOP_DIR = true;
    private final boolean BOOST_DIR = true;
    boolean dir;
    public Climb(){
        _front = new WPI_TalonSRX(RobotMap.FRONT_CLIMB);
        _back = new WPI_TalonSRX(RobotMap.BACK_CLIMB);
        _zoop = new Solenoid(RobotMap.PCM_CAN_ID, RobotMap.ZOOP_CHANNEL);
        _boost = new DoubleSolenoid(RobotMap.PCM_CAN_ID, RobotMap.BOOST_CHANNEL[0], RobotMap.BOOST_CHANNEL[1]);
        _frontMiddle = new LimitSwitch(RobotMap.FRONT_MIDDLE_CLIMB_SWITCH);
        _frontEnd = new LimitSwitch(RobotMap.FRONT_END_CLIMB_SWITCH);
        _backMiddle = new LimitSwitch(RobotMap.BACK_MIDDLE_CLIMB_SWITCH);
        _backEnd = new LimitSwitch(RobotMap.BACK_END_CLIMB_SWITCH);
        dir = false;
    }

    public void moveBoost(){
        dir = !dir;
        System.out.println(dir);
        if(dir) _boost.set(Value.kForward);
        if(!dir) _boost.set(Value.kReverse);
        Timer.delay(0.3);
    }

    public void moveFront(double speed){
        _front.set(ControlMode.PercentOutput, speed);
    }

    public void moveBack(double speed){
        _back.set(ControlMode.PercentOutput, speed);
    }

    public void moveBoth(double speed){
        moveFront(speed);
        moveBack(speed);
    }

    public void autoClimb(){
        //go up
        addSequential(new up());
        //once the end limit switches are hit zoop
        addSequential(new zoop());
        //once pneumatic is done, move up front
        addSequential(new upFront());
        //once front hits mid limit switch, drive forward a certain amount of rotations
        addSequential(new driveFoward());
        //once those rotations are achieved, move the back up
        addSequential(new upBack());
        //once the middle back limit switch is hit, drive forward
        addSequential(new driveFoward());
        //once the wheels move forwar a certain amount of rotions, zoop the climber in the inital position
        addSequential(new unZoop());
    }

    public void startClimb(boolean ready){
        if(ready) autoClimb();
    }

    public void getLimitSwitches(){
        System.out.println("FRONT MIDDLE: " + _frontMiddle.get()); //front middle
        System.out.println("FRONT END: " + _frontEnd.get()); //back midle
        System.out.println("BACK MIDDLE: " + _backMiddle.get()); //front end
        System.out.println("BACK END: " + _backEnd.get()); //correct
        System.out.println();
    }

    public class up extends Command{
        public void execute() {
            // _boost.set(BOOST_DIR);
            while (!_frontEnd.get() || !_backEnd.get()){
                _front.set(1);
                _back.set(1);
            }
            // _boost.set(!BOOST_DIR);
        } 
		protected boolean isFinished() {
			return false;
		} 
    }

    public class zoop extends Command{
        public void execute() {
            _zoop.set(ZOOP_DIR);
        } 
		protected boolean isFinished() {
			return false;
		} 
    }

    public class upFront extends Command{
        public void execute() {
            while(!_frontMiddle.get()){
                _front.set(-1);
            }
        } 
		protected boolean isFinished() {
			return false;
		} 
    }

    public class upBack extends Command{
        public void execute() {
            while(!_backMiddle.get()){
                _back.set(-1);
            }
        } 
		protected boolean isFinished() {
			return false;
		} 
    }

    public class driveFoward extends Command{
        public void execute() {
            System.out.println("vroom vroom im driving");
            //_sunKist.moveDriveToTarget((RobotMap.ROBOT_LENGTH/2)/(2*Math.PI*(RobotMap.WHEEL_DIAMETER/2)));
        } 
		protected boolean isFinished() {
			return false;
		} 
    }

    public class unZoop extends Command{
        public void execute() {
            _zoop.set(!ZOOP_DIR);
        } 
		protected boolean isFinished() {
			return false;
		} 
    }
}

