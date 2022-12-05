package pgdp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class LightPanelTest {

    @Test
    public void testUpOneStepIncUp(){
        LightPanel l = new LightPanel(1);
        assertEquals("", l.getDepiction());
        assertEquals(0, l.getLevel());
        l.up("green");
        assertEquals("green", l.getDepiction());
        assertEquals(1, l.getLevel());
        l.up("blue");
        assertEquals("blue", l.getDepiction());
        assertEquals(1, l.getLevel());
        l.up("yellow");
        assertEquals("yellow", l.getDepiction());
        assertEquals(2, l.getLevel());
        l.up("doubleYellow");
        assertEquals("doubleYellow", l.getDepiction());
        assertEquals(3, l.getLevel());
        l.up("[SC]");
        assertEquals("[SC]", l.getDepiction());
        assertEquals(3, l.getLevel());
        l.up("red");
        assertEquals("red", l.getDepiction());
        assertEquals(4, l.getLevel());
        l.up("end");
        assertEquals("yellow", l.getDepiction());
        assertEquals(5, l.getLevel());
    }

    @Test
    public void testUpSameTypeTwiceUp(){
        // should not change anything and return false
        LightPanel l = new LightPanel(1);
        boolean a = l.up("green");
        assertEquals(true, a);
        assertEquals(1, l.getLevel());
        assertEquals("green", l.getDepiction());
        a = l.up("green");
        assertEquals(false, a);
        assertEquals(1, l.getLevel());
        assertEquals("green", l.getDepiction());
    }

    @Test
    public void testUpOneStepDecUp(){
        LightPanel l = new LightPanel(1);
        String[] upTypes = l.getUpTypes();
        l.up("end");
        for (int i = upTypes.length - 1; i >= 0; i--) {
            boolean a = l.up(upTypes[i]);
            assertEquals(false, a);
            assertEquals("yellow", l.getDepiction());
            assertEquals(5, l.getLevel());
        }
    }

    @Test
    public void testDecInSameLevelUp(){
        LightPanel l = new LightPanel(1);
        boolean worked = l.up("blue");
        assertEquals(true, worked);
        assertEquals("blue", l.getDepiction());
        assertEquals(1, l.getLevel());
        worked = l.up("green");
        assertEquals(false, worked);
        assertEquals("blue", l.getDepiction());
        assertEquals(1, l.getLevel());
        
    }

    @Test
    public void testBullshitUp(){
        LightPanel l = new LightPanel(1);
        boolean worked = l.up("blabla");
        assertEquals(false, worked);
        assertEquals("", l.getDepiction());
        assertEquals(0, l.getLevel());
    }

    public void assertClear(boolean worked, LightPanel l){
        assertEquals(worked, l.down("clear"));
        assertEquals("", l.getDepiction());
        assertEquals(0, l.getLevel());
    }

    @Test
    public void testClearDown(){
        LightPanel l = new LightPanel(1);
        assertClear(false, l);

        l.up("green");
        assertClear(true, l);
        l.up("blue");
        assertClear(true, l);
        l.up("yellow");
        assertClear(true, l);
        l.up("doubleYellow");
        assertClear(true, l);
        l.up("[SC]");
        assertClear(true, l);
        l.up("red");
        assertClear(true, l);
        l.up("end");
        assertClear(true, l);
    }

    public void assertNoChangeDown(LightPanel l, String type){
        String priorDepiction = l.getDepiction();
        int priorLevel = l.getLevel();
        boolean worked = l.down(type);
        assertEquals(false, worked);
        assertEquals(priorDepiction, l.getDepiction());
        assertEquals(priorLevel, l.getLevel());
    }

    public void assertClearedDown(LightPanel l, String type){
        boolean worked = l.down(type);
        assertEquals(true, worked);
        assertEquals("", l.getDepiction());
        assertEquals(0, l.getLevel());
    }

    @Test
    public void testGreenBlueDown(){
        LightPanel l = new LightPanel(1);
        assertNoChangeDown(l, "blabla");
        assertNoChangeDown(l, "green");
        assertNoChangeDown(l, "blue");

        l.up("green");
        assertNoChangeDown(l, "blabla");
        assertClearedDown(l, "green");

        l.up("blue");
        assertNoChangeDown(l, "blabla");
        assertClearedDown(l, "blue");

        l.up("green");
        l.up("blue");
        assertNoChangeDown(l, "blabla");
        assertNoChangeDown(l, "green");
        assertClearedDown(l, "blue");
    }

    public void assertGreen(LightPanel l, String type){
        boolean worked = l.down(type);
        assertEquals(true, worked);
        assertEquals("green", l.getDepiction());
        assertEquals(1, l.getLevel());
    }

    @Test
    public void testDangerDown(){
        LightPanel l = new LightPanel(1);
        assertNoChangeDown(l, "blabla");
        assertNoChangeDown(l, "danger");

        l.up("green");
        assertNoChangeDown(l, "blabla");
        assertNoChangeDown(l, "danger");

        l.up("blue");
        assertNoChangeDown(l, "blabla");
        assertNoChangeDown(l, "danger");

        l.up("green");
        l.up("blue");
        assertNoChangeDown(l, "blabla");
        assertNoChangeDown(l, "danger");

        l.up("yellow");
        assertNoChangeDown(l, "blabla");
        assertGreen(l, "danger");

        l.up("doubleYellow");
        assertNoChangeDown(l, "blabla");
        assertGreen(l, "danger");

        l.up("[SC]");
        assertNoChangeDown(l, "blabla");
        assertGreen(l, "danger");

        l.up("red");
        assertNoChangeDown(l, "blabla");
        assertGreen(l, "danger");

        l.up("end");
        assertNoChangeDown(l, "blabla");
        assertNoChangeDown(l, "danger");
    }
}
