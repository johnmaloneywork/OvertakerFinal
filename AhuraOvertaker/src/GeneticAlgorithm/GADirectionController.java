package GeneticAlgorithm;

import ahooraDriver.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class GADirectionController {
    private ParametersContainerE6 myPara;
    private double estimatedTurn = 0.0;

    public GADirectionController (){
    }

    public float calculateSteer(MySensorModel sensors, StuckTypes stuck, boolean isOut){

        float steer;

        /**
         * IF STUCK CODE
         * BEGINNING
         */
        if(stuck != StuckTypes.NoStuck){
            if(isOut){
                if(DriverControllerHelperE6.isOnTheLeftHandSide(sensors)){//left hand side of the track
                    if(DriverControllerHelperE6.isTowardsInsideTheTrack(sensors)){//is towards the track, try to enter the track slowly (with a smooth direction)
                        if(DriverControllerHelperE6.isInTheCorrectDirection(sensors)){
                            steer = (float) 0.0;//g=1
                        }else{
                            steer = (float) 0.2;//g=1
                        }

                    }else{
                        if(DriverControllerHelperE6.isInTheCorrectDirection(sensors)){
                            steer = (float) 0.2;//g=-1
                        }else{
                            steer = (float) 0.2;//g=-1
                        }
                    }

                }else{//right hand side of the track
                    if(DriverControllerHelperE6.isTowardsInsideTheTrack(sensors)){//is towards the track, try to enter the track slowly (with a smooth direction)
                        if(DriverControllerHelperE6.isInTheCorrectDirection(sensors)){
                            steer = (float) 0.0;//g=1
                        }else{
                            steer = (float) -0.2;//g=1
                        }

                    }else{
                        if(DriverControllerHelperE6.isInTheCorrectDirection(sensors)){
                            steer = (float) -0.2;//g=-1
                        }else{
                            steer = (float) -0.2;//g=-1
                        }

                    }
                }
            }else{
                if(sensors.getGear() == -1){
                    steer = -(float) Math.signum(sensors.getAngleToTrackAxis());
                }else{
                    steer = (float) Math.signum(sensors.getAngleToTrackAxis());
                }
            }
            steer *= 5.0;
        }
        /**
         * IF STUCK CODE
         * ENDING
         */


        else{
            /**
             * ON TRACK CODE
             * BEGINNING
             */


            if(!isOut){
//                int maxDistanceSensorIndx = DriverControllerHelperE6.extermumIndexAngle(MySensorModel.getTrackEdgeSensors(), extermumTypes.maximization);
//                float distInfront = (float) DriverControllerHelperE6.maximumDistanceInfront(MySensorModel.getTrackEdgeSensors());
//
//                double maxCorrectionSensors = getMyPara().getParameterByName("Maxsensors");
//                double minCorrectionSensors = getMyPara().getParameterByName("Minsensors");
//                double soarDirection;//turnDirection/(minInMiddle-maxIn);//[-1, 1] left or right of the track
//
//                double maxSoar = getMyPara().getParameterByName("MaximumSoar");//the minimum distance that it is still in soar (10 sensors for correction until that)
//                double minSoar = getMyPara().getParameterByName("MinimumSoar");//the maximum distance that it is in soar (3 sensor for correction from that on)
//
//                double numberOfCorrectionSensors = DriverControllerHelperE6.logSig(minCorrectionSensors, maxCorrectionSensors, minSoar, maxSoar, 0.99, distInfront);
//
//                int correctionSensors = (int) numberOfCorrectionSensors;
//                double minv = getMyPara().getParameterByName("minv");
//                double midv1 = getMyPara().getParameterByName("midv1");
//                double midv2 = getMyPara().getParameterByName("midv2");
//                double maxv = getMyPara().getParameterByName("maxv");
//
//                double s = DriverControllerHelperE6.trapasoide(minv, midv1, midv2, maxv, distInfront);
//
//                if(getEstimatedTurn() < 0.0)//we are going to turn left
//                    s = -s;
//                soarDirection = s;
//
//
//                double steerToTurn = -weightedMean2(DriverControllerHelperE6.angles, MySensorModel.getTrackEdgeSensors(),
//                        maxDistanceSensorIndx, correctionSensors, soarDirection, sensors);
//                steer = (float) (steerToTurn);
//
//
                int current = GeneticDriverController.driverCounter;
                IndividualTest individualTest = GAClient.getIndividuals()[current - 1];

                steer = (float) ((sqrt(MySensorModel.getTrackPosition() * individualTest.getAllGenes()[15]) +
                        sqrt(MySensorModel.getTrackEdgeSensors()[0] * individualTest.getAllGenes()[16]) +
                        //sqrt(MySensorModel.getTrackEdgeSensors()[4] * individualTest.getAllGenes()[17]) +
                        sqrt(MySensorModel.getTrackEdgeSensors()[7] * individualTest.getAllGenes()[17]) +
                        //(MySensorModel.getTrackEdgeSensors()[7] * individualTest.getAllGenes()[17]) +
                        //sqrt(MySensorModel.getTrackEdgeSensors()[8] * individualTest.getAllGenes()[19]) +
                        sqrt(MySensorModel.getTrackEdgeSensors()[9] * individualTest.getAllGenes()[18]) +
                        (MySensorModel.getTrackEdgeSensors()[9] * individualTest.getAllGenes()[19]) +
                        //sqrt(MySensorModel.getTrackEdgeSensors()[9] / individualTest.getAllGenes()[21]) +
                        //sqrt(MySensorModel.getTrackEdgeSensors()[10] * individualTest.getAllGenes()[21]) +
                        sqrt(MySensorModel.getTrackEdgeSensors()[11] * individualTest.getAllGenes()[20]) +
                        //(MySensorModel.getTrackEdgeSensors()[9] * individualTest.getAllGenes()[20]) +
                        //sqrt(MySensorModel.getTrackEdgeSensors()[14] * individualTest.getAllGenes()[23]) +
                        sqrt(MySensorModel.getTrackEdgeSensors()[18] * individualTest.getAllGenes()[21])) / 30000);

                //System.out.println("STEER: " + steer);

                /**
                 * ON TRACK CODE
                 * ENDING
                 */
            }else{
                steer = -(float) ((float) ((-sensors.getAngleToTrackAxis()*2.0)+Math.signum(MySensorModel.getTrackPosition())*13.0*Math.PI/180));

            }
        }

        return steer;
    }

    public double weightedMean2(float[] angles, double[] distances, int baseAngle,
                                int count, double soarDirection, MySensorModel sensors){
        soarDirection = Math.pow(2.0, 2.0*soarDirection);//[-1, 1] left or right of the track
        double resx=0;
        double resy=0.0;

        for(int i=baseAngle - count; i <= baseAngle + count; ++i){
            int indx = Math.min(i, angles.length - 1);
            indx = Math.max(indx, 0);

            if(Math.abs(angles[indx]) < 2 && angles[indx] != 0)
                continue;

            double dis = distances[indx];
            if(i<baseAngle){
                dis /= soarDirection;
            }
            if(i>=baseAngle){
                dis *= soarDirection;
            }
            if(i == baseAngle){
                dis *= 2.0;
            }
            if(Math.abs((sensors.getAngleToTrackAxis() + angles[indx]*Math.PI/180.0)) > Math.PI/2 ){
                continue;
            }
            resx += (DriverControllerHelperE6.cosAng[indx]*(dis));
            resy += (DriverControllerHelperE6.sinAng[indx]*(dis));
        }
        double res = Math.atan2(resy, resx);//*sumDist/sumDist2;

        return res;
    }


    public double getEstimatedTurn() {
        return estimatedTurn;
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
}
