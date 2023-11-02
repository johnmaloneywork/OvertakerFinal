package GeneticAlgorithm;

import ahooraDriver.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneticDriverController extends GAController {

    GASpeedController speedController = new GASpeedController();
    GADirectionController directionController = new GADirectionController();
    List<SensorModel> sensorList = new ArrayList<>();
    double totalDistance = 0.0;
    double prevLapTime = 0.0;
    double totalTime = 0.0;
    double damage = 0.0;
    float clutch = DriverControllerHelperE6.clutchMax;
    int lapCounter = 1;
    double bestLapTime = 5000.0;
    boolean lastLapTimeCounted= false;
    MySensorModel noiseCan = new MySensorModel();
    Action action = new Action();
    int episodeCounter = 1;
    static int driverCounter = 1;
    static int generationCounter = 9614;
    double damageAverage = 0;
    double fitnessAverage = 0;
    double topSpeedAverage = 0;
    double distanceAverage = 0;

    IndividualTest bestOverall = new IndividualTest();

    private IndividualTest individualTest;

    private final GA ga = new GA(1000);

    DecimalFormat decimalFormat = new DecimalFormat("#.###");
    static boolean overtakingMode = false;
    private static double topSpeed = 0;


    @Override
    public Action control(SensorModel sensors) {

        if (MySensorModel.getSpeed() > topSpeed){
            topSpeed = MySensorModel.getSpeed();
            MySensorModel.setTopSpeed(topSpeed);
        }

//        if (!MySensorModel.isOvertakeComplete() && MySensorModel.getCurrentLapTime() > 4.00) {
//            for (int i = 0; i < MySensorModel.getOvertakeSensors().length; i++) {
//                if (MySensorModel.getOvertakeSensors()[i] < 200) {
//                    MySensorModel.setOvertakeComplete(true);
//                    MySensorModel.setTimeAtOvertake(MySensorModel.getCurrentLapTime());
//                }
//            }
//        }
//
//        if (!MySensorModel.isAlongside() && MySensorModel.getCurrentLapTime() > 4.00){
//            for (int i = 0; i < MySensorModel.getAlongsideSensors().length; i++) {
//                if (MySensorModel.getAlongsideSensors()[i] < 200) {
//                    MySensorModel.setAlongside(true);
//                }
//            }
//        }

        if (sensors.getDamage() >= 9950){
            action.restartRace = true;
        }

        if (prevLapTime < bestLapTime && prevLapTime > 1){
            individualTest.getFitnessObject().setBestLapTime(prevLapTime);
        }

        totalDistance += sensors.getDistanceRaced();
//        if(sensors.getDistanceFromStartLine() < 10.0f && sensors.getDistanceFromStartLine() > 0.0f && !lastLapTimeCounted){
//            lastLapTimeCounted = true;
//            prevLapTime += sensors.getLastLapTime();
//            double lastLapTime = sensors.getLastLapTime();
//            lapCounter++;
//            if (lapCounter == 2){
//                bestLapTime = lastLapTime;
//                System.out.println("BEST LAP TIME: " + bestLapTime);
//            }
//            else if (lapCounter > 2 && bestLapTime > sensors.getLastLapTime()){
//                bestLapTime = lastLapTime;
//                System.out.println("BEST LAP TIME: " + bestLapTime);
//            }
//            if (lapCounter == 4){
//                System.out.println("BEST LAP TIME: " + bestLapTime);
//                action.restartRace = true;
//            }
//        }

        if(sensors.getDistanceFromStartLine() > 10.0f)
            lastLapTimeCounted = false;

        totalTime = prevLapTime + sensors.getCurrentLapTime();

        StuckTypes isStuck = StuckHandler.isStuck(sensors);
        boolean isOut = StuckHandler.isOutTrack(sensors);
        myPara.updatePenalty(isOut, sensors.getDamage(), lapCounter, sensors.getRacePosition());

        // TODO Auto-generated method stub
        speedController.setMyPara(myPara);
        directionController.setMyPara(myPara);

        sensorList.add(sensors);
        damage = sensors.getDamage();
        if(sensorList.size() > DriverControllerHelperE6.memorySensorLength){
            sensorList.remove(0);
        }

        noiseCan = NoiseCanceller.cancelNoise(sensorList);

        double estimatedTurn = DriverControllerHelperE6.turnDirectionCalculator();
        speedController.setEstimatedTurn(estimatedTurn);
        directionController.setEstimatedTurn(estimatedTurn);


        int gear = speedController.calculateGear(noiseCan, isStuck);
        double steer = directionController.calculateSteer(noiseCan, isStuck, isOut);

        myPara.frictionUpdater(gear, noiseCan, steer);
        myPara.trackWidthUpdater(noiseCan, steer, isOut);

        float [] accelBrake = speedController.calcBrakeAndAccelPedals(noiseCan, steer, isStuck, isOut);

        if(gear == 1) {
            clutch = DriverControllerHelperE6.clutchMax;
        }
        if(clutch > 0.0) {
            clutch = speedController.clutching(noiseCan, this.clutch);
        }

        action.gear = gear;
        action.steering = steer;
        action.accelerate = accelBrake[1];
        action.brake = accelBrake[0];
        action.clutch = clutch;


        return action;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

        //DECLARATIONS AND OUTPUT FOR EFFECT
        Fitness fitness = new Fitness();
        individualTest = GAClient.getIndividuals()[driverCounter - 1];
        individualTest.setFitnessObject(fitness);
        System.out.println("\n===== Gen."+generationCounter+ " - Dr." + driverCounter + " =====");

        //DAMAGE FITNESS
        individualTest.getFitnessObject().setDamage(MySensorModel.getDamage());
        System.out.println("\nDamage incurred: " + individualTest.getFitnessObject().getDamage());
        System.out.println("Damage fitness: " + individualTest.getFitnessObject().getDamageReward());

        damageAverage += individualTest.getFitnessObject().getDamage();

        //DISTANCE TRAVELLED FITNESS
        individualTest.getFitnessObject().setDistanceTravelled(MySensorModel.getDistanceFromStartLine());
        System.out.println("\nTotal distance: " + individualTest.getFitnessObject().getDistanceTravelled());
        System.out.println("Distance fitness: " + individualTest.getFitnessObject().getDistanceReward());
        distanceAverage += individualTest.getFitnessObject().getDistanceTravelled();

        //TOP SPEED FITNESS
        //individualTest.getFitnessObject().setTopSpeed(MySensorModel.getTopSpeed());
        individualTest.getFitnessObject().setTopSpeed(topSpeed);
        System.out.println("\nTop speed: " + individualTest.getFitnessObject().getTopSpeed());
        individualTest.getFitnessObject().setSpeedReward(topSpeed);
        System.out.println("Speed reward: " + individualTest.getFitnessObject().getSpeedReward());
        topSpeedAverage += individualTest.getFitnessObject().getTopSpeed();

        System.out.println("\nFastest Lap: " + individualTest.getFitnessObject().getBestLapTime());
        System.out.println("\nFastest Lap Reward: " + individualTest.getFitnessObject().getBestLapTimeReward());
        individualTest.getFitnessObject().getBestLapTimeReward();

        //OUTPUT DRIVER ID
        System.out.println("\nDRIVER " + individualTest.getId());

        //OVERALL FITNESS
        individualTest.setFitnessTotal(fitness.getOverallFitness());
        System.out.println("Total fitness: " + fitness.getOverallFitness());
        fitnessAverage += fitness.getOverallFitness();

        double currentFitness = fitness.getOverallFitness();

        //OUTPUTTING ALL DRIVER INFO TO A FILE FOR EACH GENERATION
        try {
            FileWriter fileWriter = new FileWriter("C:\\AhuraEvolution\\drivers-generation"+generationCounter+".txt", true);
            FileReader fileReader = new FileReader("C:\\AhuraEvolution\\drivers-generation"+generationCounter+".txt");

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            if(bufferedReader.readLine() == null){
                bufferedWriter.write("***************Drivers List***************\n");
            }

            if(driverCounter == 1){
                bufferedWriter.write("\n<><><><><><>Generation " + generationCounter + "<><><><><><>");
            }

            bufferedWriter.write("\n===============Driver " + driverCounter + "===============" +
                    "\n" + "Driver ID: " + individualTest.getId() +
                    "\n" + "Driver Genes: " + Arrays.toString(individualTest.getAllGenes()) +
                    "\n" + "Damage incurred: " + individualTest.getFitnessObject().getDamage() +
                    "\n" + "Damage fitness: " + individualTest.getFitnessObject().getDamageReward() +

                    "\n" + "Top speed: " + individualTest.getFitnessObject().getTopSpeed() +
                    "\n" + "Top speed reward: " + individualTest.getFitnessObject().getSpeedReward() +

                    "\n" + "Distance travelled: " + individualTest.getFitnessObject().getDistanceTravelled() +
                    "\n" + "Distance reward: " + individualTest.getFitnessObject().getDistanceReward() +

                    "\n" + "Fastest Lap: " + individualTest.getFitnessObject().getBestLapTime() +
                    "\n" + "Distance reward: " + individualTest.getFitnessObject().getBestLapTimeReward() +

                    "\n" + "Genes: " + Arrays.toString(individualTest.getAllGenes()) +

                    "\n" + "Total fitness: " + decimalFormat.format(currentFitness) + "\n");

            if (driverCounter == 40){
                bufferedWriter.write("\nGeneration " + generationCounter + " Average Damage: " + (damageAverage / 40) +
                        "\nGeneration " + generationCounter + " Average Top Speed: " + (topSpeedAverage / 40) +
                        "\nGeneration " + generationCounter + " Average Distance Travelled: " + (distanceAverage / 40) +
                        "\nGeneration " + generationCounter + " Average Fitness: " + (fitnessAverage / 40));
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //RESETTING INFORMATION WHEN A GENERATION ENDS
        driverCounter++;
        if(driverCounter == 41) {
            try {
                FileWriter bestFileWriter = new FileWriter("C:\\AhuraElite\\best of generation " + generationCounter + ".txt", true);
                FileReader bestFileReader = new FileReader("C:\\AhuraElite\\best of generation " + generationCounter + ".txt");

                BufferedWriter bestBufferedWriter = new BufferedWriter(bestFileWriter);
                BufferedReader bestBufferedReader = new BufferedReader(bestFileReader);

                if (bestBufferedReader.readLine() == null) {
                    bestBufferedWriter.write("***************Elite of Generation " + generationCounter + "***************\n");
                }

                for (int i = 0; i < GAClient.getIndividuals().length; i++){
                    if (GAClient.getIndividuals()[i].getFitnessTotal() > bestOverall.getFitnessTotal()){
                        bestOverall = GAClient.getIndividuals()[i];
                    }
                }

                bestBufferedWriter.write("\n" + "Driver ID: " + bestOverall.getId() +
                        "\n" + "Driver Fitness: " + bestOverall.getFitnessTotal() +
                        "\n" + "Fastest Lap: " + bestOverall.getFitnessObject().getBestLapTime() +
                        "\n" + "Driver Genes: " + Arrays.toString(bestOverall.getAllGenes()));
                bestBufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            generationCounter++;
            setDriverCounter(1);
            damageAverage = 0;
            fitnessAverage = 0;
            distanceAverage = 0;
            topSpeedAverage = 0;

            GAClient.setIndividuals(ga.evolve(GAClient.getIndividuals()));
        }

        MySensorModel.setTopSpeed(0);
        topSpeed = 0;
        episodeCounter++;
        action.restartRace = false;
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
        myPara.setDamage(damage);
        myPara.setTotalTime(totalTime);
    }
    public float[] initAngles()	{
        float[] angles = DriverControllerHelperE6.angles;

        /* set angles as {-90,-75,-60,-45,-30,-20,-15,-10,-5,0,5,10,15,20,30,45,60,75,90} */
        return angles;
    }
    public static int getDriverCounter() {
        return driverCounter;
    }
    public static void setDriverCounter(int driverCounter) {
        GeneticDriverController.driverCounter = driverCounter;
    }
}