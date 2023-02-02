package pgdp.pvm;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestIO implements IO {
    IO wrapper;
    List<String> operations;
    Iterator<String> typeOrder;
    Iterator<Integer> valueOrder;

    /**
     * 
     * @param ioCommands
     */
    public TestIO(String ioCommands) {
        if (ioCommands.contains("ERR")) {
            // Ignore everything after ERR
            ioCommands = ioCommands.substring(0, ioCommands.indexOf("ERR"));
        }

        operations = Arrays
                .stream(ioCommands.split("\\s+"))
                .filter(i -> i.length() > 0)
                .filter(i -> !i.startsWith("M"))
                .toList();
        typeOrder = operations
                .stream()
                .map(i -> i.substring(0, 1).toLowerCase())
                .iterator();
        valueOrder = operations
                .stream()
                .map(i -> Integer.parseInt(i
                        .substring(1)))
                .iterator();

        wrapper = IO.of(
            () -> valueOrder.next(),
            i -> assertEquals(valueOrder.next(), i));
    }

    @Override
    public void write(int i) {
        // If not has next -> error
        if (!valueOrder.hasNext()) {
            fail("Received WRITE call when no more calls were expected");
            return;
        }

        // If next is not write flag
        if (!typeOrder.next().equals("w")) {
            fail("Received wrong call when READ was expected");
            return;
        }
        
        wrapper.write(i);
    }

    @Override
    public int read() {
        // If not has next -> error
        if (!valueOrder.hasNext()) {
            fail("Received READ call when no more calls were expected");
            return 0;
        }

        // If next is not read flag
        if (!typeOrder.next().equals("r")) {
            fail("Received wrong call when READ was expected");
            return 0;
        }

        return wrapper.read();
    }

    /**
     * @return true if no more read and write calls are expected
     */
    public boolean close() {
        return !typeOrder.hasNext();
    }
}
