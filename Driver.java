import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class Driver {
	private static float trgtSp = 1;
	private static float fullWhite;
	private static float setPoint;
	private static float fullBlack;
	private static int K = 1;
	private static float Kp = 320;
	private static float Ki = 1;
	private static float Kd = 1024;

	public static void main(String[] args) throws InterruptedException {
		Car car = new Car();
		MoveControl carCntrl = new MoveControl(car);
		PIDController pid = new PIDController(Kp, Ki, Kd);
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
		SampleProvider provider = colorSensor.getRedMode();
		
		setUpSpeed();
		carCntrl.setSpeed(trgtSp);
		setProportionalRange(provider);
		//displayEverything();
		BSW leftSensor = new BSW(0);
		BSW rightSensor = new BSW(1);
		ConstructionZone cz = new ConstructionZone(carCntrl, trgtSp);
		leftSensor.start();
		rightSensor.start();
		cz.start();
		Button.waitForAnyPress();
		
		float reading;
		float error;
		int utTimesK = 0;
		int outOfBound  = 0;
		float newFullWhite = fullWhite - ((fullWhite - setPoint) / 3);
		boolean lost = false;
		carCntrl.drive();
		while(!lost) {
			reading = getSensorReading(provider);
			if (reading >= newFullWhite) {
				//LCD.clear(6); LCD.drawString("outOfBound=" + outOfBound, 3, 6);
				outOfBound = outOfBound + 1;
			} else if (reading <= fullBlack) {
				//LCD.clear(6); LCD.drawString("outOfBound=" + outOfBound, 3, 6);
				outOfBound = outOfBound + 1;
			} else {
				outOfBound = 0;
			}
			if (outOfBound >= 200) {
				lost = true;    /* terminate */
			}
			error = (setPoint - reading);
			utTimesK = (int) (K * pid.calculate(error));
			/*LCD.clear(6); LCD.clear(7);
			LCD.drawString("error=" + error, 0, 6);
			LCD.drawString("utTimesK=" + utTimesK, 0, 7);*/
			if(utTimesK > carCntrl.getResponseRg()) {
				utTimesK = carCntrl.getResponseRg();
			}
			carCntrl.turn(utTimesK);
			Thread.sleep(4);
		}
		carCntrl.stop();
		LCD.clear(); LCD.drawString("LOST!!", 5, 3);
		Sound.setVolume(80);  Sound.twoBeeps();
		leftSensor.cancel();
		rightSensor.cancel();
		cz.cancel();
		Button.waitForAnyPress();
		colorSensor.close();
		car.close();
	}
	
	private static void setUpSpeed() {
		LCD.drawString("Select Speed", 3, 2);
		LCD.drawString("Speed=" + trgtSp, 2, 4);
		int pressed = Button.waitForAnyPress();
		while(pressed != Button.ENTER.getId()) {
			if (pressed == Button.UP.getId() && trgtSp < 18 ) {
				trgtSp = trgtSp + 0.5f;
			}
			else if (pressed == Button.DOWN.getId() && trgtSp > 0 ) {
				trgtSp = trgtSp - 0.5f;
			}
			LCD.clear(4); LCD.drawString("Speed=" + trgtSp, 2, 4);
			pressed = Button.waitForAnyPress();
		}
	}
	
	private static void setProportionalRange(SampleProvider provider) {
		LCD.clear();
		LCD.drawString("Press Any Button", 1, 2);
		LCD.drawString("to read fullWhite", 0, 3);
		LCD.drawString("then fullBlack", 2, 4);
		Button.waitForAnyPress();
		fullWhite = getSensorReading(provider);
		Button.waitForAnyPress();
		fullBlack = getSensorReading(provider);
		setPoint = (fullWhite + fullBlack) / 2;
		LCD.clear();
	}
	
	private static float getSensorReading(SampleProvider provider) {
		float[] sample = new float[provider.sampleSize()];
		provider.fetchSample(sample, 0);
		return sample[0];
	}
	
	public static void displayEverything() {
		LCD.clear();
		LCD.drawString("fullWhite=" + fullWhite, 0, 0);
		LCD.drawString("set point=" + setPoint, 0, 1);
		LCD.drawString("fullBlack=" + fullBlack, 0, 2);
		int trgtSpDeg = (int) ((trgtSp * 360.0 * 25.4) / (55 * Math.PI));
		LCD.drawString("tSpDeg=" + trgtSpDeg, 0, 3);
		LCD.drawString("responseRg=" + (960 - trgtSpDeg), 0, 4);
	}
}