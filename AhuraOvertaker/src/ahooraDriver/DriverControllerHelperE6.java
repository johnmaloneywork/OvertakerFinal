package ahooraDriver;

import java.util.ArrayList;
import java.util.List;


/**
 * @author mbonyadi
 *
 */
public class DriverControllerHelperE6 {

    public static int memorySensorLength = 1;
    public static int[]  gearUp={9500,9400,9500,9500,9500,0};
    public static int[]  gearDown={0,3300,6200,7000,7300,7700};
    public static double minSpeed = 25;
    public static double maxSpeed = 360;

    public static float [] angles = {-90,-75,-50,-35,-20,-15,-10,-5,-1,0,1,5,10,15,20,35,50,75,90};

    public static double [] cosAng = {6.12323399573677e-17,0.258819045102521,0.642787609686539,0.819152044288992,0.939692620785908,0.965925826289068,0.984807753012208,0.996194698091746,0.999847695156391,1,0.999847695156391,0.996194698091746,0.984807753012208,0.965925826289068,0.939692620785908,0.819152044288992,0.642787609686539,0.258819045102521,6.12323399573677e-17};


    public static double [] sinAng = {-1,-0.965925826289068,-0.766044443118978,-0.573576436351046,-0.342020143325669,-0.258819045102521,-0.173648177666930,-0.0871557427476582,-0.0174524064372835,0,0.0174524064372835,0.0871557427476582,0.173648177666930,0.258819045102521,0.342020143325669,0.573576436351046,0.766044443118978,0.965925826289068,1};


    public static double [] sinAngO = {1.22464679914735e-16,0.173648177666930,0.342020143325669,0.500000000000000,0.642787609686540,0.766044443118978,0.866025403784439,0.939692620785908,0.984807753012208,1,0.984807753012208,0.939692620785908,0.866025403784439,0.766044443118978,0.642787609686539,0.500000000000000,0.342020143325669,0.173648177666930,0,-0.173648177666930,-0.342020143325669,-0.500000000000000,-0.642787609686539,-0.766044443118978,-0.866025403784439,-0.939692620785908,-0.984807753012208,-1,-0.984807753012208,-0.939692620785908,-0.866025403784439,-0.766044443118978,-0.642787609686540,-0.500000000000000,-0.342020143325669,-0.173648177666930,-1.22464679914735e-16};

    public static double [] cosAngO = {-1,-0.984807753012208,-0.939692620785908,-0.866025403784439,-0.766044443118978,-0.642787609686539,-0.500000000000000,-0.342020143325669,-0.173648177666930,6.12323399573677e-17,0.173648177666930,0.342020143325669,0.500000000000000,0.642787609686539,0.766044443118978,0.866025403784439,0.939692620785908,0.984807753012208,1,0.984807753012208,0.939692620785908,0.866025403784439,0.766044443118978,0.642787609686539,0.500000000000000,0.342020143325669,0.173648177666930,6.12323399573677e-17,-0.173648177666930,-0.342020143325669,-0.500000000000000,-0.642787609686539,-0.766044443118978,-0.866025403784439,-0.939692620785908,-0.984807753012208,-1};

    public static int zeroAngle = 9;

    public static double asrSlip= 1.0;
    public static double asrRange= 1.0;
    public static double asrMinSpeed = 150.0;

    /* ABS Filter Constants */
    public static float wheelRadius[]={(float) 0.3179,(float) 0.3179,(float) 0.3276,(float) 0.3276};

    /* Clutching Constants */
    public static float clutchMax=(float) 0.5;
    public static float clutchDelta=(float) 0.05;
    public static float	clutchDeltaTime=(float) 0.02;
    public static float clutchDeltaRaced=10;
    public static float clutchDec=(float) 0.01;
    public static float clutchMaxModifier=(float) 1.3;
    public static float clutchMaxTime=(float) 1.5;

    private static double sin1 = Math.sin(Math.PI/180.0);
    private static double cos1 = Math.cos(Math.PI/180.0);

