
public interface MoveControlInterface {
	void setSpeed(float inchesInSec);   // linear speed at inches per second
	void drive();                       // move the car forward
	void drive(float inches);           // move forward by inches, then stop
	void reverse();                     // reverse the car 
	void reverse(float inches);         // reverse forward by inches, then stop
	void stop();                        // stop the car
	void rotate(int degrees);           // turn left/right by degrees when +/-
	void turn(int degrees);             // changes the speed by + and - degrees
}