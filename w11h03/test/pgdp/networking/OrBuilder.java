package pgdp.networking;

import org.opentest4j.AssertionFailedError;

import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

public class OrBuilder implements Runnable {
    private List<Runnable> assertions = new ArrayList<>();
    private String message = null;
    private OrBuilder() {}
    public static OrBuilder assertThat(Runnable assertion) {
        OrBuilder builder = new OrBuilder();
        return builder.or(assertion);
    }

    public OrBuilder or(Runnable assertion) {
        assertions.add(assertion);
        return this;
    }

    public OrBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public void run() {
        List<AssertionFailedError> errors = new ArrayList<>();
        boolean valid = false;
        for (Runnable assertion : assertions) {
            try {
                assertion.run();
                valid = true;
            } catch (AssertionFailedError e) {
                errors.add(e);
            }
        }
        if (!valid) {
            System.err.println("One assertion had to succeed, zero did.");
            if (message != null)
                System.err.println(message);
            System.err.println("\nThe following lines are the reasons why all assertions failed.");
            for (int i = 0; i < assertions.size(); i++) {
                AssertionFailedError error = errors.get(i);
                System.err.println((i+1) + ": " + error.getMessage());
            }
            fail();
        }
    }
}
