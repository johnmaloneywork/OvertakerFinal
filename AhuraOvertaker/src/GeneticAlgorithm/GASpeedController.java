package GeneticAlgorithm;

import ahooraDriver.DriverControllerHelperE6;
import ahooraDriver.MySensorModel;
import ahooraDriver.ParametersContainerE6;
import ahooraDriver.StuckTypes;

import static java.lang.Math.sqrt;

public class GASpeedController {
    private ParametersContainerE6 myPara;
    private double estimatedTurn = 0.0;
    private double pSpeed = 0.0;

    public GASpeedController(){

    }

    public float [] calcBrakeAndAccelPedals(MySensorModel sensors, double currentSteer, StuckTypes isStuck, boolean isOut){
        float accel_and_brake = calcAccel(sensors, currentSteer, isOut, isStuck);
        float accel,brake;
        if (accel_and_brake>0) {
            accel = filterASR(sensors, accel_and_brake);
            brake = 0;
        }
        else {
            accel = 0;
            // apply ABS to brake
            brake = filterABS(sensors,-accel_and_brake);
        }

        return new float[]{brake, accel};
    }

    public float clutching(MySensorModel sensors, float clutch)
    {

        float maxClutch = DriverControllerHelperE6.clutchMax;

        // Check if the current situation is the race start
        if ((MySensorModel.getCurrentLapTime()<DriverControllerHelperE6.clutchDeltaTime  &&
                sensors.getDistanceRaced()<DriverControllerHelperE6.clutchDeltaRaced)) {
            clutch = maxClutch;
        }

        // Adjust the current value of the clutch
        if(clutch > 0)
        {
            double delta = DriverControllerHelperE6.clutchDelta;
            if (sensors.getGear() < 2)
            {
                // Apply a stronger clutch output when the gear is one and the race is just started
                delta /= 2.0;
                maxClutch *= DriverControllerHelperE6.clutchMaxModifier;
                if (MySensorModel.getCurrentLapTime() < DriverControllerHelperE6.clutchMaxTime)
                    clutch = maxClutch;
            }

            // check clutch is not bigger than maximum values
            clutch = Math.min(maxClutch,clutch);

            // if clutch is not at max value decrease it quite quickly
            if (clutch!=maxClutch)
            {
                clutch -= delta;
                clutch = Math.max((float) 0.0,clutch);
            }
            // if clutch is at max value decrease it very slowly
            else
                clutch -= DriverControllerHelperE6.clutchDec;
        }
        return clutch;
    }


    private float filterASR(MySensorModel sensors,float accel){
        float asrSlip = (float) DriverControllerHelperE6.asrSlip;
        float asrRange = (float) DriverControllerHelperE6.asrRange;
        float asrMinSpeed = (float) DriverControllerHelperE6.asrMinSpeed;

        // convert speed to m/s
        float speed = (float) (MySensorModel.getSpeed() / 3.6);
        // when spedd lower than min speed for abs do nothing
        if (speed > asrMinSpeed)
            return accel;

        // compute the speed of wheels in m/s
        float slip = 0.0f;
        for (int i = 0; i < 4; i++) {
            double wheelsSpeed = sensors.getWheelSpinVelocity()[i] * DriverControllerHelperE6.wheelRadius[i];
            slip += wheelsSpeed;
        }

        // slip is the difference between actual speed of car and average speed of wheels
        slip = speed - slip/4.0f;

        // when slip too high applu ABS
        if (-slip > asrSlip) {
            accel = accel + (slip + asrSlip)/asrRange;
        }
        // check brake is not negative, otherwise set it to zero
        if (accel<0)
            return 0;
        else
            return accel;
    }

    private float filterABS(MySensorModel sensors,float brake){
        float absSlip = (float) getMyPara().getParameterByName("absSlip");//DriverControllerHelperE5.absSlip;
        float absRange = (float) getMyPara().getParameterByName("absRange");//DriverControllerHelperE5.absRange;
        float absMinSpeed = (float) getMyPara().getParameterByName("absMinSpeed");//DriverControllerHelperE5.absMinSpeed;

        // convert speed to m/s
        float speed = (float) (MySensorModel.getSpeed() / 3.6);
        // when spedd lower than min speed for abs do nothing
        if (speed < absMinSpeed)
            return brake;

        // compute the speed of wheels in m/s
        float slip = 0.0f;
        for (int i = 0; i < 4; i++) {
            double wheelsSpeed = sensors.getWheelSpinVelocity()[i] * DriverControllerHelperE6.wheelRadius[i];
            slip += wheelsSpeed;
        }

        // slip is the difference between actual speed of car and average speed of wheels
        slip = speed - slip/4.0f;
        // when slip too high applu ABS
        if (slip > absSlip) {
            brake = brake - (slip - absSlip)/absRange;
        }

        // check brake is not negative, otherwise set it to zero
        if (brake<0)
            return 0;
        else
            return brake;
    }

