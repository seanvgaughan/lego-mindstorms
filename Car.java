import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Car implements CarInterface {
	EV3LargeRegulatedMotor left;
	EV3LargeRegulatedMotor right;
	
	public Car() {
		left = new EV3LargeRegulatedMotor(MotorPort.A);
		right = new EV3LargeRegulatedMotor(MotorPort.D);
	}

	@Override
	public RegulatedMotor getLeftMotor() {
		return left;
	}

	@Override
	public RegulatedMotor getRightMotor() {
		return right;
	}

	@Override
	public float getTrackWidth() {
		return 3.5f;   /* 3.5 inches between two front wheels */
	}

	@Override
	public float getTireWidth() {
		return 26/25.4f;   /* 26 millimeter to inches */
	}

	@Override
	public float getTireDiameter() {
		return 55/25.4f;   /* 56 millimeter to inches */
	}
	
	public void close() {
		left.close();
		right.close();
	}
}