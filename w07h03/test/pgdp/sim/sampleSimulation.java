package pgdp.sim;

import java.util.Random;
import org.junit.jupiter.api.Test;

public class sampleSimulation {
    @Test
    public void run() {
        SimConfig.plantReproductionCost = 100;
        SimConfig.plantMaxGrowth = 101;
        SimConfig.plantMinGrowth = 30;

        SimConfig.hamsterFoodConsumption = 13;
        SimConfig.hamsterConsumedFood = 66;
        SimConfig.hamsterReproductionCost = 100;
        SimConfig.hamsterInitialFood = 66;

        SimConfig.pinguFoodConsumption = 25;
        SimConfig.pinguConsumedFood = 50;
        SimConfig.pinguReproductionCost = 100;
        SimConfig.pinguInitialFood = 66;

        SimConfig.wolfFoodConsumption = 15;
        SimConfig.wolfConsumedFood = 45;
        SimConfig.wolfReproductionCost = 100;
        SimConfig.wolfInitialFood = 75;

        SimConfig.numInitialPlant = 20;
        SimConfig.numInitialHamster = 20;
        SimConfig.numInitialPingu = 20;
        SimConfig.numInitialWolf = 20;

        SimConfig.width = 40;
        SimConfig.height = 40;

        int ticks = 10; // Wie viele Durchläufe simuliert werden sollen

        Cell[] cells = new Cell[SimConfig.width*SimConfig.height];
        Simulation sim = new Simulation(cells, SimConfig.width, SimConfig.height);

        Random num = new Random(12);

        for (int i = 0; i < SimConfig.numInitialHamster; i++) {
            int pos = num.nextInt(0, SimConfig.width*SimConfig.height);
            cells[pos] = new Hamster();
        }
        for (int i = 0; i < SimConfig.numInitialPlant; i++) {
            int pos = num.nextInt(0, SimConfig.width*SimConfig.height);
            cells[pos] = new Plant();
        }
        for (int i = 0; i < SimConfig.numInitialPingu; i++) {
            int pos = num.nextInt(0, SimConfig.width*SimConfig.height);
            cells[pos] = new Pingu();
        }
        for (int i = 0; i < SimConfig.numInitialWolf; i++) {
            int pos = num.nextInt(0, SimConfig.width*SimConfig.height);
            cells[pos] = new Wolf();
        }


        for (int i = 1; i <= ticks; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (j % SimConfig.width == 0) {
                    System.out.println();
                }
                System.out.print("|" + createString(sim.getCells()[j]));
            }
            System.out.print("\n------------------------------------ " + i + " ----------------------------------------");
            sim.tick();
        }
    }

    private static String createString(Cell cell) {
        if (cell == null) return " ";
        CellSymbol symbol = cell.getSymbol();
        if (symbol == CellSymbol.PLANT) return "T";
        if (symbol == CellSymbol.PINGU) return "P";
        if (symbol == CellSymbol.HAMSTER) return "H";
        if (symbol == CellSymbol.WOLF) return "W";
        return "X";
    }
}