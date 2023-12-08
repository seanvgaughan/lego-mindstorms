import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class BSW extends Thread {
	private EV3UltrasonicSensor sensor;
	private int x;
	private boolean stopRequest = false;
	
	public BSW(int side) {
		if(side == 0) {
			this.sensor = new EV3UltrasonicSensor(SensorPort.S1);
			this.x = 0;
		}
		else {
			this.sensor = new EV3UltrasonicSensor(SensorPort.S4);
			this.x = 9;
		}
	}
	
	public void cancel() {
		stopRequest = true;
	}
	
	@Override
	public void run() {
		SampleProvider provider = sensor.getDistanceMode();
		float[] sample = new float[provider.sampleSize()];
		while(!stopRequest) {
			try {
				provider.fetchSample(sample, 0);
				int distance = (int) (sample[0] * 100.0);
				if(distance <= 20) {
					Sound.setVolume(80);   Sound.twoBeeps();
					LCD.drawString("!!!- " + distance, x, 0);
					Thread.sleep(2000);
					LCD.clear(x, 0, 7);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sensor.close();
	}
}