package GeneticAlgorithm;

import java.util.Random;

public class Mutation {

    private final double mutationRate;

    private final Random random = new Random();

    public Mutation() {
        this.mutationRate = 0.02;
    }

    public void scrambleMutation(IndividualTest[] individualsTest) {

        for (int i = 10; i < individualsTest.length; i++) {

            if (this.mutationRate >= Math.random()) {
                System.out.println("MUTATION HAS OCCURRED ON INDIVIDUAL " + individualsTest[i].getId() + " - DRIVER " + i);
                double[] mutatedGenes = individualsTest[i].getAllGenes();

                int totalParameters = individualsTest[i].getAllGenes().length;

                for (int j = 0; j < 3; j++) {
                    int firstRandomNum = random.nextInt(totalParameters);
                    int secondRandomNum = random.nextInt(totalParameters);

                    while (firstRandomNum >= secondRandomNum) {
                        firstRandomNum = random.nextInt(totalParameters);
                        secondRandomNum = random.nextInt(totalParameters);
                    }

                    for (int k = 0; k < 10; k++) {
                        int s1 = random.nextInt(secondRandomNum + 1 - firstRandomNum) + firstRandomNum;
                        int s2 = random.nextInt(secondRandomNum + 1 - firstRandomNum) + firstRandomNum;
                        double a = mutatedGenes[s1];
                        mutatedGenes[s1] = mutatedGenes[s2];
                        mutatedGenes[s2] = a;
                    }
                }
                individualsTest[i].setAllGenes(mutatedGenes);
            }
        }
    }
}
