import lejos.robotics.RegulatedMotor;

public interface CarInterface {
	RegulatedMotor getLeftMotor();
	RegulatedMotor getRightMotor();
	float getTrackWidth();        // inches between two front wheels
	float getTireWidth();         // in inches
	float getTireDiameter();      // in inches
}