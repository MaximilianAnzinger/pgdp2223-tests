package pgdp.mocks;

import pgdp.security.SignalPost;

public class SignalPostMock extends SignalPost implements Mock {
    public SignalPostMock(int postNumber) {
        super(postNumber);
    }

    @Override
    public boolean up(String s) {
        trackMethodCall("up", s);
        return false;
    }

    @Override
    public boolean down(String s) {
        trackMethodCall("down", s);
        return false;
    }
}
