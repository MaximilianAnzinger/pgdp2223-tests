package pgdp.security;

import org.junit.jupiter.api.Test;

public class SignalPostTest {
    @Test
    void toStringShouldReturnCorrectString() {
        SignalPost signalPost = new SignalPost(2) {
            @Override
            public boolean up(String s) {
                return false;
            }

            @Override
            public boolean down(String s) {
                return false;
            }
        };
        signalPost.setLevel(1);
        signalPost.setDepiction("depiction");
        assert signalPost.toString().equals("Signal Post 2: 1 depiction");
    }
}
