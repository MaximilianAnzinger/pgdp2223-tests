package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pingulogy.RecursivePingulogy;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class RecursivePingulogyTest {

    private final long[][] test = {{0, 1L}, {1, 1L}, {2, 2L}, {3, 3L}, {4, 3L}, {5, 4L}, {6, 7L}, {7, 9L}, {8, 10L}, {9, 15L}, {10, 23L}, {11, 28L}, {12, 35L}, {13, 53L}, {14, 74L}, {15, 91L}, {16, 123L}, {17, 180L}, {18, 239L}, {19, 305L}, {20, 426L}, {21, 599L}, {22, 783L}, {23, 1036L}, {24, 1451L}, {25, 1981L}, {26, 2602L}, {27, 3523L}, {28, 4883L}, {29, 6564L}, {30, 8727L}, {31, 11929L}, {32, 16330L}, {33, 21855L}, {34, 29383L}, {35, 40188L}, {36, 54515L}, {37, 73093L}, {38, 98954L}, {39, 134891L}, {40, 182123L}, {41, 245140L}, {42, 332799L}, {43, 451905L}, {44, 609386L}, {45, 823079L}, {46, 1117503L}, {47, 1513196L}, {48, 2041851L}, {49, 2763661L}, {50, 3748202L}, {51, 5068243L}, {52, 6847363L}, {53, 9275524L}, {54, 12564647L}, {55, 16983849L}, {56, 22970250L}, {57, 31115695L}, {58, 42113143L}, {59, 56937948L}, {60, 77056195L}, {61, 104344533L}, {62, 141164234L}, {63, 190932091L}, {64, 258456923L}, {65, 349853300L}, {66, 473260559L}, {67, 640321105L}, {68, 866767146L}, {69, 1172967159L}, {70, 1586842223L}, {71, 2147409356L}, {72, 2906501451L}, {73, 3932776541L}, {74, 5321093802L}, {75, 7201320163L}, {76, 9745779443L}, {77, 13186646884L}, {78, 17843507767L}, {79, 24148419769L}, {80, 32678205770L}, {81, 44216801535L}, {82, 59835435303L}, {83, 80975045308L}, {84, 109573213075L}, {85, 148269038373L}, {86, 200645915914L}, {87, 271523303691L}, {88, 367415464523L}, {89, 497183992660L}, {90, 672815135519L}, {91, 910462071905L}, {92, 1232014921706L}, {93, 1667183120839L}, {94, 2256092342943L}, {95, 3052939065516L}, {96, 4131212964251L}, {97, 5590458584621L}, {98, 7565123751402L}, {99, 10237091095283L}, {100, 13852884513123L}, {101, 18746040920644L}, {102, 25367338598087L}, {103, 34327066703689L}, {104, 46451809946890L}, {105, 62859420439375L}, {106, 85061743899863L}, {107, 115105943354268L}, {108, 155763040333155L}, {109, 210780584778613L}, {110, 285229431153994L}, {111, 385974927041691L}, {112, 522306665444923L}, {113, 706790600711220L}, {114, 956433789349679L}, {115, 1294256519528305L}, {116, 1751403931601066L}, {117, 2370014990772119L}, {118, 3207124098227663L}, {119, 4339916970657676L}, {120, 5872822853974251L}, {121, 7947154079771901L}, {122, 10754165167113002L}, {123, 14552656795289603L}, {124, 19692799787720403L}, {125, 26648473326656804L}, {126, 36060987129515607L}, {127, 48798113378299609L}, {128, 66034072902097610L}, {129, 89357933782829215L}, {130, 120920087637330823L}, {131, 163630299658696828L}, {132, 221426079587024435L}, {133, 299635955202989253L}, {134, 405470474933358474L}, {135, 548686678904418091L}, {136, 742488114377038123L}, {137, 1004742385339336980L}, {138, 1359627628771135039L}, {139, 1839861472185874305L}, {140, 2489718614093413226L}, {141, 3369112399449808999L}, {142, 4559116729728144383L}, {143, 6169441558465161836L}, {144, 8348549627636635451L}};
    private final long[][] test2 = {{0, 1}, {1, 2L}, {2, 2L}, {3, 2L}, {4, 4L}, {5, 6L}, {6, 6L}, {7, 8L}, {8, 14L}, {9, 18L}, {10, 20L}, {11, 30L}, {12, 46L}, {13, 56L}, {14, 70L}, {15, 106L}, {16, 148L}, {17, 182L}, {18, 246L}, {19, 360L}, {20, 478L}, {21, 610L}, {22, 852L}, {23, 1198L}, {24, 1566L}, {25, 2072L}, {26, 2902L}, {27, 3962L}, {28, 5204L}, {29, 7046L}, {30, 9766L}, {31, 13128L}, {32, 17454L}, {33, 23858L}, {34, 32660L}, {35, 43710L}, {36, 58766L}, {37, 80376L}, {38, 109030L}, {39, 146186L}, {40, 197908L}, {41, 269782L}, {42, 364246L}, {43, 490280L}, {44, 665598L}, {45, 903810L}, {46, 1218772L}, {47, 1646158L}, {48, 2235006L}, {49, 3026392L}, {50, 4083702L}, {51, 5527322L}, {52, 7496404L}, {53, 10136486L}, {54, 13694726L}, {55, 18551048L}, {56, 25129294L}, {57, 33967698L}, {58, 45940500L}, {59, 62231390L}, {60, 84226286L}, {61, 113875896L}, {62, 154112390L}, {63, 208689066L}, {64, 282328468L}, {65, 381864182L}, {66, 516913846L}, {67, 699706600L}, {68, 946521118L}, {69, 1280642210L}, {70, 1733534292L}, {71, 2345934318L}, {72, 3173684446L}, {73, 4294818712L}, {74, 5813002902L}, {75, 7865553082L}, {76, 10642187604L}, {77, 14402640326L}, {78, 19491558886L}, {79, 26373293768L}, {80, 35687015534L}, {81, 48296839538L}, {82, 65356411540L}, {83, 88433603070L}, {84, 119670870606L}, {85, 161950090616L}, {86, 219146426150L}, {87, 296538076746L}, {88, 401291831828L}, {89, 543046607382L}, {90, 734830929046L}, {91, 994367985320L}, {92, 1345630271038L}, {93, 1820924143810L}, {94, 2464029843412L}, {95, 3334366241678L}, {96, 4512184685886L}, {97, 6105878131032L}, {98, 8262425928502L}, {99, 11180917169242L}, {100, 15130247502804L}, {101, 20474182190566L}, {102, 27705769026246L}, {103, 37492081841288L}, {104, 50734677196174L}, {105, 68654133407378L}, {106, 92903619893780L}, {107, 125718840878750L}, {108, 170123487799726L}, {109, 230211886708536L}, {110, 311526080666310L}, {111, 421561169557226L}, {112, 570458862307988L}, {113, 771949854083382L}, {114, 1044613330889846L}, {115, 1413581201422440L}, {116, 1912867578699358L}, {117, 2588513039056610L}, {118, 3502807863202132L}, {119, 4740029981544238L}, {120, 6414248196455326L}, {121, 8679833941315352L}, {122, 11745645707948502L}, {123, 15894308159543802L}, {124, 21508330334226004L}, {125, 29105313590579206L}, {126, 39385599575440806L}, {127, 53296946653313608L}, {128, 72121974259031214L}, {129, 97596226756599218L}, {130, 132068145804195220L}, {131, 178715867565658430L}, {132, 241840175274661646L}, {133, 327260599317393656L}, {134, 442852159174048870L}, {135, 599271910405978506L}, {136, 810940949866716948L}, {137, 1097373357808836182L}, {138, 1484976228754076246L}};
    private final int[][] pinguCode = {{0, 0, 0}, {0, 1, 1}, {0, 2, 2}, {0, 3, 3}, {0, 4, 4}, {0, 5, 5}, {0, 6, 6}, {0, 7, 7}, {0, 8, 8}, {0, 9, 9}, {1, 0, 0}, {1, 1, 1}, {1, 2, 3}, {1, 3, 4}, {1, 4, 6}, {1, 5, 7}, {1, 6, 9}, {1, 7, 10}, {1, 8, 12}, {1, 9, 13}, {2, 0, 2}, {2, 1, 1}, {2, 2, 2}, {2, 3, 3}, {2, 4, 4}, {2, 5, 3}, {2, 6, 6}, {2, 7, 8}, {2, 8, 8}, {2, 9, 7}, {3, 0, 2}, {3, 1, 1}, {3, 2, 3}, {3, 3, 4}, {3, 4, 6}, {3, 5, 7}, {3, 6, 9}, {3, 7, 11}, {3, 8, 12}, {3, 9, 13}, {4, 0, 4}, {4, 1, 5}, {4, 2, 4}, {4, 3, 5}, {4, 4, 6}, {4, 5, 9}, {4, 6, 9}, {4, 7, 9}, {4, 8, 10}, {4, 9, 14}, {5, 0, 4}, {5, 1, 3}, {5, 2, 7}, {5, 3, 6}, {5, 4, 8}, {5, 5, 9}, {5, 6, 11}, {5, 7, 11}, {5, 8, 14}, {5, 9, 15}, {6, 0, 6}, {6, 1, 5}, {6, 2, 7}, {6, 3, 5}, {6, 4, 7}, {6, 5, 7}, {6, 6, 9}, {6, 7, 10}, {6, 8, 11}, {6, 9, 13}, {7, 0, 6}, {7, 1, 5}, {7, 2, 7}, {7, 3, 8}, {7, 4, 11}, {7, 5, 9}, {7, 6, 11}, {7, 7, 13}, {7, 8, 15}, {7, 9, 17}, {8, 0, 8}, {8, 1, 10}, {8, 2, 8}, {8, 3, 10}, {8, 4, 10}, {8, 5, 12}, {8, 6, 11}, {8, 7, 15}, {8, 8, 14}, {8, 9, 16}, {9, 0, 8}, {9, 1, 7}, {9, 2, 12}, {9, 3, 10}, {9, 4, 12}, {9, 5, 14}, {9, 6, 16}, {9, 7, 15}, {9, 8, 18}, {9, 9, 19}};

    @Test
    @DisplayName("Beispiel Artemis")
    void pinguSequenceRecPositiv() {
        testpinguSequenceRecPositiv(test, 1, 1, 2);
    }

    @Test
    @DisplayName("Eigenes Beispiel")
    void pinguSequenceRecPositivTestZwei() {
        testpinguSequenceRecPositiv(test2, 1, 2, 2);
    }

    @Test
    @DisplayName("Beispiel Artemis adapted to negativ input")
    void testpinguSequenzRecNegativ() {
        testpinguSequenzRecNegativ(test, 1, 1, 2);
    }

    @Test
    @DisplayName("Eigenes Beispiel")
    void testpinguSequenzRecNegativTestZwei() {
        testpinguSequenzRecNegativ(test2, 1, 2, 2);
    }

    @Test
    @DisplayName("Teste p=(0,0,0)")
    void testPinguSequenzVektorAusNullen() {
        testpinguSequenceRecPositiv(new long[][]{{144, 0L}}, 0, 0, 0);
    }

    @Test
    @DisplayName("Test timelimit")
    void testTimeLimit() {
        Assertions.assertTimeout(Duration.ofSeconds(1), () -> RecursivePingulogy.pinguSequenceRec(40, 1, 1, 2), "Nicht Einhaltung des Zeitlimits von einer Sekunde");
        Assertions.assertTimeout(Duration.ofSeconds(1), () -> RecursivePingulogy.pinguSequenceRec(144, 1, 1, 2), "Nicht Einhaltung des Zeitlimits von einer Sekunde");
    }

    @Test
    @DisplayName("Test reset static variablen")
    void testReset() {
        assertNotEquals(RecursivePingulogy.pinguSequenceRec(40, 1, 1, 2), RecursivePingulogy.pinguSequenceRec(40, 2, 2, 2));
    }

    private void testpinguSequenceRecPositiv(long[][] array, int p0, int p1, int p2) {
        for (long[] i : array) {
            assertEquals(i[1], RecursivePingulogy.pinguSequenceRec((int) i[0], p0, p1, p2));
        }
    }

    private void testpinguSequenzRecNegativ(long[][] array, int p0, int p1, int p2) {
        for (int i = 1; i < 122; i++) {
            long output = RecursivePingulogy.pinguSequenceRec(-1 * (int) array[i][0], p0, p1, p2);
            assertEquals(array[i][1] * 2, output);
        }
    }

    @Test
    @DisplayName("Beispiel Artemis")
    void pinguF() {
        RecursivePingulogy r = new RecursivePingulogy();
        int[] ergebnis = {1, 1, 2, 2, 3, 3, 4, 5, 5, 6};
        for (int i = 0; i < ergebnis.length; i++) {
            assertEquals(ergebnis[i], r.pinguF(i));
        }
    }

    @Test
    @DisplayName("Beispiel Artemis")
    void pinguM() {
        RecursivePingulogy r = new RecursivePingulogy();
        int[] ergebnis = {0, 0, 1, 2, 2, 3, 4, 4, 5, 6};
        for (int i = 0; i < ergebnis.length; i++) {
            assertEquals(ergebnis[i], r.pinguM(i));
        }
    }

    @Test
    @DisplayName("Beispiel Artemis")
    void pinguCode() {
        assertEquals(0, RecursivePingulogy.pinguCode(0, 0));
        for (int[] i : pinguCode) {
            assertEquals(i[2], RecursivePingulogy.pinguCode(i[0], i[1]));
        }

    }

    @Test
    @DisplayName("Verschiedene Varianten")
    void pinguDNA() {
        assertEquals("GCACTCGAGA", RecursivePingulogy.pinguDNA(21, 25));
        assertEquals("", RecursivePingulogy.pinguDNA(0, 0));
        assertEquals("AA", RecursivePingulogy.pinguDNA(0, 2));
        assertEquals("TT", RecursivePingulogy.pinguDNA(2, 0));
        assertEquals("AGA", RecursivePingulogy.pinguDNA(1, 3));
        assertEquals("TGT", RecursivePingulogy.pinguDNA(3, 1));
        assertEquals("GC", RecursivePingulogy.pinguDNA(1, 1));
        assertEquals("GCGC", RecursivePingulogy.pinguDNA(3, 3));
        assertEquals("ATCGA", RecursivePingulogy.pinguDNA(2, 4));
        assertEquals("TACGT", RecursivePingulogy.pinguDNA(4, 2));
        assertEquals("GCGC", RecursivePingulogy.pinguDNA(2, 2));
        assertEquals("GCGCGC", RecursivePingulogy.pinguDNA(4, 4));
        assertEquals("ATC", RecursivePingulogy.pinguDNA(1, 2));
        assertEquals("TAC", RecursivePingulogy.pinguDNA(2, 1));
        assertEquals("AATCTCGAAC", RecursivePingulogy.pinguDNA(12, 33));
    }

    @Test
    @DisplayName("pinguDNA mit Integer.MAX_VALUE")
    void pinguDNALarge() {
        assertEquals("GCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGCGC",
                RecursivePingulogy.pinguDNA(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertEquals("TTTTTTTTTTTTTTTTTTTTTTTTTGTTCGTTCGTTC", RecursivePingulogy.pinguDNA(Integer.MAX_VALUE, 42));
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAAAGAACGAACGAAC", RecursivePingulogy.pinguDNA(42, Integer.MAX_VALUE));
    }
}