    public static int extermumIndexAngle(double [] in, extermumTypes type){
        int indx = 0;
        for (int i=0;i<in.length;++i){

            if(((double)extermumTypes.toInt(type))*in[indx]>((double)extermumTypes.toInt(type))*in[i]){
                indx = i;
            }else{
                if(((double)extermumTypes.toInt(type))*in[indx]==((double)extermumTypes.toInt(type))*in[i]){
                    if(Math.abs(angles[indx]) > Math.abs(angles[i]))
                        indx = i;
                }
            }
        }
        if(Math.abs(angles[indx]) < 2)
            indx = zeroAngle;
        return indx;
    }

    public static double maximumDistanceInfront(double[] proximities){
        return proximities[zeroAngle];
    }


    /**
     * @param minY: minimum value that the curve can converge to
     * @param maxY: maximum value that the curve can converge to
     * @param minX: the value of x that has the minimumY
     * @param maxX: the value of x that has the maximumY
     * @param x: the current x that it y is needed
     * @return
     */
    public static double logSig(double minY,  double maxY, double minX, double maxX, double percent, double x){

        if(minY>maxY){
            double t = maxY;
            maxY=minY;
            minY=t;
            t=maxX;
            maxX=minX;
            minX=t;
        }
        double c = Math.log(((maxY-minY/percent)*(percent*maxY-minY))/((-minY+minY/percent)*(maxY-percent*maxY)))/(minX-maxX);
        double d = -maxX+Math.log(((maxY-minY)/(percent*maxY-minY))-1.0)/c;
        double res= logSig(maxY, minY, c, d, x);
        return res;
    }

    public static double logSig(double maxY,  double minY, double c, double d, double x){
        double a= maxY-minY;
        double b = 1.0;
        double res = ((a)/(b+Math.exp(c*(x+d)))+minY);
        res = Math.round(res*1000.0)/1000.0;
        return res;
    }

    public static double trapasoide(double a, double b, double c, double d, double inp){
        if(inp < a || inp > d){
            return 0.0;
        }

        if(inp > b && inp < c){
            return 1.0;
        }

        if(inp > a && inp < b){
            return (((1.0-0.0)/(b-a))*(inp-a));
        }

        return ((0.0-1.0)/(d-c))*(inp-c)+1.0;
    }

    public static double turnDirectionCalculator() {

        double distLeft = MySensorModel.getTrackEdgeSensors()[DriverControllerHelperE6.zeroAngle - 1];//DriverControllerHelperE4.angleToDistanceInterpolate(leftAngl, sensors.getTrackEdgeSensors());
        double distRight = MySensorModel.getTrackEdgeSensors()[DriverControllerHelperE6.zeroAngle + 1];//DriverControllerHelperE4.angleToDistanceInterpolate(rightAngl, sensors.getTrackEdgeSensors());
        double distBase = MySensorModel.getTrackEdgeSensors()[DriverControllerHelperE6.zeroAngle];//DriverControllerHelperE4.angleToDistanceInterpolate(baseAngl, sensors.getTrackEdgeSensors());
        double sinAngle = 0.0;
        double k = 0.0;

        if(distRight > distLeft){
            k = distRight*sin1/(distBase-distRight*cos1);
        }else{
            k = distBase*sin1/(distLeft-distBase*cos1);
        }
        sinAngle = Math.atan(k);

        return sinAngle;
    }

    public static boolean isTowardsInsideTheTrack(MySensorModel sensors){
        return MySensorModel.getTrackPosition()*sensors.getAngleToTrackAxis() > 0;
    }
    public static boolean isOnTheLeftHandSide(MySensorModel sensors){
        return Math.abs(MySensorModel.getTrackPosition()) > 0.0 && MySensorModel.getTrackPosition() > 0;
    }
    public static boolean isInTheCorrectDirection(MySensorModel sensor){
        return Math.abs(sensor.getAngleToTrackAxis()) <= Math.PI/2;
    }
}
