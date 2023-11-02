package GeneticAlgorithm;

import ahooraDriver.MySensorModel;

import static ahooraDriver.MySensorModel.getCurrentLapTime;

public class Fitness {

    private double damage;
    private double damageReward;
    private double totalTimeAlive;
    private double distanceTravelled;
    private double distanceReward;
    private double topSpeed;
    private double speedReward;
    private double bestLapTime = 0;
    private double bestLapTimeReward;

    public Fitness(){
        damage = 0;
        totalTimeAlive = 0;
        distanceTravelled = 0;
        topSpeed = 0;
        bestLapTime = 0;
    }

    public double getOverallFitness() {
        double fitness = 0;

        fitness+= getDistanceReward() + getSpeedReward() + getDamageReward() + getBestLapTimeReward();

        return fitness;
    }

    public double getDamage() {
        return damage;
    }
    public void setDamage(double damage) {
        this.damage = damage;
    }
//    public double getTotalTimeAlive() {
//        return totalTimeAlive;
//    }
//    public void setTotalTimeAlive(double totalTimeAlive) {
//        this.totalTimeAlive = totalTimeAlive;
//    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }
    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }
    public double getDistanceReward(){
        return distanceTravelled;
    }

    public void setDistanceReward(double distanceReward) {
        this.distanceReward = distanceReward;
    }
    public double getSpeedReward() {
        return topSpeed * 6;
    }
    public void setSpeedReward(double speedReward) {
        this.speedReward = speedReward;
    }
    public double getTopSpeed() {
        return topSpeed;
    }
    public void setTopSpeed(double topSpeed) {
        this.topSpeed = topSpeed;
    }
    public double getDamageReward() {
        if (MySensorModel.getDamage() >= 5000 && MySensorModel.getDamage() <= 7500){
            damageReward = 5;
        }
        else if (MySensorModel.getDamage() >= 3000 && MySensorModel.getDamage() < 5000){
            damageReward = 10;
        }
        else if (MySensorModel.getDamage() >= 2000 && MySensorModel.getDamage() < 3000){

            damageReward = 25;
        }
        else if (MySensorModel.getDamage() >= 1000 && MySensorModel.getDamage() < 2000){
            damageReward = 100;
        }
        else if (MySensorModel.getDamage() >= 200 && MySensorModel.getDamage() < 1000){
            damageReward = 200;
        }
        else if (MySensorModel.getDamage() <= 199 && MySensorModel.getDamage() > 0){
            damageReward = 250;
        }
        else if (MySensorModel.getDamage() == 0){
            damageReward = 300;
        }
        return damageReward;
    }

    public void setDamageReward(double damageReward) {
        this.damageReward = damageReward;
    }

    public double getBestLapTime() {
        return bestLapTime;
    }

    public void setBestLapTime(double bestLapTime) {
        this.bestLapTime = bestLapTime;
    }

    public double getBestLapTimeReward() {
        if (bestLapTime > 0) {
            return 2500 / bestLapTime;
        }
        return 0;
    }
}
