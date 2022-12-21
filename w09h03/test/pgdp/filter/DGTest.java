package pgdp.filter;

import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

public class DGTest {

    /*
    HINWEISE:

    - STELLE DIE ITERATOR CLASS VON VIDEOCONTAINER AUF PUBLIC
    - DAS VIDEO NOOT.MP4 MUSS KORREKT HERUNTERGELADEN UND IM KORREKTEN ORDNER SEIN (wie für das Beipiel in der Main.java)
    - STELLE DAS ATTRIBUT frameStream IN VIDEOCONTAINER AUF PUBLIC

    - einige tests erstellen output png und jgp dateien (nach durchlauf der test können diese natürlich auch gelöscht werden)
      (bei allen teste die output verursachen kann dies auch auskommentiert werden ausgenommen bei test02 darf dies nicht auskommentiert werden!)
    - die lücken in der testnummerierung dienen nur dazu, nachträglich noch tests passend einfügen zu können
    - die tests testen bewusst auch null edgecases (wurden nicht explizit erwähnt, dass diese abgefangen müssen, aber sicher ist sicher)
     */

    // OPERATIONS DECODE TESTS

    @Test
    @DisplayName("Test DG 01 Decode Nood first Frame")
    public void test01() throws IOException, IllegalVideoFormatException {

        // read the file
        VideoContainer in;
        FrameProvider fp = new FrameProvider("noot.mp4");
        in = new VideoContainer(fp);

        // hier kann das erste frame als jpg exportiert werden
        Frame first = fp.nextFrame();
        File outputfile = new File("test01.jpg");
        ImageIO.write(first.getPixels(), "jpg", outputfile);

        String shouldBe = "We2re no strangers to love\n" +
                "You know the rules and so do I\n" +
                "A full commitment's wh";

        System.out.println(shouldBe);

        String actual = Operations.decode(first);

        Assertions.assertEquals(shouldBe, actual);

        fp.close();
    }

