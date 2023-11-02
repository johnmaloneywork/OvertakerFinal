package GeneticAlgorithm;

import java.util.Arrays;
import java.util.Random;

public class IndividualTest {

    /*
     * 20 Genes to fill
     * Defining the genes
     * Speed Genes | Opponent Sensors Genes | Track Sensor Genes
     *
     * Speed Genes: MySensorModel.getSpeed() --> Speed of the Genetic Bot
     * Speed Genes: MySensorMode.getRelativeSpeed() --> Speed of the Defending Bot
     *
     * Opponent Sensor Genes: opponentSensors[15] - opponentSensors[23]
     *
     * Track Sensor Genes: trackEdgeSensors[0], trackEdgeSensors[4], trackEdgeSensors[7],
     *					   trackEdgeSensors[8], trackEdgeSensors[9], trackEdgeSensors[10],
     *					   trackEdgeSensors[11], trackEdgeSensors[14], trackEdgeSensors[18]
     *
     * Total of 20 Genes
     */

    /* Genes that make up each chromosome */
    private double[] speedGenes;
    private double[] steerGenes;
    private double[] allGenes; //14 Genes
    private Population population;

    /* Randomising evolution of genes */
    private static final Random rand = new Random();

    /* Assigning initial fitness of each chromosome */
    private Fitness fitnessObject;
    private double fitnessTotal;

    /* ID for each Individual */
    private String id;

    public IndividualTest(){

        this.id = generateIdentification();
        this.fitnessTotal = 0;
    }

    /* Constructor */
    public IndividualTest(double[] allGenes){
        this.steerGenes = new double[7];
        this.speedGenes = new double[15];
        this.allGenes = new double[steerGenes.length + speedGenes.length];

        for (int i = 0; i < this.speedGenes.length; i++){
            this.speedGenes[i] = rand.nextDouble();
            this.allGenes[i] = this.speedGenes[i];
        }

        int k = this.speedGenes.length;
        for (int j = 0; j < this.steerGenes.length; j++){
            this.steerGenes[j] = rand.nextDouble();
            this.allGenes[k] = steerGenes[j];
            k++;
        }

//        for (int l = 0; l < speedGenes.length; l++){
//            this.allGenes[k] = speedGenes[l];
//            k++;
//        }

//        System.arraycopy(steerGenes, 0, this.allGenes, 0, this.steerGenes.length);
//        System.out.println(Arrays.toString(allGenes));
//
//        //System.arraycopy(speedGenes, 0, this.allGenes, allGenes.length - steerGenes.length, 15);

        this.id = generateIdentification();

        this.fitnessTotal = 0;
    }

    public String toString(){
        String chromosomeAsString = "";

        chromosomeAsString += "\nDRIVER: " + getId();
        chromosomeAsString += "\nGENES: " + Arrays.toString(getAllGenes());

        return chromosomeAsString;
    }

    public String generateIdentification(){
        int leftLimit = 48;
        int rightLimit = 122;
        int identificationStringLength = 10;

        String id = rand.ints(leftLimit, rightLimit)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(identificationStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return id;
    }

    public double[] getAllGenes() {
        return allGenes;
    }

    public void setAllGenes(double[] allGenes) {
        this.allGenes = allGenes;
    }

    public double[] getSpeedGenes() {
        return speedGenes;
    }

    public void setSpeedGenes(double[] speedGenes) {
        this.speedGenes = speedGenes;
    }

    public double[] getSteerGenes() {
        return steerGenes;
    }

    public void setSteerGenes(double[] steerGenes) {
        this.steerGenes = steerGenes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public Fitness getFitnessObject() {
        return fitnessObject;
    }

    public void setFitnessObject(Fitness fitnessObject) {
        this.fitnessObject = fitnessObject;
    }

    public double getFitnessTotal() {
        return fitnessTotal;
    }

    public void setFitnessTotal(double fitnessTotal) {
        this.fitnessTotal = fitnessTotal;
    }

}
