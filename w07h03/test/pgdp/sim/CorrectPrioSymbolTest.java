package pgdp.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CorrectPrioSymbolTest {
    @Test
    public void TestCorrectPriorities() {
        assertEquals(1,(new Hamster()).priority());
        assertEquals(1,(new Pingu()).priority());
        assertEquals(1,(new Wolf()).priority());
        assertEquals(1, (new MovingCell() {
            @Override
            public boolean canEat(Cell other) {
                return false;
            }

            @Override
            public int foodConsumption() {
                return 0;
            }

            @Override
            public int consumedFood() {
                return 0;
            }

            @Override
            public int reproductionCost() {
                return 0;
            }

            @Override
            public int initialFood() {
                return 0;
            }

            @Override
            public Cell getNew() {
                return null;
            }

            @Override
            public CellSymbol getSymbol() {
                return null;
            }
        }).priority());
        assertEquals(0,(new Plant()).priority());
    }

    @Test
    public void ReturnCorrectSymbol(){
        assertEquals(CellSymbol.PLANT, (new Plant()).getSymbol());
        assertEquals(CellSymbol.WOLF, (new Wolf()).getSymbol());
        assertEquals(CellSymbol.HAMSTER, (new Hamster()).getSymbol());
        assertEquals(CellSymbol.PINGU, (new Pingu()).getSymbol());
    }
}