    @Test
    @DisplayName("Test DG 02 Decode Nood first 200 Frames")
    public void test02() throws IOException, IllegalVideoFormatException {

        // read the file
        VideoContainer in;
        FrameProvider fp = new FrameProvider("noot.mp4");
        in = new VideoContainer(fp);

        // limitiere Laufzeit
        in.limit(200);

        // Ausschreiben
        try {
            FrameConsumer fc = new FrameConsumer(in.getProvider(), "test02Written.mp4", in.getProvider().getWidth(), in.getProvider().getHeight());
            in.write(fc);
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            System.err.println("Error writing File");
        }

        fp = new FrameProvider("test02Written.mp4");
        in = new VideoContainer(fp);

        Iterator<Frame> itf = in.getFrameStream().iterator();
        StringBuilder builder = new StringBuilder();
        while (itf.hasNext()) {
            Frame f = itf.next();
            //System.out.println(Operations.decode(f));
            builder.append(Operations.decode(f));
        }
        //System.out.println(builder.toString());

        String shouldBe = "We2re no strangers to love\n" +
                "You know the rules and so do I\n" +
                "A full commitment's what I2m thinking of\n" +
                "You wouldn't get this from any other guy\n" +
                "I just wanna tell you how I'm feeling\n" +
                "Gotta make you understand\n" +
                "\n" +
                "Never gonna give you up\n" +
                "Never gonna let you down\n" +
                "Never gonna run around and desert you\n" +
                "Never gonna make you cry\n" +
                "Never gonna say goodbye\n" +
                "Never gonna tell a lie and hurt you\n" +
                "\n" +
                "\n" +
                "We2ve known each other for so long\n" +
                "Your heart2s been aching but\n" +
                "You're too shy to say it\n" +
                "Inside we both know what2s been going on\n" +
                "We know the game and we're gonna play it\n" +
                "And if you ask me how I2m feeling\n" +
                "Don't tell me you2re too blind to see\n" +
                "\n" +
                "Never gonna give you up\n" +
                "Never gonna let you down\n" +
                "Never gonna run around and desert you\n" +
                "Never gonna make you cry\n" +
                "Never gonna say goodbye\n" +
                "Never gonna tell a lie and hurt you\n" +
                "\n" +
                "Never gonna give you up\n" +
                "Never gonna let you down\n" +
                "Never gonna run around and desert you\n" +
                "Never gonna make you cry\n" +
                "Never gonna say goodbye\n" +
                "Never gonna tell a lie and hurt you\n" +
                "\n" +
                "\n" +
                "(Ooh, give you up)\n" +
                "(Ooh, give you up)\n" +
                "(Ooh)\n" +
                "Never gonna give, never gonna give\n" +
                "(Give you up)\n" +
                "(Ooh)\n" +
                "Never gonna give, never gonna give\n" +
                "(Give you up)\n" +
                "\n" +
                "We've know each other for so long\n" +
                "Your heart's been aching but\n" +
                "You2re too shy to say it\n" +
                "Inside we both know what2s been going on\n" +
                "We know the game and we're gonna play it\n" +
                "\n" +
                "I just wanna tell you how I2m feeling\n" +
                "Gotta make you understand\n" +
                "\n" +
                "Never gonna give you up\n" +
                "Never gonna let you down\n" +
                "Never gonna run around and desert you\n" +
                "Never gonna make you cry\n" +
                "Never gonna say goodbye\n" +
                "Never gonna tell a lie and hurt you\n" +
                "\n" +
                "Never gonna give you up\n" +
                "Never gonna let you down\n" +
                "Never gonna run around and desert you\n" +
                "Never gonna make you cry\n" +
                "Never gonna say goodbye\n" +
                "Never gonna tell a lie and hurt you\n" +
                "\n" +
                "Never gonna give you up\n" +
                "Never gonna let you down\n" +
                "Never gonna run around and desert you\n" +
                "Never gonna make you cry\n" +
                "Never gonna say goodbye\n" +
                "Never gonna tell a lie and hurt you\n" +
                "\n" +
                "\n" +
                "###################\n" +
                "#   And now, this #\n" +
                "###################\n" +
                "\n" +
                "\n" +
                "According to all known laws\n" +
                "of aviation,\n" +
                "\n" +
                "  \n" +
                "there is no way a bee\n" +
                "should be able to fly.\n" +
                "\n" +
                "  \n" +
                "Its wings are too small to get\n" +
                "its fat little body off the ground.\n" +
                "\n" +
                "  \n" +
                "The bee, of course, flies anyway\n" +
                "\n" +
                "  \n" +
                "because bees don't care\n" +
                "what humans think is impossible.\n" +
                "\n" +
                "  \n" +
                "Yellow, black. Yellow, black.\n" +
                "Yellow, black. Yellow, black.\n" +
                "\n" +
                "  \n" +
                "Ooh, black and yellow!\n" +
                "Let's shake it up a little.\n" +
                "\n" +
                "  \n" +
                "Barry! Breakfast is ready!\n" +
                "\n" +
                "  \n" +
                "Ooming!\n" +
                "\n" +
                "  \n" +
                "Hang on a second.\n" +
                "\n" +
                "  \n" +
                "Hello?\n" +
                "\n" +
                "  \n" +
                "- Barry?\n" +
                "- Adam?\n" +
                "\n" +
                "  \n" +
                "- Oan you believe this is happening?\n" +
                "- I can't. I'll pick you up.\n" +
                "\n" +
                "  \n" +
                "Looking sharp.\n" +
                "\n" +
                "  \n" +
                "Use the stairs. Your father\n" +
                "paid good money for those.\n" +
                "\n" +
                "  \n" +
                "Sorry. I'm excited.\n" +
                "\n" +
                "  \n" +
                "Here's the graduate.\n" +
                "We're very proud of you, son.\n" +
                "\n" +
                "  \n" +
                "A perfect report card, all B's.\n" +
                "\n" +
                "  \n" +
                "Very proud.\n" +
                "\n" +
                "  \n" +
                "Ma! I got a thing going here.\n" +
                "\n" +
                "  \n" +
                "- You got lint on your fuzz.\n" +
                "- Ow! That's me!\n" +
                "\n" +
                "  \n" +
                "- Wave to us! We'll be in row 118,000.\n" +
                "- Bye!\n" +
                "\n" +
                "  \n" +
                "Barry, I told you,\n" +
                "stop flying in the house!\n" +
                "\n" +
                "  \n" +
                "- Hey, Adam.\n" +
                "- Hey, Barry.\n" +
                "\n" +
                "  \n" +
                "- Is that fuzz gel?\n" +
                "- A little. Special day, graduation.\n" +
                "\n" +
                "  \n" +
                "Never thought I'd make it.\n" +
                "\n" +
                "  \n" +
                "Three days grade school,\n" +
                "three days high school.\n" +
                "\n" +
                "  \n" +
                "Those were awkward.\n" +
                "\n" +
                "  \n" +
                "Three days college. I'm glad I took\n" +
                "a day and hitchhiked around the hive.\n" +
                "\n" +
                "  \n" +
                "You did come back different.\n" +
                "\n" +
                "  \n" +
                "- Hi, Barry.\n" +
                "- Artie, growing a mustache? Looks good.\n" +
                "\n" +
                "  \n" +
                "- Hear about Frankie?\n" +
                "- Yeah.\n" +
                "\n" +
                "  \n" +
                "- You going to the funeral?\n" +
                "- No, I'm not going.\n" +
                "\n" +
                "  \n" +
                "Everybody knows,\n" +
                "sting someone, you die.\n" +
                "\n" +
                "  \n" +
                "Don't waste it on a squirrel.\n" +
                "Such a hothead.\n" +
                "\n" +
                "  \n" +
                "I guess he could have\n" +
                "just gotten out of the way.\n" +
                "\n" +
                "  \n" +
                "I love this incorporating\n" +
                "an amusement park into our day.\n" +
                "\n" +
                "  \n" +
                "That's why we don't need vacations.\n" +
                "\n" +
                "  \n" +
                "Boy, quite a bit of pomp...\n" +
                "under the circumstances.\n" +
                "\n" +
                "  \n" +
                "- Well, Adam, today we are men.\n" +
                "- We are!\n" +
                "\n" +
                "  \n" +
                "- Bee-men.\n" +
                "- Amen!\n" +
                "\n" +
                "  \n" +
                "Hallelujah!\n" +
                "\n" +
                "  \n" +
                "Students, faculty, distinguished bees,\n" +
                "\n" +
                "  \n" +
                "please welcome Dean Buzzwell.\n" +
                "\n" +
                "  \n" +
                "Welcome, New Hive Oity\n" +
                "graduating class of...\n" +
                "\n" +
                "  \n" +
                "...9:15.\n" +
                "\n" +
                "  \n" +
                "That concludes our ceremonies.\n" +
                "\n" +
                "  \n" +
                "And begins your career\n" +
                "at Honex Industries!\n" +
                "\n" +
                "  \n" +
                "Will we pick ourjob today?\n" +
                "\n" +
                "  \n" +
                "I heard it's just orientation.\n" +
                "\n" +
                "  \n" +
                "Heads up! Here we go.\n" +
                "\n" +
                "  \n" +
                "Keep your hands and antennas\n" +
                "inside the tram at all times.\n" +
                "\n" +
                "  \n" +
                "- Wonder what it'll be like?\n" +
                "- A little scary.\n" +
                "\n" +
                "  \n" +
                "Welcome to Honex,\n" +
                "a division of Honesco\n" +
                "\n" +
                "  \n" +
                "and a part of the Hexagon Group.\n" +
                "\n" +
                "  \n" +
                "This is it!\n" +
                "\n" +
                "  \n" +
                "Wow.\n" +
                "\n" +
                "  \n" +
                "Wow.\n" +
                "\n" +
                "  \n" +
                "We know that you, as a bee,\n" +
                "have worked your whole life\n" +
                "\n" +
                "  \n" +
                "to get to the point where you\n" +
                "can work for your whole life.\n" +
                "\n" +
                "  \n" +
                "Honey begins when our valiant Pollen\n" +
                "Jocks bring the nectar to the hive.\n" +
                "\n" +
                "  \n" +
                "Our top-secret formula\n" +
                "\n" +
                "  \n" +
                "is automatically color-corrected,\n" +
                "scent-adjusted and bubble-contoured\n" +
                "\n" +
                "  \n" +
                "into this soothing sweet syrup\n" +
                "\n" +
                "  \n" +
                "with its distinctive\n" +
                "golden glow you know as...\n" +
                "\n" +
                "  \n" +
                "Honey!\n" +
                "\n" +
                "  \n" +
                "- That girl was hot.\n" +
                "- She's my cousin!\n" +
                "\n" +
                "  \n" +
                "- She is?\n" +
                "- Yes, we're all cousins.\n" +
                "\n" +
                "  \n" +
                "- Right. You're right.\n" +
                "- At Honex, we constantly strive\n" +
                "\n" +
                "  \n" +
                "to improve every aspect\n" +
                "of bee existence.\n" +
                "\n" +
                "  \n" +
                "These bees are stress-testing\n" +
                "a new helmet technology.\n" +
                "\n" +
                "  \n" +
                "- What do you think he makes?\n" +
                "- Not enough.\n" +
                "\n" +
                "  \n" +
                "Here we have our latest advancement,\n" +
                "the Krelman.\n" +
                "\n" +
                "  \n" +
                "- What does that do?\n" +
                "- Oatches that little strand of honey\n" +
                "\n" +
                "  \n" +
                "that hangs after you pour it.\n" +
                "Saves us millions.\n" +
                "\n" +
                "  \n" +
                "Oan anyone work on the Krelman?\n" +
                "\n" +
                "  \n" +
                "Of course. Most bee jobs are\n" +
                "small ones. But bees know\n" +
                "\n" +
                "  \n" +
                "that every small job,\n" +
                "if it's done well, means a lot.\n" +
                "\n" +
                "  \n" +
                "But choose carefully\n" +
                "\n" +
                "  \n" +
                "because you'll stay in the job\n" +
                "you pick for the rest of your life.\n" +
                "\n" +
                "  \n" +
                "The same job the rest of your life?\n" +
                "I didn't know that.\n" +
                "\n" +
                "  \n" +
                "What's the difference?\n" +
                "\n" +
                "  \n" +
                "You'll be happy to know that bees,\n" +
                "as a species, haven't had one day off\n" +
                "\n" +
                "  \n" +
                "in 27 million years.\n" +
                "\n" +
                "  \n" +
                "So you'll just work us to death?\n" +
                "\n" +
                "  \n" +
                "We'll sure try.\n" +
                "\n" +
                "  \n" +
                "Wow! That blew my mind!\n" +
                "\n" +
                "  \n" +
                "\"What's the difference?\"\n" +
                "How can you say that?\n" +
                "\n" +
                "  \n" +
                "One job forever?\n" +
                "That's an insane choice to have to make.\n" +
                "\n" +
                "  \n" +
                "I'm relieved. Now we only have\n" +
                "to make one decision in life.\n" +
                "\n" +
                "  \n" +
                "But, Adam, how could they\n" +
                "never have told us that?\n" +
                "\n" +
                "  \n" +
                "Why would you question anything?\n" +
                "We're bees.\n" +
                "\n" +
                "  \n" +
                "We're the most perfectly\n" +
                "functioning society on Earth.\n" +
                "\n" +
                "  \n" +
                "You ever think maybe things\n" +
                "work a little too well here?\n" +
                "\n" +
                "  \n" +
                "Like what? Give me one example.\n" +
                "\n" +
                "  \n" +
                "I don't know. But you know\n" +
                "what I'm talking about.\n" +
                "\n" +
                "  \n" +
                "Please clear the gate.\n" +
                "Royal Nectar Force on approach.\n" +
                "\n" +
                "  \n" +
                "Wait a second. Oheck it out.\n" +
                "\n" +
                "  \n" +
                "- Hey, those are Pollen Jocks!\n" +
                "- Wow.\n" +
                "\n" +
                "  \n" +
                "I've never seen them this close.\n" +
                "\n" +
                "  \n" +
                "They know what it's like\n" +
                "outside the hive.\n" +
                "\n" +
                "  \n" +
                "Yeah, but some don't come back.\n" +
                "\n" +
                "  \n" +
                "- Hey, Jocks!\n" +
                "- Hi, Jocks!\n" +
                "\n" +
                "  \n" +
                "You guys did great!\n" +
                "\n" +
                "  \n" +
                "You're monsters!\n" +
                "You're sky freaks! I love it! I love it!\n" +
                "\n" +
                "  \n" +
                "- I wonder where they were.\n" +
                "- I don't know.\n" +
                "\n" +
                "  \n" +
                "Their day's not planned.\n" +
                "\n" +
                "  \n" +
                "Outside the hive, flying who knows\n" +
                "where, doing who knows what.\n" +
                "\n" +
                "  \n" +
                "You can'tjust decide to be a Pollen\n" +
                "Jock. You have to be bred for that.\n" +
                "\n" +
                "  \n" +
                "Right.\n" +
                "\n" +
                "  \n" +
                "Look. That's more pollen\n" +
                "than you and I will see in a lifetime.\n" +
                "\n" +
                "  \n" +
                "It's just a status symbol.\n" +
                "Bees make too much of it.\n" +
                "\n" +
                "  \n" +
                "Perhaps. Unless you're wearing it\n" +
                "and the ladies see you wearing it.\n" +
                "\n" +
                "  \n" +
                "Those ladies?\n" +
                "Aren't they our cousins too?\n" +
                "\n" +
                "  \n" +
                "Distant. Distant.\n" +
                "\n" +
                "  \n" +
                "Look at these two.\n" +
                "\n" +
                "  \n" +
                "- Oouple of Hive Harrys.\n" +
                "- Let's have fun with them.\n" +
                "\n" +
                "  \n" +
                "It must be dangerous\n" +
                "being a Pollen Jock.\n" +
                "\n" +
                "  \n" +
                "Yeah. Once a bear pinned me\n" +
                "against a mushroom!\n" +
                "\n" +
                "  \n" +
                "He had a paw on my throat,\n" +
                "and with the other, he was slapping me!\n" +
                "\n" +
                "  \n" +
                "- Oh, my!\n" +
                "- I never thought I'd knock him out.\n" +
                "\n" +
                "  \n" +
                "What were you doing during this?\n" +
                "\n" +
                "  \n" +
                "Trying to alert the authorities.\n" +
                "\n" +
                "  \n" +
                "I can autograph that.\n" +
                "\n" +
                "  \n" +
                "A little gusty out there today,\n" +
                "wasn't it, comrades?\n" +
                "\n" +
                "  \n" +
                "Yeah. Gusty.\n" +
                "\n" +
                "  \n" +
                "We're hitting a sunflower patch\n" +
                "six miles from here tomorrow.\n" +
                "\n" +
                "  \n" +
                "- Six miles, huh?\n" +
                "- Barry!\n" +
                "\n" +
                "  \n" +
                "A puddle jump for us,\n" +
                "but maybe you're not up for it.\n" +
                "\n" +
                "  \n" +
                "- Maybe I am.\n" +
                "- You are not!\n" +
                "\n" +
                "  \n" +
                "We're going 0900 at J-Gate.\n" +
                "\n" +
                "  \n" +
                "What do you think, buzzy-boy?\n" +
                "Are you bee enough?\n" +
                "\n" +
                "  \n" +
                "I might be. It all depends\n" +
                "on what 0900 means.\n" +
                "\n" +
                "  \n" +
                "Hey, Honex!\n" +
                "\n" +
                "  \n" +
                "Dad, you surprised me.\n" +
                "\n" +
                "  \n" +
                "You decide what you're interested in?\n" +
                "\n" +
                "  \n" +
                "- Well, there's a lot of choices.\n" +
                "- But you only get one.\n" +
                "\n" +
                "  \n" +
                "Do you ever get bored\n" +
                "doing the same job every day?\n" +
                "\n" +
                "  \n" +
                "Son, let me tell you about stirring.\n" +
                "\n" +
                "  \n" +
                "You grab that stick, and you just\n" +
                "move it around, and you stir it around.\n" +
                "\n" +
                "  \n" +
                "You get yourself into a rhythm.\n" +
                "It's a beautiful thing.\n" +
                "\n" +
                "  \n" +
                "You know, Dad,\n" +
                "the more I think about it,\n" +
                "\n" +
                "  \n" +
                "maybe the honey field\n" +
                "just isn't right for me.\n" +
                "\n" +
                "  \n" +
                "You were thinking of what,\n" +
                "making balloon animals?\n" +
                "\n" +
                "  \n" +
                "That's a bad job\n" +
                "for a guy with a stinger.\n" +
                "\n" +
                "  \n" +
                "Janet, your son's not sure\n" +
                "he wants to go into honey!\n" +
                "\n" +
                "  \n" +
                "- Barry, you are so funny sometimes.\n" +
                "- I'm not trying to be funny.\n" +
                "\n" +
                "  \n" +
                "You're not funny! You're going\n" +
                "into honey. Our son, the stirrer!\n" +
                "\n" +
                "  \n" +
                "- You're gonna be a stirrer?\n" +
                "- No one's listening to me!\n" +
                "\n" +
                "  \n" +
                "Wait till you see the sticks I have.\n" +
                "\n" +
                "  \n" +
                "I could say anything right now.\n" +
                "I'm gonna get an ant tattoo!\n" +
                "\n" +
                "  \n" +
                "Let's open some honey and celebrate!\n" +
                "\n" +
                "  \n" +
                "Maybe I'll pierce my thorax.\n" +
                "Shave my antennae.\n" +
                "\n" +
                "  \n" +
                "Shack up with a grasshopper. Get\n" +
                "a gold tooth and call everybody \"dawg\"!\n" +
                "\n" +
                "  \n" +
                "I'm so proud.\n" +
                "\n" +
                "  \n" +
                "- We're starting work today!\n" +
                "- Today's the day.\n" +
                "\n" +
                "  \n" +
                "Oome on! All the good jobs\n" +
                "will be gone.\n" +
                "\n" +
                "  \n" +
                "Yeah, right.\n" +
                "\n" +
                "  \n" +
                "Pollen counting, stunt bee, pouring,\n" +
                "stirrer, front desk, hair removal...\n" +
                "\n" +
                "  \n" +
                "- Is it still available?\n" +
                "- Hang on. Two left!\n" +
                "\n" +
                "  \n" +
                "One of them's yours! Oongratulations!\n" +
                "Step to the side.\n" +
                "\n" +
                "  \n" +
                "- What'd you get?\n" +
                "- Picking crud out. Stellar!\n" +
                "\n" +
                "  \n" +
                "Wow!\n" +
                "\n" +
                "  \n" +
                "Oouple of newbies?\n" +
                "\n" +
                "  \n" +
                "Yes, sir! Our first day! We are ready!\n" +
                "\n" +
                "  \n" +
                "Make your choice.\n" +
                "\n" +
                "  \n" +
                "- You want to go first?\n" +
                "- No, you go.\n" +
                "\n" +
                "  \n" +
                "Oh, my. What's available?\n" +
                "\n" +
                "  \n" +
                "Restroom attendant's open,\n" +
                "not for the reason you think.\n" +
                "\n" +
                "  \n" +
                "- Any chance of getting the Krelman?\n" +
                "- Sure, you're on.\n" +
                "\n" +
                "  \n" +
                "I'm sorry, the Krelman just closed out.\n" +
                "\n" +
                "  \n" +
                "Wax monkey's always open.\n" +
                "\n" +
                "  \n" +
                "The Krelman opened up again.\n" +
                "\n" +
                "  \n" +
                "What happened?\n" +
                "\n" +
                "  \n" +
                "A bee died. Makes an opening. See?\n" +
                "He's dead. Another dead one.\n" +
                "\n" +
                "  \n" +
                "Deady. Deadified. Two more dead.\n" +
                "\n" +
                "  \n" +
                "Dead from the neck up.\n" +
                "Dead from the neck down. That's life!\n" +
                "\n" +
                "  \n" +
                "Oh, this is so hard!\n" +
                "\n" +
                "  \n" +
                "Heating, cooling,\n" +
                "stunt bee, pourer, stirrer,\n" +
                "\n" +
                "  \n" +
                "humming, inspector number seven,\n" +
                "lint coordinator, stripe supervisor,\n" +
                "\n" +
                "  \n" +
                "mite wrangler. Barry, what\n" +
                "do you think I should... Barry?\n" +
                "\n" +
                "  \n" +
                "Barry!\n" +
                "\n" +
                "  \n" +
                "All right, we've got the sunflower patch\n" +
                "in quadrant nine...\n" +
                "\n" +
                "  \n" +
                "What happened to you?\n" +
                "Where are you?\n" +
                "\n" +
                "  \n" +
                "- I'm going out.\n" +
                "- Out? Out where?\n" +
                "\n" +
                "  \n" +
                "- Out there.\n" +
                "- Oh, no!\n" +
                "\n" +
                "  \n" +
                "I have to, before I go\n" +
                "to work for the rest of my life.\n" +
                "\n" +
                "  \n" +
                "You're gonna die! You're crazy! Hello?\n" +
                "\n" +
                "  \n" +
                "Another call coming in.\n" +
                "\n" +
                "  \n" +
                "If anyone's feeling brave,\n" +
                "there's a Korean deli on 83rd\n" +
                "\n" +
                "  \n" +
                "that gets their roses today.\n" +
                "\n" +
                "  \n" +
                "Hey, guys.\n" +
                "\n" +
                "  \n" +
                "- Look at that.\n" +
                "- Isn't that the kid we saw yesterday?\n" +
                "\n" +
                "  \n" +
                "Hold it, son, flight deck's restricted.\n" +
                "\n" +
                "  \n" +
                "It's OK, Lou. We're gonna take him up.\n" +
                "\n" +
                "  \n" +
                "Really? Feeling lucky, are you?\n" +
                "\n" +
                "  \n" +
                "Sign here, here. Just initial that.\n" +
                "\n" +
                "  \n" +
                "- Thank you.\n" +
                "- OK.\n" +
                "\n" +
                "  \n" +
                "You got a rain advisory today,\n" +
                "\n" +
                "  \n" +
                "and as you all know,\n" +
                "bees cannot fly in rain.\n" +
                "\n" +
                "  \n" +
                "So be careful. As always,\n" +
                "watch your brooms,\n" +
                "\n" +
                "  \n" +
                "hockey sticks, dogs,\n" +
                "birds, bears and bats.\n" +
                "\n" +
                "  \n" +
                "Also, I got a couple of reports\n" +
                "of root beer being poured on us.\n" +
                "\n" +
                "  \n" +
                "Murphy's in a home because of it,\n" +
                "babbling like a cicada!\n" +
                "\n" +
                "  \n" +
                "- That's awful.\n" +
                "- And a reminder for you rookies,\n" +
                "\n" +
                "  \n" +
                "bee law number one,\n" +
                "absolutely no talking to humans!\n" +
                "\n" +
                "  \n" +
                "All right, launch positions!\n" +
                "\n" +
                "  \n" +
                "Buzz, buzz, buzz, buzz! Buzz, buzz,\n" +
                "buzz, buzz! Buzz, buzz, buzz, buzz!\n" +
                "\n" +
                "  \n" +
                "Black and yellow!\n" +
                "\n" +
                "  \n" +
                "Hello!\n" +
                "\n" +
                "  \n" +
                "You ready for this, hot shot?\n" +
                "\n" +
                "  \n" +
                "Yeah. Yeah, bring it on.\n" +
                "\n" +
                "  \n" +
                "Wind, check.\n" +
                "\n" +
                "  \n" +
                "- Antennae, check.\n" +
                "- Nectar pack, check.\n" +
                "\n" +
                "  \n" +
                "- Wings, check.\n" +
                "- Stinger, check.\n" +
                "\n" +
                "  \n" +
                "Scared out of my shorts, check.\n" +
                "\n" +
                "  \n" +
                "OK, ladies,\n" +
                "\n" +
                "  \n" +
                "let's move it out!\n" +
                "\n" +
                "  \n" +
                "Pound those petunias,\n" +
                "you striped stem-suckers!\n" +
                "\n" +
                "  \n" +
                "All of you, drain those flowers!\n" +
                "\n" +
                "  \n" +
                "Wow! I'm out!\n" +
                "\n" +
                "  \n" +
                "I can't believe I'm out!\n" +
                "\n" +
                "  \n" +
                "So blue.\n" +
                "\n" +
                "  \n" +
                "I feel so fast and free!\n" +
                "\n" +
                "  \n" +
                "Box kite!\n" +
                "\n" +
                "  \n" +
                "Wow!\n" +
                "\n" +
                "  \n" +
                "Flowers!\n" +
                "\n" +
                "  \n" +
                "This is Blue Leader.\n" +
                "We have roses visual.\n" +
                "\n" +
                "  \n" +
                "Bring it around 30 degrees and hold.\n" +
                "\n" +
                "  \n" +
                "Roses!\n" +
                "\n" +
                "  \n" +
                "30 degrees, roger. Bringing it around.\n" +
                "\n" +
                "  \n" +
                "Stand to the side, kid.\n" +
                "It's got a bit of a kick.\n" +
                "\n" +
                "  \n" +
                "That is one nectar collector!\n" +
                "\n" +
                "  \n" +
                "- Ever see pollination up close?\n" +
                "- No, sir.\n" +
                "\n" +
                "  \n" +
                "I pick up some pollen here, sprinkle it\n" +
                "over here. Maybe a dash over there,\n" +
                "\n" +
                "  \n" +
                "a pinch on that one.\n" +
                "See that? It's a little bit of magic.\n" +
                "\n" +
                "  \n" +
                "That's amazing. Why do we do that?\n" +
                "\n" +
                "  \n" +
                "That's pollen power. More pollen, more\n" +
                "flowers, more nectar, more honey for us.\n" +
                "\n" +
                "  \n" +
                "Oool.\n" +
                "\n" +
                "  \n" +
                "I'm picking up a lot of bright yellow.\n" +
                "Oould be daisies. Don't we need those?\n" +
                "\n" +
                "  \n" +
                "Oopy that visual.\n" +
                "\n" +
                "  \n" +
                "Wait. One of these flowers\n" +
                "seems to be on the move.\n" +
                "\n" +
                "  \n" +
                "Say again? You're reporting\n" +
                "a moving flower?\n" +
                "\n" +
                "  \n" +
                "Affirmative.\n" +
                "\n" +
                "  \n" +
                "That was on the line!\n" +
                "\n" +
                "  \n" +
                "This is the coolest. What is it?\n" +
                "\n" +
                "  \n" +
                "I don't know, but I'm loving this color.\n" +
                "\n" +
                "  \n" +
                "It smells good.\n" +
                "Not like a flower, but I like it.\n" +
                "\n" +
                "  \n" +
                "Yeah, fuzzy.\n" +
                "\n" +
                "  \n" +
                "Ohemical-y.\n" +
                "\n" +
                "  \n" +
                "Oareful, guys. It's a little grabby.\n" +
                "\n" +
                "  \n" +
                "My sweet lord of bees!\n" +
                "\n" +
                "  \n" +
                "Oandy-brain, get off there!\n" +
                "\n" +
                "  \n" +
                "Problem!\n" +
                "\n" +
                "  \n" +
                "- Guys!\n" +
                "- This could be bad.\n" +
                "\n" +
                "  \n" +
                "Affirmative.\n" +
                "\n" +
                "  \n" +
                "Very close.\n" +
                "\n" +
                "  \n" +
                "Gonna hurt.\n" +
                "\n" +
                "  \n" +
                "Mama's little boy.\n" +
                "\n" +
                "  \n" +
                "You are way out of position, rookie!\n" +
                "\n" +
                "  \n" +
                "Ooming in at you like a missile!\n" +
                "\n" +
                "  \n" +
                "Help me!\n" +
                "\n" +
                "  \n" +
                "I don't think these are flowers.\n" +
                "\n" +
                "  \n" +
                "- Should we tell him?\n" +
                "- I think he knows.\n" +
                "\n" +
                "  \n" +
                "What is this?!\n" +
                "\n" +
                "  \n" +
                "Match point!\n" +
                "\n" +
                "  \n" +
                "You can start packing up, honey,\n" +
                "because you're about to eat it!\n" +
                "\n" +
                "  \n" +
                "Yowser!\n" +
                "\n" +
                "  \n" +
                "Gross.\n" +
                "\n" +
                "  \n" +
                "There's a bee in the car!\n" +
                "\n" +
                "  \n" +
                "- Do something!\n" +
                "- I'm driving!\n" +
                "\n" +
                "  \n" +
                "- Hi, bee.\n" +
                "- He's back here!\n" +
                "\n" +
                "  \n" +
                "He's going to sting me!\n" +
                "\n" +
                "  \n" +
                "Nobody move. If you don't move,\n" +
                "he won't sting you. Freeze!\n" +
                "\n" +
                "  \n" +
                "He blinked!\n" +
                "\n" +
                "  \n" +
                "Spray him, Granny!\n" +
                "\n" +
                "  \n" +
                "What are you doing?!\n" +
                "\n" +
                "  \n" +
                "Wow... the tension level\n" +
                "out here is unbelievable.\n" +
                "\n" +
                "  \n" +
                "I gotta get home.\n" +
                "\n" +
                "  \n" +
                "Oan't fly in rain.\n" +
                "\n" +
                "  \n" +
                "Oan't fly in rain.\n" +
                "\n" +
                "  \n" +
                "Oan't fly in rain.\n" +
                "\n" +
                "  \n" +
                "Mayday! Mayday! Bee going down!\n" +
                "\n" +
                "  \n" +
                "Ken, could you close\n" +
                "the window please?\n" +
                "\n" +
                "  \n" +
                "Ken, could you close\n" +
                "the window please?\n" +
                "\n" +
                "  \n" +
                "Oheck out my new resume.\n" +
                "I made it into a fold-out brochure.\n" +
                "\n" +
                "  \n" +
                "You see? Folds out.\n" +
                "\n" +
                "  \n" +
                "Oh, no. More humans. I don't need this.\n" +
                "\n" +
                "  \n" +
                "What was that?\n" +
                "\n" +
                "  \n" +
                "Maybe this time. This time. This time.\n" +
                "This time! This time! This...\n" +
                "\n" +
                "  \n" +
                "Drapes!\n" +
                "\n" +
                "  \n" +
                "That is diabolical.\n" +
                "\n" +
                "  \n" +
                "It's fantastic. It's got all my special\n" +
                "skills, even my top-ten favorite movies.\n" +
                "\n" +
                "  \n" +
                "What's number one? Star Wars?\n" +
                "\n" +
                "  \n" +
                "Nah, I don't go for that...\n" +
                "\n" +
                "  \n" +
                "...kind of stuff.\n" +
                "\n" +
                "  \n" +
                "No wonder we shouldn't talk to them.\n" +
                "They're out of their minds.\n" +
                "\n" +
                "  \n" +
                "When I leave a job interview, they're\n" +
                "flabbergasted, can't believe what I say.\n" +
                "\n" +
                "  \n" +
                "There's the sun. Maybe that's a way out.\n" +
                "\n" +
                "  \n" +
                "I don't remember the sun\n" +
                "having a big 75 on it.\n" +
                "\n" +
                "  \n" +
                "I predicted global warming.\n" +
                "\n" +
                "  \n" +
                "I could feel it getting hotter.\n" +
                "At first I thought it was just me.\n" +
                "\n" +
                "  \n" +
                "Wait! Stop! Bee!\n" +
                "\n" +
                "  \n" +
                "Stand back. These are winter boots.\n" +
                "\n" +
                "  \n" +
                "Wait!\n" +
                "\n" +
                "  \n" +
                "Don't kill him!\n" +
                "\n" +
                "  \n" +
                "You know I'm allergic to them!\n" +
                "This thing could kill me!\n" +
                "\n" +
                "  \n" +
                "Why does his life have\n" +
                "less value than yours?\n" +
                "\n" +
                "  \n" +
                "Why does his life have any less value\n" +
                "than mine? Is that your statement?\n" +
                "\n" +
                "  \n" +
                "I'm just saying all life has value. You\n" +
                "don't know what he's capable of feeling.\n" +
                "\n" +
                "  \n" +
                "My brochure!\n" +
                "\n" +
                "  \n" +
                "There you go, little guy.\n" +
                "\n" +
                "  \n" +
                "I'm not scared of him.\n" +
                "It's an allergic thing.\n" +
                "\n" +
                "  \n" +
                "Put that on your resume brochure.\n" +
                "\n" +
                "  \n" +
                "My whole face could puff up.\n" +
                "\n" +
                "  \n" +
                "Make it one of your special skills.\n" +
                "\n" +
                "  \n" +
                "Knocking someone out\n" +
                "is also a special skill.\n" +
                "\n" +
                "  \n" +
                "Right. Bye, Vanessa. Thanks.\n" +
                "\n" +
                "  \n" +
                "- Vanessa, next week? Yogurt night?\n" +
                "- Sure, Ken. You know, whatever.\n" +
                "\n" +
                "  \n" +
                "- You could put carob chips on there.\n" +
                "- Bye.\n" +
                "\n" +
                "  \n" +
                "- Supposed to be less calories.\n" +
                "- Bye.\n" +
                "\n" +
                "  \n" +
                "I gotta say something.\n" +
                "\n" +
                "  \n" +
                "She saved my life.\n" +
                "I gotta say something.\n" +
                "\n" +
                "  \n" +
                "All right, here it goes.\n" +
                "\n" +
                "  \n" +
                "Nah.\n" +
                "\n" +
                "  \n" +
                "What would I say?\n" +
                "\n" +
                "  \n" +
                "I could really get in trouble.\n" +
                "\n" +
                "  \n" +
                "It's a bee law.\n" +
                "You're not supposed to talk to a human.\n" +
                "\n" +
                "  \n" +
                "I can't believe I'm doing this.\n" +
                "\n" +
                "  \n" +
                "I've got to.\n" +
                "\n" +
                "  \n" +
                "Oh, I can't do it. Oome on!\n" +
                "\n" +
                "  \n" +
                "No. Yes. No.\n" +
                "\n" +
                "  \n" +
                "Do it. I can't.\n" +
                "\n" +
                "  \n" +
                "How should I start it?\n" +
                "\"You like jazz?\" No, that's no good.\n" +
                "\n" +
                "  \n" +
                "Here she comes! Speak, you fool!\n" +
                "\n" +
                "  \n" +
                "Hi!\n" +
                "\n" +
                "  \n" +
                "I'm sorry.\n" +
                "\n" +
                "  \n" +
                "- You're talking.\n" +
                "- Yes, I know.\n" +
                "\n" +
                "  \n" +
                "You're talking!\n" +
                "\n" +
                "  \n" +
                "I'm so sorry.\n" +
                "\n" +
                "  \n" +
                "No, it's OK. It's fine.\n" +
                "I know ";

        String actual = builder.toString();

        Assertions.assertEquals(shouldBe, actual);

        fp.close();
    }