    public int calculateGear(MySensorModel sensors, StuckTypes stuck){

        int currentGear = sensors.getGear();
        double rpm  = sensors.getRPM();

        if(stuck == StuckTypes.AngularStuck || stuck == StuckTypes.WallStuck || stuck == StuckTypes.OpponentStuck){
            int gear;
            if(stuck == StuckTypes.WallStuck || stuck == StuckTypes.OpponentStuck)
                gear = -1;
            else
                gear = 1;

            return gear;
        }else{
            if (currentGear<1)
                return 1;
            if (currentGear <6 && rpm >= DriverControllerHelperE6.gearUp[currentGear-1])
                return currentGear + 1;
            else
            if (currentGear > 1 && rpm <= DriverControllerHelperE6.gearDown[currentGear-1])
                return currentGear - 1;
            else
                return currentGear;
        }
    }

    public void setEstimatedTurn(double estimatedTurn) {
        this.estimatedTurn = estimatedTurn;
    }

    public ParametersContainerE6 getMyPara() {
        return myPara;
    }

    public void setMyPara(ParametersContainerE6 myPara) {
        this.myPara = myPara;
    }

    public float calcAccel(MySensorModel sensors, double currentSteer, boolean isOut, StuckTypes isStuck){

        double targetSpeed;
        if(isStuck != StuckTypes.NoStuck){
            if(sensors.getGear() < 0){
                targetSpeed = DriverControllerHelperE6.minSpeed;

            }else
                targetSpeed = DriverControllerHelperE6.minSpeed*2.0;
        } else{
            if(isOut){
                if(sensors.getGear() < 0){
                    targetSpeed = DriverControllerHelperE6.minSpeed;
                }else {
                    if(Math.abs(currentSteer)<0.1){
                        targetSpeed = Math.max(DriverControllerHelperE6.minSpeed*4.0, MySensorModel.getSpeed());
                    }else{
                        targetSpeed = DriverControllerHelperE6.minSpeed*3.0;
                    }

                }
            }else{
                targetSpeed = targetSpeed(sensors);
            }
        }

//        System.out.println("GUESS: " + ((float) (2.0 / (MySensorModel.getSpeed() - targetSpeed))));
//        System.out.println("CALC ACCEL: " + ((float) (2.0/ (1.0+Math.exp(Math.abs(MySensorModel.getSpeed()) - targetSpeed))) - 1.0));
//        System.out.println("SPEED - TARGET SPEED: " + (MySensorModel.getSpeed() - targetSpeed));
//        System.out.println("MATH.EXP((MATH.ABS(SPEED - TARGET SPEED)): " + (Math.exp((Math.abs(MySensorModel.getSpeed()) - targetSpeed))));
        //System.out.println("CALC ACCEL: " + (float) (2.0/(1.0+Math.exp((Math.abs(MySensorModel.getSpeed()) - targetSpeed))) - 1.0));
        return (float) (2.0/(1.0+Math.exp((Math.abs(MySensorModel.getSpeed()) - targetSpeed))) - 1.0);

    }

    private double targetSpeed(MySensorModel sensors){

        //REVERSE
        if(sensors.getGear() < 0){
            return DriverControllerHelperE6.minSpeed;
        }

        int current = GeneticDriverController.driverCounter;
        IndividualTest individualTest = GAClient.getIndividuals()[current - 1];

        /*
         * RETURN A SIGMOID VALUE (POSSIBLY)
         * TOP SPEED = 306
         * LOW SPEED = 0
         */

        double targetSpeed =
                (MySensorModel.getTrackPosition() * individualTest.getAllGenes()[0]) +
                sqrt(MySensorModel.getTrackEdgeSensors()[0] * individualTest.getAllGenes()[1]) +
                        (MySensorModel.getTrackEdgeSensors()[0] * individualTest.getAllGenes()[2]) +
                //sqrt(MySensorModel.getTrackEdgeSensors()[4] * individualTest.getAllGenes()[3]) +
                (MySensorModel.getTrackEdgeSensors()[7] * individualTest.getAllGenes()[4]) +
                        (MySensorModel.getTrackEdgeSensors()[8] * individualTest.getAllGenes()[5]) +
                sqrt(MySensorModel.getTrackEdgeSensors()[8] * individualTest.getAllGenes()[6]) +
                sqrt(MySensorModel.getTrackEdgeSensors()[9] * individualTest.getAllGenes()[7]) +
                        (MySensorModel.getTrackEdgeSensors()[9] * individualTest.getAllGenes()[8]) +
                sqrt(MySensorModel.getTrackEdgeSensors()[10] * individualTest.getAllGenes()[9]) +
                        (MySensorModel.getTrackEdgeSensors()[10] * individualTest.getAllGenes()[10]) +
                (MySensorModel.getTrackEdgeSensors()[11] * individualTest.getAllGenes()[11]) +
                //sqrt(MySensorModel.getTrackEdgeSensors()[14] * individualTest.getAllGenes()[12]) +
                sqrt(MySensorModel.getTrackEdgeSensors()[18] * individualTest.getAllGenes()[13]) +
                        (MySensorModel.getTrackEdgeSensors()[18] * individualTest.getAllGenes()[14]);

        //System.out.println("TARGET SPEED: " + targetSpeed*myPara.getPenaltyCoef());
        return targetSpeed*myPara.getPenaltyCoef();
    }
}
