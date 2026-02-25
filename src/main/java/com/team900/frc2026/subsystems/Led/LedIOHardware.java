import com.ctre.phoenix.led.CANdle;

import com.team900.frc2026.Robot;
import com.ctre.phoenix.led.CANdle;

public class LedIOHardware implements LedIO{
    private final CANdle candle;
    private LedState currentState = LedState.kOff;
    private LedState[] currentPixels = new LedState
                    [LedConstants.kCandleLEDCount
                    + LedConstants.kNonCandleLEDCount];
    
    //Just realized CANdle is being deprecated ill try to fix stuff later
    public LedIOHardware() {
        if(Robot.isReal()) {
            candle = new CANdle(
                LedConstants.kCANdleId,getDeviceNumber(),
                LedConstants.kCANdleId.getBus()
            );
        } else {
            candle = null;
        }
    }

    public LedState getCurrentState() {
        return currentState;
    }

    public LedState[] getCurrentPixels() {
        return currentPixels;
    }

    @Override 
    public void writePixels(LedState state){
        if (state==null) state = LedState.kOff;
        currentState = state;
        if (candle!=null) candle.setControl(newSolidColor(state.red, state.green, state.blue);
    }

    @Override
    public void writePixels(LedState[] pixels){
        if(pixels==null || pixels.length==0) return;

        LedState run = pixels[0];
        int idx = 0;
        for(int i=0; i<pixels.length; i++){
            if(pixels[i]==null) pixels[i] = LedState.kOff;
            if(!run.equals(pixels[i])){
                if(candle!=null){
                    candle.setControl(newSolidColor(run.red, run.green, run.blue), 255, idx, i=idx);
                }
                idx = i;
                run = pixels[i];
                currentPixels[i] = run;
            }
        }

        if(candle!=null) candle.setControl(newSolidColor(run.red, run.green, run.blue), 255, idx, pixels.length - idx);
    }
}