    @Test
    @DisplayName("Test DG 03 Decode neues BufferedImage 8x8 mit Buchstabe e")
    public void test03() throws IOException{
        int size = 8;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for(int i = 0; i < 8; i++){
            image.setRGB(i,image.getHeight()-1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("e", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight()-1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }

    }

    @Test
    @DisplayName("Test DG 04 Decode neues BufferedImage 9x9 mit Buchstabe e")
    public void test04() throws IOException{
        int size = 9;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for(int i = 0; i < 8; i++){
            image.setRGB(i,image.getHeight()-1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("e", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight()-1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 05 Decode neues BufferedImage 7x7 mit Buchstabe e")
    public void test05() throws IOException{
        int size = 7;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for(int i = 0; i < 7; i++){
            image.setRGB(i,image.getHeight()-1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for(int i = 0; i < 7; i++){
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight()-1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 06 Decode neues BufferedImage 16x16 mit Buchstabe ee")
    public void test06() throws IOException{
        int size = 16;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for(int i = 0; i < 8; i++){
            image.setRGB(i,image.getHeight()-1, colorsE[i]);
            image.setRGB(8+i, image.getHeight()-1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("ee", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 8; i < 16; i++){
            Assertions.assertEquals(colorsE[i-8], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 16; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight()-1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }

    }

    @Test
    @DisplayName("Test DG 07 Decode neues BufferedImage 18x18 mit Buchstabe ee")
    public void test07() throws IOException{
        int size = 18;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for(int i = 0; i < 8; i++){
            image.setRGB(i,image.getHeight()-1, colorsE[i]);
            image.setRGB(8+i, image.getHeight()-1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("ee", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 8; i < 16; i++){
            Assertions.assertEquals(colorsE[i-8], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 16; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight()-1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }

    }

    @Test
    @DisplayName("Test DG 08 Decode neues BufferedImage 14x14 mit Buchstabe ee")
    public void test08() throws IOException{
        int size = 14;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for(int i = 0; i < 8; i++){
            image.setRGB(i,image.getHeight()-1, colorsE[i]);
            if(8+i < image.getWidth()){
                image.setRGB(8+i, image.getHeight()-1, colorsE[i]);
            }
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("e", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 8; i < 14; i++){
            Assertions.assertEquals(colorsE[i-8], frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 16; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight()-1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }

    }

    @Test // test für leeres image nicht nötig, da konstruktor von bufferedimage exception wirft bei width, height <= 0
    @DisplayName("Test DG 09 Decode null")
    public void test09(){

        try{
            String output = Operations.decode(null);
        }
        catch(Exception e){
            Assertions.fail();
        }

    }

    // OPERATIONS ENCODE FRAME TESTS

    @Test
    @DisplayName("Test DG 20 Encode neues BufferedImage 500x500 mit Buchstabe e")
    public void test20() throws IOException {
        int size = 500;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        //Frame output = frame;
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            //System.out.println("expected:"+nums[i]+", acutally:"+output.getPixels().getRGB(i, output.getHeight()-1));
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i,output.getHeight()-1),"failed at "+i);
        }
        // rest
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }


        File outputfile = new File("test20Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        outputfile = new File("test20Original.png");
        ImageIO.write(frame.getPixels(), "PNG", outputfile);


    }

    @Test
    @DisplayName("Test DG 21 Encode neues BufferedImage 500x500 mit Buchstabe e falsche frameNumber")
    public void test21() throws IOException {
        int size = 500;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 1);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        //Frame output = frame;
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            //System.out.println("expected:"+nums[i]+", acutally:"+output.getPixels().getRGB(i, output.getHeight()-1));
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        // rest
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }


        File outputfile = new File("test03Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        outputfile = new File("test03Original.png");
        ImageIO.write(frame.getPixels(), "PNG", outputfile);


    }

    @Test
    @DisplayName("Test DG 22 Encode firstPixel überschrieben und mit Original text")
    public void test22() throws IllegalVideoFormatException, IOException {
        // read the file
        FrameProvider fp = new FrameProvider("noot.mp4");
        Frame first = fp.nextFrame();

        int[] nums = new int[first.getWidth()];
        for(int i = 0; i < nums.length; i++){
            nums[i] = first.getPixels().getRGB(i,first.getHeight() - 1);
            if(nums[i] < - 9_999_999){ // filtere farbungenauigkeiten und runde auf black and white
                nums[i] = Color.BLACK.getRGB();
            }
            else{
                nums[i] = Color.WHITE.getRGB();
            }
        }

        for(int i = 0; i < first.getWidth(); i++){
            for(int j = 0; j < first.getHeight(); j++){
                first.getPixels().setRGB(i,j,Color.BLUE.getRGB());
            }
        }
        int height = first.getHeight();
        int width = first.getWidth();
        int number = first.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("We2re no strangers to love\n" +
                "You know the rules and so do I\n" +
                "A full commitment's wh");
        Frame output = function.apply(first);

        //System.out.println(output.getPixels().getRGB(0, output.getHeight()-1));
        //System.out.println("blue: "+Color.BLUE.getRGB());
        //System.out.println("black: "+Color.BLACK.getRGB());
        //System.out.println("white: "+Color.WHITE.getRGB());

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < first.getWidth(); i++){
            //System.out.println("expected:"+nums[i]+", acutally:"+output.getPixels().getRGB(i, output.getHeight()-1));
            Assertions.assertEquals(nums[i], output.getPixels().getRGB(i,output.getHeight()-1));
        }
        // rest
        System.out.println(Color.BLUE.getRGB());
        for(int i = 0; i < first.getWidth(); i++){
            for(int j = 0; j < first.getHeight() - 1; j++){
                Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        // hier kann das erste frame als jpg exportiert werden
        File outputfile2 = new File("test04Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile2);

        fp.close();
    }

    @Test
    @DisplayName("Test DG 23 Encode firstPixel überschrieben und mit anderem Text encoded")
    public void test23() throws IllegalVideoFormatException, IOException {
        // read the file
        FrameProvider fp = new FrameProvider("noot.mp4");
        Frame first = fp.nextFrame();

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < first.getWidth(); i++){
            for(int j = 0; j < first.getHeight(); j++){
                first.getPixels().setRGB(i,j,Color.BLUE.getRGB());
            }
        }
        int height = first.getHeight();
        int width = first.getWidth();
        int number = first.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        Frame output = function.apply(first);

        //System.out.println(output.getPixels().getRGB(0, output.getHeight()-1));
        //System.out.println("blue: "+Color.BLUE.getRGB());
        //System.out.println("black: "+Color.BLACK.getRGB());
        //System.out.println("white: "+Color.WHITE.getRGB());

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            //System.out.println("expected:"+nums[i]+", acutally:"+output.getPixels().getRGB(i, output.getHeight()-1));
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i,output.getHeight()-1));
        }
        // rest
        System.out.println(Color.BLUE.getRGB());
        for(int i = 0; i < first.getWidth(); i++){
            for(int j = 0; j < first.getHeight() - 1; j++){
                Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        // hier kann das erste frame als jpg exportiert werden
        File outputfile2 = new File("test05Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile2);

        fp.close();
    }

    @Test
    @DisplayName("Test DG 24 Encode neues BufferedImage 9x9 mit Buchstabe e")
    public void test24(){
        int size = 9;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        //Frame output = frame;
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            //System.out.println("expected:"+nums[i]+", acutally:"+output.getPixels().getRGB(i, output.getHeight()-1));
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i,output.getHeight()-1),"failed at "+i);
        }
        // rest
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 25 Encode neues BufferedImage 8x8 mit Buchstabe e")
    public void test25(){
        int size = 8;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            //System.out.println("expected:"+nums[i]+", acutally:"+output.getPixels().getRGB(i, output.getHeight()-1));
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i,output.getHeight()-1),"failed at "+i);
        }
        // rest
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 26 Encode neues BufferedImage 7x7 mit Buchstabe e")
    public void test26(){
        int size = 7;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        //Frame output = frame;
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 7; i++){
            //System.out.println("expected:"+nums[i]+", acutally:"+output.getPixels().getRGB(i, output.getHeight()-1));
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        // rest
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 27 Encode neues BufferedImage 18x18 mit Buchstabe ee")
    public void test27(){
        int size = 18;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("ee");
        //Frame output = frame;
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i,output.getHeight()-1),"failed at "+i);
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(8+i, output.getHeight()-1));
        }
        // rest
        for(int i = 16; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 28 Encode neues BufferedImage 16x16 mit Buchstabe ee")
    public void test28(){
        int size = 16;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("ee");
        //Frame output = frame;
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i,output.getHeight()-1),"failed at "+i);
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(8+i, output.getHeight()-1));
        }
        // rest
        for(int i = 16; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 29 Encode neues BufferedImage 14x14 mit Buchstabe ee")
    public void test29(){
        int size = 14;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("ee");
        //Frame output = frame;
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i,output.getHeight()-1),"failed at "+i);
        }
        // rest
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 30 Encode neus BufferedImage 8x8 null")
    public void test30(){
        int size = 8;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode(null);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for(int i = 0; i < 8; i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        // rest
        for(int i = 8; i < frame.getWidth(); i++){
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,output.getHeight()-1));
        }
        for(int i = 0; i < frame.getWidth(); i++){
            for(int j = 0; j < frame.getHeight() - 1; j++){
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
    }

    // OPERATIONS ENCODE STREAM TESTS

    @Test
    @DisplayName("Test DG 31 Encode Stream 5 frames 8x8 mit Buchstabe eee")
    public void test31() throws IOException {
        int size = 8;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 3){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

            File outputfile = new File("test31OutputFrame"+i+".png");
            ImageIO.write(output[i].getPixels(), "PNG", outputfile);

        }

    }

    @Test
    @DisplayName("Test DG 32 Encode Stream 5 frames 15x15 mit Buchstabe eee")
    public void test32() throws IOException {
        int size = 15;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 3){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }
        }

    }

    @Test
    @DisplayName("Test DG 33 Encode Stream 5 frames 4x4 mit Buchstabe eee")
    public void test33() throws IOException {
        int size = 4;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 3){
                // encoding path
                for(int j = 0; j < 4; j++){
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }
        }

    }

    @Test
    @DisplayName("Test DG 34 Encode Stream 5 frames 16x16 mit Buchstabe eee")
    public void test34() throws IOException {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 2){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < 1){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= 1){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }
        }

    }

    @Test
    @DisplayName("Test DG 35 Encode Stream 5 frames 16x16 mit Buchstabe eeee")
    public void test35() throws IOException {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 2){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < 2){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= 2){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

        }

    }

