package factories;

import AbstractClasses.ProblemDomain;
import heuristics.Algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AlgorithmFactory {

    private static ArrayList<Integer> HEURISTICS_NUMBERS;
    private static ArrayList<Algorithm> algorithms;
    private static ArrayList<Algorithm> eliteAlgorithms;
    private static Random rnd;

    /* produces an array of all permutations of algorithms */
    private static void generateAlgorithms() {
        int index = 0;
        int heuristics[];
        Algorithm algorithm;
        rnd = new Random();
        algorithms = new ArrayList<>();

        for (int i : HEURISTICS_NUMBERS) {
            for (int j: HEURISTICS_NUMBERS) {
                for (int k: HEURISTICS_NUMBERS) {

                    heuristics = new int[3];
                    heuristics[0] = i;
                    heuristics[1] = j;
                    heuristics[2] = k;

                    algorithm = new Algorithm(index, heuristics);
                    algorithms.add(algorithm);
                    ++index;
                }
            }
        }
    }

    /* return algorithms */
    public static ArrayList<Algorithm> getAlgorithms() {
        if (algorithms == null) {
            generateAlgorithms();
        }
        return algorithms;
    }

    /* returns algorithms in random order */
    static ArrayList<Algorithm> getRandomAlgorithms() {
        if (algorithms == null) {
            generateAlgorithms();
        }
        ArrayList<Algorithm> randomAlgorithms = new ArrayList<>(algorithms);
        Collections.shuffle(randomAlgorithms, rnd);

        return randomAlgorithms;
    }

    /* returns algorithms ordered by fitness */
    static ArrayList<Algorithm> getEliteAlgorithms(String problemType) throws IOException {
        if (algorithms == null) {
            generateAlgorithms();
        }
        if (eliteAlgorithms == null) {
            eliteAlgorithms = new ArrayList<>();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(problemType+".csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            int counter = 0;

            ArrayList<Integer> algOrder = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                algOrder.add(Integer.parseInt(line));
                counter++;
            }

            for (int algIndex : algOrder) {
                eliteAlgorithms.add(algorithms.get(algIndex));
            }
        }
        return eliteAlgorithms;
    }

   public static void setProblemHeuristics(ProblemDomain prob){
        HEURISTICS_NUMBERS = new ArrayList<>();

        ArrayList<ProblemDomain.HeuristicType> heuristicTypes = new ArrayList<>();
        heuristicTypes.add(ProblemDomain.HeuristicType.MUTATION);
        heuristicTypes.add(ProblemDomain.HeuristicType.RUIN_RECREATE);
        heuristicTypes.add(ProblemDomain.HeuristicType.LOCAL_SEARCH);

        for (ProblemDomain.HeuristicType h: heuristicTypes) {
            for (int i : prob.getHeuristicsOfType(h)) {
                HEURISTICS_NUMBERS.add(i);
            }
        }
        Collections.sort(HEURISTICS_NUMBERS);
    }

}