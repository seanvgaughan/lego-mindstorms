import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ConstructionZone extends Thread{
	private boolean stopRequest = false;
	private MoveControl carCntrl;
	private float trgtSP;
	private boolean inConstZone = false;
	
	public ConstructionZone(MoveControl cntrl, float trgtSpeed) {
		this.carCntrl = cntrl;
		this.trgtSP = trgtSpeed;
	}
	
	public void cancel() {
		stopRequest = true;
	}
	
	@Override
	public void run() {
		EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S2);
		SampleProvider provider = sensor.getColorIDMode();
		float[] sample = new float[provider.sampleSize()];
		while(!stopRequest) {
			provider.fetchSample(sample, 0);
			if(!inConstZone && sample[0] == 0) {
				inConstZone = true;
				carCntrl.setSpeed( trgtSP/2 );
				while(sample[0] == 0) {
					LCD.drawString("WARNING", 5, 1);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					LCD.clear(1);
					provider.fetchSample(sample, 0);
				}
				inConstZone = false;
				carCntrl.setSpeed( trgtSP );
			}
			/*if(inConstZone && sample[0] != 0) {
				inConstZone = false;
				carCntrl.setSpeed( trgtSP );
			}
			if(sample[0] == 0) {
				LCD.drawString("WARNING", 5, 1);
				Thread.sleep(2000);
				LCD.clear(1);
			}*/
		}
		sensor.close();
	}
}