
public class MoveControl implements MoveControlInterface {
	Car car;
	private int targetSpDeg = 360; //default speed
	private int responseRg = 0;
	
	public MoveControl(Car car) {
		this.car = car;
	}
	
	public void setAcceleration(int accel) {
		car.getLeftMotor().setAcceleration(accel);
		car.getRightMotor().setAcceleration(accel);
	}
	
	public int getResponseRg() { return responseRg; }
	
	@Override
	public void setSpeed(float inchesInSec) {
		/* degrees per second = inches per second * 360 / circumference */
		int degreesInSec = (int) ((inchesInSec * 360.0) / (car.getTireDiameter() * Math.PI));
		car.getLeftMotor().setSpeed(degreesInSec);
		car.getRightMotor().setSpeed(degreesInSec);
		targetSpDeg = degreesInSec;
		responseRg = 960 - targetSpDeg;
	}

	@Override
	public void drive() {
		car.getLeftMotor().forward();
		car.getRightMotor().forward();
	}

	@Override
	public void drive(float inches) {
		/* degrees of rotation = travelDistance * 360 / circumference */
		int degForward = (int) ((inches * 360.0) / ( car.getTireDiameter() * Math.PI ));
		car.getLeftMotor().rotate(degForward,true);
		car.getRightMotor().rotate(degForward);
		stop();
	}

	@Override
	public void reverse() {
		car.getLeftMotor().backward();
		car.getRightMotor().backward();
	}

	@Override
	public void reverse(float inches) {
		/* degrees of rotation = travelDistance * 360 / circumference */
		int degForward = (int) ((inches * 360.0) / ( car.getTireDiameter() * Math.PI ));
		car.getLeftMotor().rotate(-degForward,true);
		car.getRightMotor().rotate(-degForward);
		stop();
	}

	@Override
	public void stop() {
		car.getLeftMotor().stop(true);
		car.getRightMotor().stop();
	}

	@Override
	public void rotate(int degrees) {
		/* degreeOf motorRotation = ( (trackWidth + tireWidth) * degreesOf Turn ) / tireDiameter */
		float deg = ( (car.getTrackWidth() + car.getTireWidth()) * degrees ) / car.getTireDiameter();
		int rotateDeg = (int) Math.ceil(deg);
		car.getLeftMotor().rotate(-rotateDeg,true);
		car.getRightMotor().rotate(rotateDeg);
	}
	
	@Override
	public void turn(int degrees) {
		if(targetSpDeg - degrees <= 0) {
			car.getLeftMotor().setSpeed(1);
		} else {
			car.getLeftMotor().setSpeed(targetSpDeg - degrees);
		}
		if(targetSpDeg + degrees <= 0) {
			car.getRightMotor().setSpeed(1);
		} else {
			car.getRightMotor().setSpeed(targetSpDeg + degrees);
		}
	}
}