import org.junit.jupiter.api.Test;
import pgdp.pvm.IO;
import pgdp.pvm.PVMError;
import pgdp.pvm.PVMParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PinguJVMVerifyTest {

    @Test
    void shouldThrowPVMErrorForInsufficientArithmeticStackSize(){
        Stream<String> invalid = Stream.of("ALLOC 2",
                "CONST 0",
                "STORE 0",
                "CONST 1",
                "STORE 1",
                "LOAD 0",
                "ADD");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 7: Not enough variables on the stack for ADD";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorForInsufficientBooleanStackSize(){
        Stream<String> invalid = Stream.of("ALLOC 2",
                "TRUE",
                "STORE 0",
                "FALSE",
                "STORE 1",
                "LOAD 0",
                "AND");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 7: Not enough variables on the stack for AND";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorForInsufficientComparisonStackSize(){
        Stream<String> invalid = Stream.of("ALLOC 2",
                "CONST 1",
                "STORE 0",
                "CONST 2",
                "STORE 1",
                "LOAD 0",
                "EQ");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 7: Not enough variables on the stack for EQ";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorWhenNegatingBooleanWithNEG(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "TRUE",
                "STORE 0",
                "LOAD 0",
                "NEG");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 5: First Argument must be INT at NEG";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorForInsufficientArithmeticNegationStackSize(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 1",
                "STORE 0",
                "NEG");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 4: Not enough variables on the stack for NEG";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorForInsufficientBooleanNegationStackSize(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 1",
                "STORE 0",
                "NOT");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 4: Not enough variables on the stack for NOT";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorWhenNegatingIntWithNOT(){
        Stream<String> invalid = Stream.of("ALLOC 2",
                "CONST 3",
                "STORE 0",
                "LOAD 0",
                "NOT");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 5: First Argument must be BOOL at NOT";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorForIncompatibleArithmeticTypes(){
        Stream<String> invalid = Stream.of("ALLOC 2",
                "TRUE",
                "STORE 0",
                "CONST 3",
                "STORE 1",
                "LOAD 0",
                "LOAD 1",
                "ADD");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 8: First Argument must be INT at ADD";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorForIncompatibleBooleanTypes(){
        Stream<String> invalid = Stream.of("ALLOC 2",
                "TRUE",
                "STORE 0",
                "CONST 3",
                "STORE 1",
                "LOAD 0",
                "LOAD 1",
                "OR");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 8: First Argument must be BOOL at OR";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorForIncompatibleComparisonTypes(){
        Stream<String> invalid = Stream.of("ALLOC 2",
                "TRUE",
                "STORE 0",
                "CONST 3",
                "STORE 1",
                "LOAD 0",
                "LOAD 1",
                "NEQ");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 8: First Argument must be INT at NEQ";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorWhenWritingBoolean(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "TRUE",
                "STORE 0",
                "LOAD 0",
                "WRITE");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 5: First Argument must be INT at WRITE";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
        // cannot test for message here because there is no preset PVMError for this situation
    void shouldThrowPVMErrorWhenLoadingFromNegativeAddress(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 4",
                "STORE 0",
                "LOAD -3");

        assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
    }

    @Test
    void shouldThrowPVMErrorWhenLoadingFromOOBAddress(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 4",
                "STORE 0",
                "LOAD 1");

        assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
    }

    @Test
    void shouldThrowPVMErrorWhenLoadingUndefined(){ // shouldn't be possible according to the docs
        Stream<String> invalid = Stream.of("ALLOC 2",
                "CONST 1",
                "STORE 0",
                "LOAD 1");

        assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
    }

    @Test
    void shouldThrowPVMErrorWhenStoringToNegativeAddress(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 4",
                "STORE -3");

        assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
    }

    @Test
    void shouldThrowPVMErrorWhenStoringToOOBAddress(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 4",
                "STORE 1");

        assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
    }

    @Test
    void shouldThrowPVMErrorWhenPOPStackSizeInsufficient(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 1",
                "STORE 0",
                "POP");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 4: Not enough variables on the stack for POP";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorWhenDUPStackSizeInsufficient(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 1",
                "STORE 0",
                "DUP");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 4: Not enough variables on the stack for DUP";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorWhenFJUMPStackSizeInsufficient(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 1",
                "STORE 0",
                "FJUMP here",
                "here:",
                "HALT");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 4: Not enough variables on the stack for FJUMP";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowPVMErrorWhenFJUMPNotBoolean(){
        Stream<String> invalid = Stream.of("ALLOC 1",
                "CONST 1",
                "STORE 0",
                "LOAD 0",
                "FJUMP here",
                "here:",
                "HALT");

        Exception exception = assertThrows(PVMError.class, () -> new PVMParser(invalid).run(IO.system()));
        String expectedMessage = "Error in line 5: First Argument must be BOOL at FJUMP";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