    @Test
    @DisplayName("Test DG 36 Encode Stream 5 frames 16x16 mit Buchstabe eeeee")
    public void test36() throws IOException {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 3){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < 2){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= 2){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

        }

    }

    @Test
    @DisplayName("Test DG 37 Encode Stream 5 frames 16x16 mit Buchstabe eeeeeeeeee vollmachen")
    public void test37() throws IOException {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeeeeeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < amount){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < amount){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= amount){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

        }

    }

    @Test
    @DisplayName("Test DG 38 Encode Stream 5 frames 16x16 mit Buchstabe eeeeeeeeeeee overflow")
    public void test38() throws IOException {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeeeeeeeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < amount){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < amount){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= amount){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

        }

    }

    @Test
    @DisplayName("Test DG 39 Encode Stream 5 frames 16x16 mit null")
    public void test39() throws IOException {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode(null);

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 0){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < amount){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= amount){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

        }

    }

    @Test
    @DisplayName("Test DG 40 Encode Stream 3 frames 16x16 mit Buchstabe eeee")
    public void test40() throws IOException {
        int size = 16;
        int amount = 3;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 2){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < 2){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= 2){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

        }

    }

    @Test
    @DisplayName("Test DG 41 Encode Stream 3 frames 16x16 mit Buchstabe eeeee")
    public void test41() throws IOException {
        int size = 16;
        int amount = 3;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 3){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < 2){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= 2){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

            File outputfile = new File("test31OutputFrame"+i+".png");
            ImageIO.write(output[i].getPixels(), "PNG", outputfile);

        }

    }

    @Test
    @DisplayName("Test DG 42 Encode Stream 500 frames 16x16 mit Buchstabe eeee")
    public void test42() throws IOException {
        int size = 16;
        int amount = 500;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 2){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < 2){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= 2){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

        }

    }

    @Test
    @DisplayName("Test DG 43 Encode Stream 500 frames 16x16 mit Buchstabe eeeee")
    public void test43() throws IOException {
        int size = 16;
        int amount = 500;
        BufferedImage[] images = new BufferedImage[amount];
        for(int k = 0; k < amount; k++){
            images[k] = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    images[k].setRGB(i,j,Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for(int i = 0; i < frames.length; i++){
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for(int i = 0; i < amount; i++){
            try{
                output[i] = (Frame) outputObject[i];
            }
            catch(Exception e){
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for(int i = 0; i < amount; i++){
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if(i < 3){
                // encoding path
                for(int j = 0; j < 8; j++){
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    if(i < 2){
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8+j,output[i].getHeight()-1));
                    }
                }
                // rest
                for(int j = 8; j < output[i].getWidth(); j++){
                    if(i >= 2){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,output[i].getHeight()-1));
                    }
                }
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight() - 1; k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k));
                    }
                }
            }
            else{
                for(int j = 0; j < output[i].getWidth(); j++){
                    for(int k = 0; k < output[i].getHeight(); k++){
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j,k),"failed at frame "+i+" at index "+j+","+k);
                    }
                }
            }

        }

    }

    // OPERATIONS CROP TEST

    // beide erweitern

    @Test
    @DisplayName("Test DG 50 Crop x erweitern ungerade, y erweitern ungerade")
    public void test50() throws IOException {
        int size = 3;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size-2; j++){
                image.setRGB(j,i,Color.YELLOW.getRGB());
                imageCopy.setRGB(j,i,Color.YELLOW.getRGB());
            }
            for(int j = size-2; j < size; j++){
                image.setRGB(j,i,Color.RED.getRGB());
                imageCopy.setRGB(j,i,Color.RED.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int newHeight = 6;
        int newWidth = 6;
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(0, Color.BLACK.getRed());
        Assertions.assertEquals(0, Color.BLACK.getGreen());
        Assertions.assertEquals(0, Color.BLACK.getBlue());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        // balken drüber
        int endOben = 1;
        for(int i = 0; i < newWidth; i++){
            for(int j = 0; j < endOben; j++){
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
        // balken mitte
        int endMitte = 4;
        for(int i = 0; i < newWidth; i++){
            for(int j = endOben; j < endMitte; j++){
                if(i >= 1 && i <= 3){
                    Assertions.assertEquals(imageCopy.getRGB(i-1, j-1), output.getPixels().getRGB(i,j));
                }
                else{
                    Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
                }
            }
        }
        // balken danach
        for(int i = 0; i < newWidth; i++){
            for(int j = endMitte; j < newHeight; j++){
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test50Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);
    }

    @Test
    @DisplayName("Test DG 51 Crop x erweitern gerade, y erweitern gerade")
    public void test51() throws IOException {
        int size = 2;
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size-1; j++){
                image.setRGB(j,i,Color.YELLOW.getRGB());
                imageCopy.setRGB(j,i,Color.YELLOW.getRGB());
            }
            for(int j = size-1; j < size; j++){
                image.setRGB(j,i,Color.RED.getRGB());
                imageCopy.setRGB(j,i,Color.RED.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int newHeight = 6;
        int newWidth = 6;
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        // balken drüber
        int endOben = 2;
        for(int i = 0; i < newWidth; i++){
            for(int j = 0; j < endOben; j++){
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
        // balken mitte
        int endMitte = 4;
        for(int i = 0; i < newWidth; i++){
            for(int j = endOben; j < endMitte; j++){
                if(i >= 2 && i <= 3){
                    Assertions.assertEquals(imageCopy.getRGB(i-2, j-2), output.getPixels().getRGB(i,j), "failed at "+i+","+j);
                }
                else{
                    Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
                }
            }
        }
        // balken danach
        for(int i = 0; i < newWidth; i++){
            for(int j = endMitte; j < newHeight; j++){
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test51Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 52 Crop x erweitern ungerade, y erweitern gerade")
    public void test52() throws IOException {
        int sizeX = 3;
        int sizeY = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeY; i++){
            for(int j = 0; j < sizeX-2; j++){
                image.setRGB(j,i,Color.YELLOW.getRGB());
                imageCopy.setRGB(j,i,Color.YELLOW.getRGB());
            }
            for(int j = sizeX-2; j < sizeX; j++){
                image.setRGB(j,i,Color.RED.getRGB());
                imageCopy.setRGB(j,i,Color.RED.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int newHeight = 6;
        int newWidth = 6;
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        // balken drüber
        int endOben = 2;
        for(int i = 0; i < newWidth; i++){
            for(int j = 0; j < endOben; j++){
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
        // balken mitte
        int endMitte = 4;
        for(int i = 0; i < newWidth; i++){
            for(int j = endOben; j < endMitte; j++){
                if(i >= 1 && i <= 3){
                    Assertions.assertEquals(imageCopy.getRGB(i-1, j-2), output.getPixels().getRGB(i,j));
                }
                else{
                    Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
                }
            }
        }
        // balken danach
        for(int i = 0; i < newWidth; i++){
            for(int j = endMitte; j < newHeight; j++){
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test52Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 53 Crop x erweitern gerade, y erweitern ungerade")
    public void test53() throws IOException {
        int sizeX = 2;
        int sizeY = 3;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeY; i++){
            for(int j = 0; j < sizeX-1; j++){
                image.setRGB(j,i,Color.YELLOW.getRGB());
                imageCopy.setRGB(j,i,Color.YELLOW.getRGB());
            }
            for(int j = sizeX-1; j < sizeX; j++){
                image.setRGB(j,i,Color.RED.getRGB());
                imageCopy.setRGB(j,i,Color.RED.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int newHeight = 6;
        int newWidth = 6;
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        // balken drüber
        int endOben = 1;
        for(int i = 0; i < newWidth; i++){
            for(int j = 0; j < endOben; j++){
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
            }
        }
        // balken mitte
        int endMitte = 4;
        for(int i = 0; i < newWidth; i++){
            for(int j = endOben; j < endMitte; j++){
                if(i >= 2 && i <= 3){
                    Assertions.assertEquals(imageCopy.getRGB(i-2, j-1), output.getPixels().getRGB(i,j));
                }
                else{
                    Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
                }
            }
        }
        // balken danach
        for(int i = 0; i < newWidth; i++){
            for(int j = endMitte; j < newHeight; j++){
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test53Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    // beide kürzen

    @Test
    @DisplayName("Test DG 54 Crop x kürzen ungerade, y kürzen ungerade")
    public void test54() throws IOException {
        int sizeX = 6;
        int sizeY = 6;
        int newWidth = 3;
        int newHeight = 3;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 3){
                    if(j < 3){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 3){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1,1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0,2));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,2));

        File outputfile = new File("test54Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 55 Crop x kürzen gerade, y kürzen gerade")
    public void test55() throws IOException {
        int sizeX = 6;
        int sizeY = 6;
        int newWidth = 2;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 3){
                    if(j < 3){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 3){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1,0));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1,1));

        File outputfile = new File("test55Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 56 Crop x kürzen ungerade, y kürzen gerade")
    public void test56() throws IOException {
        int sizeX = 6;
        int sizeY = 6;
        int newWidth = 3;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 3){
                    if(j < 3){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 3){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,0));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1,1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,1));

        File outputfile = new File("test56Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 57 Crop x kürzen gerade, y kürzen ungerade")
    public void test57() throws IOException {
        int sizeX = 6;
        int sizeY = 6;
        int newWidth = 2;
        int newHeight = 3;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 3){
                    if(j < 3){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 3){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1,0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1,1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1,2));

        File outputfile = new File("test57Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    // x kürzen, y erweitern

    @Test
    @DisplayName("Test DG 58 Crop x kürzen ungerade, y erweitern ungerade")
    public void test58() throws IOException {
        int sizeX = 5;
        int sizeY = 2;
        int newWidth = 2;
        int newHeight = 5;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 2){
                    if(j < 1){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 1){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1,1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1,2));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,3));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,4));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,4));

        File outputfile = new File("test58Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 59 Crop x kürzen gerade, y erweitern gerade")
    public void test59() throws IOException {
        int sizeX = 5;
        int sizeY = 2;
        int newWidth = 3;
        int newHeight = 4;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 2){
                    if(j < 1){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 1){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2,0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1,1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,2));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2,3));

        File outputfile = new File("test59Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 60 Crop x kürzen ungerade, y erweitern gerade")
    public void test60() throws IOException {
        int sizeX = 5;
        int sizeY = 2;
        int newWidth = 2;
        int newHeight = 4;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 2){
                    if(j < 1){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 1){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1,1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1,2));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,3));

        File outputfile = new File("test60Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 61 Crop x kürzen gerade, y erweitern ungerade")
    public void test61() throws IOException {
        int sizeX = 5;
        int sizeY = 2;
        int newWidth = 3;
        int newHeight = 5;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 2){
                    if(j < 1){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 1){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2,0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1,1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,2));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2,3));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,4));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1,4));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2,4));

        File outputfile = new File("test61Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    // x erweitern, y kürzen

    @Test
    @DisplayName("Test DG 62 Crop x erweitern ungerade, y kürzen ungerade")
    public void test62() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 5;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4,0));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1,1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4,1));



        File outputfile = new File("test62Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 63 Crop x erweitern gerade, y kürzen gerade")
    public void test63() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 4;
        int newHeight = 3;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,0));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1,1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,1));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,2));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,2));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,2));


        File outputfile = new File("test63Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 64 Crop x erweitern ungerade, y kürzen gerade")
    public void test64() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 5;
        int newHeight = 3;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4,0));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1,1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4,1));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,2));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1,2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,2));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,2));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4,2));


        File outputfile = new File("test64Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    @Test
    @DisplayName("Test DG 65 Crop x erweitern gerade, y kürzen ungerade")
    public void test65() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 4;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);
        //Frame output = frame;
        Frame output = function.apply(frame);
        //System.out.println(Color.RED.getRGB());
        //System.out.println(Color.BLACK.getRGB());
        //System.out.println(Color.WHITE.getRGB());
        //System.out.println(Color.YELLOW.getRGB());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2,0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,0));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1,1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2,1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3,1));


        File outputfile = new File("test65Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);
        /*outputfile = new File("test50Original.png");
        ImageIO.write(imageCopy, "PNG", outputfile);*/
    }

    // edgecases

    @Test
    @DisplayName("Test DG 66 Crop width 0")
    public void test66() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 0;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);

        try{
            Frame output = function.apply(frame);
        }
        catch(Exception e){
            Assertions.fail();
        }



    }

    @Test
    @DisplayName("Test DG 67 Crop width -1")
    public void test67() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = -1;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);

        try{
            Frame output = function.apply(frame);
        }
        catch(Exception e){
            Assertions.fail();
        }



    }

    @Test
    @DisplayName("Test DG 68 Crop height 0")
    public void test68() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 3;
        int newHeight = 0;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);

        try{
            Frame output = function.apply(frame);
        }
        catch(Exception e){
            Assertions.fail();
        }



    }

    @Test
    @DisplayName("Test DG 69 Crop height -1")
    public void test69() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 3;
        int newHeight = -1;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);

        try{
            Frame output = function.apply(frame);
        }
        catch(Exception e){
            Assertions.fail();
        }



    }

    @Test
    @DisplayName("Test DG 70 Crop width und height 0")
    public void test70() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 0;
        int newHeight = 0;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);

        try{
            Frame output = function.apply(frame);
        }
        catch(Exception e){
            Assertions.fail();
        }



    }

    @Test
    @DisplayName("Test DG 71 Crop width und height -1")
    public void test71() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = -1;
        int newHeight = -1;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(i < 1){
                    if(j < 2){
                        // links oben
                        image.setRGB(i,j,Color.RED.getRGB());
                        imageCopy.setRGB(i,j,Color.RED.getRGB());
                    }
                    else{
                        // links unten
                        image.setRGB(i,j,Color.YELLOW.getRGB());
                        imageCopy.setRGB(i,j,Color.YELLOW.getRGB());
                    }
                }
                else{
                    if(j < 2){
                        // rechts oben
                        image.setRGB(i,j,Color.BLUE.getRGB());
                        imageCopy.setRGB(i,j,Color.BLUE.getRGB());
                    }
                    else{
                        // rechts unten
                        image.setRGB(i,j,Color.GREEN.getRGB());
                        imageCopy.setRGB(i,j,Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth,newHeight);

        try{
            Frame output = function.apply(frame);
        }
        catch(Exception e){
            Assertions.fail();
        }



    }

    // OPERATIONS GRAYSCALE TESTS

    @Test
    @DisplayName("Test DG 80 GrayScale Frame 3x3 yellow")
    public void test80() throws IOException {
        int sizeX = 3;
        int sizeY = 3;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.YELLOW.getRed());
        //System.out.println(Color.YELLOW.getGreen());
        //System.out.println(Color.YELLOW.getBlue());

        Assertions.assertEquals(number, output.getFrameNumber());

        Color gray = new Color(225,225,225);

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test80Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 81 GrayScale Frame 10x1 pink")
    public void test81() throws IOException {
        int sizeX = 10;
        int sizeY = 1;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.PINK.getRGB());
            }
        }
        Frame frame = new Frame(image, 5);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.PINK.getRed());
        //System.out.println(Color.PINK.getGreen());
        //System.out.println(Color.PINK.getBlue());

        Assertions.assertEquals(number, output.getFrameNumber());

        Color gray = new Color(198,198,198);

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test81Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 82 GrayScale Frame 2x5 black")
    public void test82() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.BLACK.getRGB());
            }
        }
        Frame frame = new Frame(image, 5);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.PINK.getRed());
        //System.out.println(Color.PINK.getGreen());
        //System.out.println(Color.PINK.getBlue());

        Assertions.assertEquals(number, output.getFrameNumber());

        Color gray = new Color(0,0,0);

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test82Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 83 GrayScale Frame 3x3 white")
    public void test83() throws IOException {
        int sizeX = 3;
        int sizeY = 3;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.WHITE.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.PINK.getRed());
        //System.out.println(Color.PINK.getGreen());
        //System.out.println(Color.PINK.getBlue());

        Assertions.assertEquals(number, output.getFrameNumber());

        Color gray = new Color(255,255,255);

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test83Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 84 GrayScale Frame 3x3 darkgray")
    public void test84() throws IOException {
        int sizeX = 3;
        int sizeY = 3;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.DARK_GRAY.getRed());
        //System.out.println(Color.DARK_GRAY.getGreen());
        //System.out.println(Color.DARK_GRAY.getBlue());

        Color gray = new Color(63,63,63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                //Assertions.assertEquals(Color.DARK_GRAY.getRGB(), output.getPixels().getRGB(i,j));
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test84Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 85 GrayScale Frame 1x1 darkgray")
    public void test85() throws IOException {
        int sizeX = 1;
        int sizeY = 1;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.DARK_GRAY.getRed());
        //System.out.println(Color.DARK_GRAY.getGreen());
        //System.out.println(Color.DARK_GRAY.getBlue());

        Color gray = new Color(63,63,63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                //Assertions.assertEquals(Color.DARK_GRAY.getRGB(), output.getPixels().getRGB(i,j));
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test85Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 86 GrayScale Frame 100x100 darkgray")
    public void test86() throws IOException {
        int sizeX = 100;
        int sizeY = 100;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.DARK_GRAY.getRed());
        //System.out.println(Color.DARK_GRAY.getGreen());
        //System.out.println(Color.DARK_GRAY.getBlue());

        Color gray = new Color(63,63,63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                //Assertions.assertEquals(Color.DARK_GRAY.getRGB(), output.getPixels().getRGB(i,j));
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test86Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 87 GrayScale Frame 2x50 darkgray")
    public void test87() throws IOException {
        int sizeX = 2;
        int sizeY = 50;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.DARK_GRAY.getRed());
        //System.out.println(Color.DARK_GRAY.getGreen());
        //System.out.println(Color.DARK_GRAY.getBlue());

        Color gray = new Color(63,63,63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                //Assertions.assertEquals(Color.DARK_GRAY.getRGB(), output.getPixels().getRGB(i,j));
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test87Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 88 GrayScale Frame 50x2 darkgray")
    public void test88() throws IOException {
        int sizeX = 50;
        int sizeY = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                image.setRGB(i,j,Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.DARK_GRAY.getRed());
        //System.out.println(Color.DARK_GRAY.getGreen());
        //System.out.println(Color.DARK_GRAY.getBlue());

        Color gray = new Color(63,63,63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                //Assertions.assertEquals(Color.DARK_GRAY.getRGB(), output.getPixels().getRGB(i,j));
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i,j));
            }
        }

        File outputfile = new File("test88Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 89 GrayScale Frame 2x2 mixed")
    public void test89() throws IOException {
        int sizeX = 2;
        int sizeY = 2;
        BufferedImage image = new BufferedImage(sizeX,sizeY,BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0,0,Color.YELLOW.getRGB());
        image.setRGB(1,0,Color.WHITE.getRGB());
        image.setRGB(0,1,Color.PINK.getRGB());
        image.setRGB(1,1,Color.DARK_GRAY.getRGB());
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        //System.out.println(Color.DARK_GRAY.getRed());
        //System.out.println(Color.DARK_GRAY.getGreen());
        //System.out.println(Color.DARK_GRAY.getBlue());

        Color gray1 = new Color(225,225,225);
        Color gray2 = new Color(255,255,255);
        Color gray3 = new Color(198, 198, 198);
        Color gray4 = new Color(63,63,63);

        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(gray1.getRGB(), output.getPixels().getRGB(0,0));
        Assertions.assertEquals(gray2.getRGB(), output.getPixels().getRGB(1,0));
        Assertions.assertEquals(gray3.getRGB(), output.getPixels().getRGB(0,1));
        Assertions.assertEquals(gray4.getRGB(), output.getPixels().getRGB(1,1));

        File outputfile = new File("test89Output.png");
        ImageIO.write(output.getPixels(), "PNG", outputfile);

    }

    @Test
    @DisplayName("Test DG 90 GrayScale Frame null")
    public void test90(){

        try{
            Frame output = Operations.grayscale(null);
        }
        catch(Exception e){
            Assertions.fail();
        }

    }

    // VIDEOCONTAINER KONSTRUKTOR TESTS

    @Test
    @DisplayName("Test DG 100 VideoContainer Konstruktor nood")
    public void test100() {

        try{
            VideoContainer in;
            FrameProvider fp = new FrameProvider("noot.mp4");
            in = new VideoContainer(fp);
        }
        catch(Exception e){
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("Test DG 101 VideoContainer Konstruktor nood2")
    public void test101() {

        try{
            VideoContainer in;
            FrameProvider fp = new FrameProvider("noot2.mp4");
            in = new VideoContainer(fp);
        }
        catch(FileNotFoundException fnfe){
            System.out.println("threw correct exception!");
        }
        catch(Exception e){
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("Test DG 102 VideoContainer Konstruktor width 0")
    public void test102() {
        MyBufferedImageWidthZero image = new MyBufferedImageWidthZero(3, 3, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                image.setRGB(i,j,Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        //System.out.println(provider.getWidth());
        //System.out.println(provider.getHeight());
        try{
            VideoContainer container = new VideoContainer(provider);
        }
        catch(IllegalVideoFormatException ivfe){
            System.out.println("correct exception thrown!");
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Test DG 103 VideoContainer Konstruktor width negative")
    public void test103() {
        MyBufferedImageWidthNegative image = new MyBufferedImageWidthNegative(3, 3, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                image.setRGB(i,j,Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        //System.out.println(provider.getWidth());
        //System.out.println(provider.getHeight());
        try{
            VideoContainer container = new VideoContainer(provider);
        }
        catch(IllegalVideoFormatException ivfe){
            System.out.println("correct exception thrown!");
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Test DG 104 VideoContainer Konstruktor height 0")
    public void test104() {
        MyBufferedImageHeightZero image = new MyBufferedImageHeightZero(3, 3, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                image.setRGB(i,j,Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        //System.out.println(provider.getWidth());
        //System.out.println(provider.getHeight());
        try{
            VideoContainer container = new VideoContainer(provider);
        }
        catch(IllegalVideoFormatException ivfe){
            System.out.println("correct exception thrown!");
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Test DG 105 VideoContainer Konstruktor height negative")
    public void test105() {
        MyBufferedImageHeightNegative image = new MyBufferedImageHeightNegative(3, 3, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                image.setRGB(i,j,Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        //System.out.println(provider.getWidth());
        //System.out.println(provider.getHeight());
        try{
            VideoContainer container = new VideoContainer(provider);
        }
        catch(IllegalVideoFormatException ivfe){
            System.out.println("correct exception thrown!");
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Test DG 106 VideoContainer Konstruktor both 0")
    public void test106() {
        MyBufferedImageBothZero image = new MyBufferedImageBothZero(3, 3, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                image.setRGB(i,j,Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        //System.out.println(provider.getWidth());
        //System.out.println(provider.getHeight());
        try{
            VideoContainer container = new VideoContainer(provider);
        }
        catch(IllegalVideoFormatException ivfe){
            System.out.println("correct exception thrown!");
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Test DG 107 VideoContainer Konstruktor both negative")
    public void test107() {
        MyBufferedImageBothNegative image = new MyBufferedImageBothNegative(3, 3, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                image.setRGB(i,j,Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        //System.out.println(provider.getWidth());
        //System.out.println(provider.getHeight());
        try{
            VideoContainer container = new VideoContainer(provider);
        }
        catch(IllegalVideoFormatException ivfe){
            System.out.println("correct exception thrown!");
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    // VIDEOCONTAINER LIMIT TESTS

    @Test
    @DisplayName("Test DG 120 VideoContainer Limit noot auf 100 Frames")
    public void test120() throws FileNotFoundException, IllegalVideoFormatException {

        int length = 100;

        FrameProvider fp = new FrameProvider("./noot.mp4");
        FrameProvider fp1 = new FrameProvider("./noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.frameStream.toArray();

        // limitiere Laufzeit
        in.limit(length);

        Object[] output = in.frameStream.toArray();

        Assertions.assertEquals(output.length, length);

        for(int i = 0; i < length; i++){
            for(int j = 0; j < fp.getWidth(); j++){
                for(int k = 0; k < fp.getHeight(); k++){
                    Assertions.assertEquals(((Frame) array[i]).getPixels().getRGB(j,k), ((Frame) output[i]).getPixels().getRGB(j,k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 121 VideoContainer Limit noot auf 10 Frames")
    public void test121() throws FileNotFoundException, IllegalVideoFormatException {

        int length = 10;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.frameStream.toArray();

        // limitiere Laufzeit
        in.limit(length);

        Object[] output = in.frameStream.toArray();

        Assertions.assertEquals(output.length, length);

        for(int i = 0; i < length; i++){
            for(int j = 0; j < fp.getWidth(); j++){
                for(int k = 0; k < fp.getHeight(); k++){
                    Assertions.assertEquals(((Frame) array[i]).getPixels().getRGB(j,k), ((Frame) output[i]).getPixels().getRGB(j,k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 122 VideoContainer Limit noot auf 2 Frames")
    public void test122() throws FileNotFoundException, IllegalVideoFormatException {

        int length = 2;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.frameStream.toArray();

        // limitiere Laufzeit
        in.limit(length);

        Object[] output = in.frameStream.toArray();

        Assertions.assertEquals(output.length, length);

        for(int i = 0; i < length; i++){
            for(int j = 0; j < fp.getWidth(); j++){
                for(int k = 0; k < fp.getHeight(); k++){
                    Assertions.assertEquals(((Frame) array[i]).getPixels().getRGB(j,k), ((Frame) output[i]).getPixels().getRGB(j,k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 123 VideoContainer Limit noot auf 1 Frames")
    public void test123() throws FileNotFoundException, IllegalVideoFormatException {

        int length = 1;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.frameStream.toArray();

        // limitiere Laufzeit
        in.limit(length);

        Object[] output = in.frameStream.toArray();

        Assertions.assertEquals(output.length, length);

        for(int i = 0; i < length; i++){
            for(int j = 0; j < fp.getWidth(); j++){
                for(int k = 0; k < fp.getHeight(); k++){
                    Assertions.assertEquals(((Frame) array[i]).getPixels().getRGB(j,k), ((Frame) output[i]).getPixels().getRGB(j,k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 124 VideoContainer Limit noot auf 0 Frames")
    public void test124() throws FileNotFoundException, IllegalVideoFormatException {

        int length = 0;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.frameStream.toArray();

        // limitiere Laufzeit

        try{
            in.limit(length);
        }
        catch(Exception e){
            Assertions.fail();
        }


        Object[] output = in.frameStream.toArray();

        Assertions.assertEquals(output.length, length);
    }

    @Test
    @DisplayName("Test DG 125 VideoContainer Limit noot auf -1 Frames")
    public void test125() throws FileNotFoundException, IllegalVideoFormatException {

        int length = -1;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.frameStream.toArray();

        // limitiere Laufzeit
        try{
            in.limit(length);
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Test DG 126 VideoContainer Limit noot auf -10 Frames")
    public void test126() throws FileNotFoundException, IllegalVideoFormatException {

        int length = -6;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.frameStream.toArray();

        // limitiere Laufzeit
        try{
            in.limit(length);
        }
        catch(Exception e){
            Assertions.fail();
        }
    }


}

class MyFrameProvider extends FrameProvider{

    private BufferedImage image;
    private int frameCount;

    public MyFrameProvider(BufferedImage image){
        this.image = image;
        this.frameCount = 0;
    }

    public boolean fileExists() {
        return true;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public double getFrameRate() {
        return 1;
    }

    public int getBitrate() {
        return 1;
    }

    public Frame nextFrame() throws FFmpegFrameGrabber.Exception {

        if (frameCount >= 1) {
            return null;
        }
        else{
            return new Frame(deepCopy(image), frameCount++);
        }

    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}

class MyBufferedImageHeightZero extends BufferedImage {
    public MyBufferedImageHeightZero(int width, int height, int imageType) {
        super(width, height, imageType);
    }
    @Override
    public int getHeight(){
        return 0;
    }
}

class MyBufferedImageHeightNegative extends BufferedImage {
    public MyBufferedImageHeightNegative(int width, int height, int imageType) {
        super(width, height, imageType);
    }
    @Override
    public int getHeight(){
        return -1;
    }
}

class MyBufferedImageWidthZero extends BufferedImage {
    public MyBufferedImageWidthZero(int width, int height, int imageType) {
        super(width, height, imageType);
    }
    @Override
    public int getWidth(){
        return 0;
    }
}

class MyBufferedImageWidthNegative extends BufferedImage {
    public MyBufferedImageWidthNegative(int width, int height, int imageType) {
        super(width, height, imageType);
    }
    @Override
    public int getWidth(){
        return -1;
    }
}

class MyBufferedImageBothZero extends BufferedImage {
    public MyBufferedImageBothZero(int width, int height, int imageType) {
        super(width, height, imageType);
    }
    @Override
    public int getWidth(){
        return 0;
    }
    @Override
    public int getHeight(){
        return 0;
    }
}

class MyBufferedImageBothNegative extends BufferedImage {
    public MyBufferedImageBothNegative(int width, int height, int imageType) {
        super(width, height, imageType);
    }
    @Override
    public int getWidth(){
        return -1;
    }
    @Override
    public int getHeight(){
        return -1;
    }
}

