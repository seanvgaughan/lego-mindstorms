
public class PIDController {
	float KP;
	float KI;
	float KD;
	float prevI = 0;
	float prevE = 0;
	long prevTime;
	
	public PIDController(float kp, float ki, float kd) {
		this.KP = kp;   this.KI = ki;   this.KD = kd;
	}
	
	public float calculate(float error) {
		float pTerm = KP * error;
		
		float lpTime = 1;
		float curI = prevI + ( (error + prevE) * (lpTime / 2) );
		float iTerm = KI * curI;
		
		float curD = ( (error - prevE) / lpTime );
		float dTerm = KD * curD;
		
		prevE = error;
		prevI = curI;
		return pTerm + iTerm + dTerm;
	}
}