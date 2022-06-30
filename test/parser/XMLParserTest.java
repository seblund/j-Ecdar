package parser;

import logic.Refinement;
import logic.SimpleTransitionSystem;
import models.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XMLParserTest {

    private static Automaton expected;
    private static List<List<Guard>> noGuards = new ArrayList<>();
    private static String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_1.dtd'><nta><declaration>// Place global declarations here.\n" +
            "broadcast chan i, o;</declaration><template><name>Test</name><declaration>clock x, y, z;</declaration><location id=\"id0\" x=\"-336\" y=\"-112\"><label kind=\"invariant\" x=\"-384\" y=\"-152\">y&lt;=50 &amp;&amp; z&lt;=40</label></location><location id=\"id1\" x=\"-104\" y=\"-32\"></location><location id=\"id2\" x=\"-336\" y=\"-32\"></location><init ref=\"id2\"/><transition action=\"\"><source ref=\"id1\"/><target ref=\"id2\"/><label kind=\"synchronisation\" x=\"-224\" y=\"48\">i?</label><nail x=\"-216\" y=\"48\"/></transition><transition controllable=\"false\" action=\"\"><source ref=\"id2\"/><target ref=\"id0\"/><label kind=\"synchronisation\" x=\"-368\" y=\"-88\">o!</label></transition><transition action=\"\"><source ref=\"id2\"/><target ref=\"id1\"/><label kind=\"guard\" x=\"-216\" y=\"-56\">x == 4</label><label kind=\"synchronisation\" x=\"-272\" y=\"-56\">i?</label><label kind=\"assignment\" x=\"-280\" y=\"-32\">x = 0, y = 0, z = 0</label></transition></template><template><name>G1</name><declaration>clock y;</declaration><location id=\"id3\" x=\"-336\" y=\"-112\"><label kind=\"invariant\" x=\"-352\" y=\"-152\">y&lt;=50</label></location><location id=\"id4\" x=\"-104\" y=\"-32\"></location><location id=\"id5\" x=\"-336\" y=\"-32\"></location><init ref=\"id5\"/><transition action=\"\"><source ref=\"id4\"/><target ref=\"id5\"/><label kind=\"synchronisation\" x=\"-216\" y=\"64\">i?</label><nail x=\"-216\" y=\"48\"/></transition><transition controllable=\"false\" action=\"\"><source ref=\"id5\"/><target ref=\"id3\"/><label kind=\"synchronisation\" x=\"-368\" y=\"-88\">o!</label></transition><transition action=\"\"><source ref=\"id5\"/><target ref=\"id4\"/><label kind=\"synchronisation\" x=\"-224\" y=\"-24\">i?</label></transition></template><template><name>G2</name><declaration>clock y;</declaration><location id=\"id6\" x=\"-336\" y=\"-112\"></location><location id=\"id7\" x=\"-104\" y=\"-32\"></location><location id=\"id8\" x=\"-336\" y=\"-32\"><label kind=\"invariant\" x=\"-352\" y=\"-16\">y&lt;=20</label></location><init ref=\"id8\"/><transition action=\"\"><source ref=\"id7\"/><target ref=\"id8\"/><label kind=\"synchronisation\" x=\"-216\" y=\"64\">i?</label><nail x=\"-216\" y=\"48\"/></transition><transition controllable=\"false\" action=\"\"><source ref=\"id8\"/><target ref=\"id6\"/><label kind=\"synchronisation\" x=\"-368\" y=\"-88\">o!</label></transition><transition action=\"\"><source ref=\"id8\"/><target ref=\"id7\"/><label kind=\"synchronisation\" x=\"-224\" y=\"-24\">i?</label></transition></template><template><name>G3</name><declaration>clock y;</declaration><location id=\"id9\" x=\"-336\" y=\"-112\"><label kind=\"invariant\" x=\"-352\" y=\"-152\">y&lt;=50</label></location><location id=\"id10\" x=\"-104\" y=\"-32\"></location><location id=\"id11\" x=\"-336\" y=\"-32\"><label kind=\"invariant\" x=\"-352\" y=\"-8\">y&lt;=20</label></location><init ref=\"id11\"/><transition action=\"\"><source ref=\"id10\"/><target ref=\"id11\"/><label kind=\"synchronisation\" x=\"-216\" y=\"64\">i?</label><nail x=\"-216\" y=\"48\"/></transition><transition controllable=\"false\" action=\"\"><source ref=\"id11\"/><target ref=\"id9\"/><label kind=\"synchronisation\" x=\"-368\" y=\"-88\">o!</label></transition><transition action=\"\"><source ref=\"id11\"/><target ref=\"id10\"/><label kind=\"synchronisation\" x=\"-224\" y=\"-24\">i?</label></transition></template><template><name>G4</name><declaration>clock y;</declaration><location id=\"id12\" x=\"-336\" y=\"-112\"><label kind=\"invariant\" x=\"-352\" y=\"-152\">y&lt;=50</label></location><location id=\"id13\" x=\"-104\" y=\"-32\"></location><location id=\"id14\" x=\"-336\" y=\"-32\"><label kind=\"invariant\" x=\"-368\" y=\"-16\">y&lt;=20</label></location><init ref=\"id14\"/><transition action=\"\"><source ref=\"id13\"/><target ref=\"id14\"/><label kind=\"synchronisation\" x=\"-216\" y=\"64\">i?</label><label kind=\"assignment\" x=\"-296\" y=\"16\">y=0</label><nail x=\"-216\" y=\"48\"/></transition><transition controllable=\"false\" action=\"\"><source ref=\"id14\"/><target ref=\"id12\"/><label kind=\"synchronisation\" x=\"-368\" y=\"-88\">o!</label></transition><transition action=\"\"><source ref=\"id14\"/><target ref=\"id13\"/><label kind=\"synchronisation\" x=\"-224\" y=\"-24\">i?</label><label kind=\"assignment\" x=\"-168\" y=\"-64\">y=0</label></transition></template><template><name>G5</name><declaration>clock y;</declaration><location id=\"id15\" x=\"-104\" y=\"-32\"></location><location id=\"id16\" x=\"-336\" y=\"-32\"><label kind=\"invariant\" x=\"-352\" y=\"-16\">y&lt;=20</label></location><init ref=\"id16\"/><transition action=\"\"><source ref=\"id16\"/><target ref=\"id15\"/><label kind=\"synchronisation\" x=\"-224\" y=\"-24\">i?</label></transition></template><template><name>G6</name><declaration>clock y;</declaration><location id=\"id17\" x=\"80\" y=\"24\"></location><location id=\"id18\" x=\"-248\" y=\"24\"></location><location id=\"id19\" x=\"-88\" y=\"24\"><label kind=\"invariant\" x=\"-112\" y=\"40\">y&lt;=20</label></location><init ref=\"id19\"/><transition controllable=\"false\" action=\"\"><source ref=\"id19\"/><target ref=\"id17\"/><label kind=\"guard\" x=\"-56\" y=\"24\">y&gt;4 &amp;&amp; y&lt;=8</label><label kind=\"synchronisation\" x=\"-24\" y=\"0\">o!</label></transition><transition controllable=\"false\" action=\"\"><source ref=\"id19\"/><target ref=\"id18\"/><label kind=\"guard\" x=\"-208\" y=\"24\">y&gt;=2 &amp;&amp; y&lt;=4</label><label kind=\"synchronisation\" x=\"-176\" y=\"0\">o!</label></transition></template><template><name>G7</name><declaration>clock y;</declaration><location id=\"id20\" x=\"80\" y=\"24\"></location><location id=\"id21\" x=\"-248\" y=\"24\"></location><location id=\"id22\" x=\"-88\" y=\"24\"><label kind=\"invariant\" x=\"-104\" y=\"40\">y&lt;=20</label></location><init ref=\"id22\"/><transition action=\"\"><source ref=\"id22\"/><target ref=\"id20\"/><label kind=\"guard\" x=\"-56\" y=\"24\">y&gt;4 &amp;&amp; y&lt;=8</label><label kind=\"synchronisation\" x=\"-24\" y=\"0\">i?</label></transition><transition action=\"\"><source ref=\"id22\"/><target ref=\"id21\"/><label kind=\"guard\" x=\"-208\" y=\"24\">y&gt;=2 &amp;&amp; y&lt;=4</label><label kind=\"synchronisation\" x=\"-176\" y=\"0\">i?</label></transition></template><template><name>G8</name><declaration>clock y;</declaration><location id=\"id23\" x=\"80\" y=\"24\"></location><location id=\"id24\" x=\"-248\" y=\"24\"></location><location id=\"id25\" x=\"-88\" y=\"24\"></location><init ref=\"id25\"/><transition action=\"\"><source ref=\"id25\"/><target ref=\"id23\"/><label kind=\"guard\" x=\"-56\" y=\"24\">y&gt;4 &amp;&amp; y&lt;=8</label><label kind=\"synchronisation\" x=\"-24\" y=\"0\">i?</label></transition><transition action=\"\"><source ref=\"id25\"/><target ref=\"id24\"/><label kind=\"guard\" x=\"-208\" y=\"24\">y&gt;=2 &amp;&amp; y&lt;=4</label><label kind=\"synchronisation\" x=\"-176\" y=\"0\">i?</label></transition></template><template><name>G9</name><declaration>clock y;</declaration><location id=\"id26\" x=\"80\" y=\"24\"></location><location id=\"id27\" x=\"-248\" y=\"24\"></location><location id=\"id28\" x=\"-88\" y=\"24\"></location><init ref=\"id28\"/><transition action=\"\"><source ref=\"id28\"/><target ref=\"id26\"/><label kind=\"guard\" x=\"-56\" y=\"24\">y&gt;=4 &amp;&amp; y&lt;=8</label><label kind=\"synchronisation\" x=\"-24\" y=\"0\">i?</label></transition><transition action=\"\"><source ref=\"id28\"/><target ref=\"id27\"/><label kind=\"guard\" x=\"-208\" y=\"24\">y&gt;=2 &amp;&amp; y&lt;=4</label><label kind=\"synchronisation\" x=\"-176\" y=\"0\">i?</label></transition></template><template><name>G10</name><declaration>clock y;</declaration><location id=\"id29\" x=\"80\" y=\"24\"></location><location id=\"id30\" x=\"-248\" y=\"24\"><label kind=\"invariant\" x=\"-264\" y=\"40\">y&lt;=50</label></location><location id=\"id31\" x=\"-88\" y=\"24\"></location><init ref=\"id31\"/><transition action=\"\"><source ref=\"id31\"/><target ref=\"id29\"/><label kind=\"guard\" x=\"-56\" y=\"24\">y&gt;4 &amp;&amp; y&lt;=8</label><label kind=\"synchronisation\" x=\"-24\" y=\"0\">i?</label></transition><transition action=\"\"><source ref=\"id31\"/><target ref=\"id30\"/><label kind=\"guard\" x=\"-208\" y=\"24\">y&gt;=2 &amp;&amp; y&lt;=4</label><label kind=\"synchronisation\" x=\"-176\" y=\"0\">i?</label></transition></template><template><name>G11</name><declaration>clock y;</declaration><location id=\"id32\" x=\"24\" y=\"24\"></location><location id=\"id33\" x=\"-136\" y=\"-96\"><label kind=\"invariant\" x=\"-152\" y=\"-128\">y&lt;=30</label></location><location id=\"id34\" x=\"-136\" y=\"24\"><label kind=\"invariant\" x=\"-146\" y=\"39\">y&lt;=20</label></location><init ref=\"id34\"/><transition controllable=\"false\" action=\"\"><source ref=\"id33\"/><target ref=\"id34\"/><label kind=\"guard\" x=\"-88\" y=\"-40\">y&gt;=25</label><label kind=\"synchronisation\" x=\"-96\" y=\"-64\">o!</label><nail x=\"-96\" y=\"-32\"/></transition><transition controllable=\"false\" action=\"\"><source ref=\"id34\"/><target ref=\"id33\"/><label kind=\"synchronisation\" x=\"-160\" y=\"-48\">o!</label><nail x=\"-136\" y=\"8\"/></transition><transition controllable=\"false\" action=\"\"><source ref=\"id34\"/><target ref=\"id32\"/><label kind=\"guard\" x=\"-64\" y=\"24\">y&gt;20</label><label kind=\"synchronisation\" x=\"-64\" y=\"0\">o!</label></transition></template><template><name>G12</name><declaration>clock y;</declaration><location id=\"id35\" x=\"48\" y=\"-128\"><label kind=\"invariant\" x=\"24\" y=\"-160\">y&lt;=20</label></location><location id=\"id36\" x=\"-128\" y=\"-128\"></location><location id=\"id37\" x=\"48\" y=\"16\"></location><location id=\"id38\" x=\"-136\" y=\"16\"></location><init ref=\"id38\"/><transition action=\"\"><source ref=\"id36\"/><target ref=\"id37\"/><label kind=\"synchronisation\" x=\"-64\" y=\"-128\">i?</label><nail x=\"-16\" y=\"-88\"/></transition><transition action=\"\"><source ref=\"id37\"/><target ref=\"id36\"/><label kind=\"guard\" x=\"-120\" y=\"-88\">y&gt;4</label><label kind=\"synchronisation\" x=\"-88\" y=\"-56\">i?</label><label kind=\"assignment\" x=\"-72\" y=\"-40\">y=0</label><nail x=\"-64\" y=\"-48\"/></transition><transition action=\"\"><source ref=\"id37\"/><target ref=\"id35\"/><label kind=\"guard\" x=\"56\" y=\"-88\">y&lt;=4</label><label kind=\"synchronisation\" x=\"32\" y=\"-88\">i?</label></transition><transition action=\"\"><source ref=\"id38\"/><target ref=\"id37\"/><label kind=\"guard\" x=\"-40\" y=\"16\">y&gt;=5</label><label kind=\"synchronisation\" x=\"-80\" y=\"16\">i?</label></transition></template><template><name>G13</name><declaration>clock y;</declaration><location id=\"id39\" x=\"48\" y=\"-128\"></location><location id=\"id40\" x=\"-128\" y=\"-128\"></location><location id=\"id41\" x=\"48\" y=\"16\"></location><location id=\"id42\" x=\"-136\" y=\"16\"></location><init ref=\"id42\"/><transition action=\"\"><source ref=\"id40\"/><target ref=\"id41\"/><label kind=\"synchronisation\" x=\"-64\" y=\"-128\">i?</label><nail x=\"-16\" y=\"-88\"/></transition><transition action=\"\"><source ref=\"id41\"/><target ref=\"id40\"/><label kind=\"guard\" x=\"-120\" y=\"-88\">y&gt;4</label><label kind=\"synchronisation\" x=\"-88\" y=\"-56\">i?</label><label kind=\"assignment\" x=\"-72\" y=\"-40\">y=0</label><nail x=\"-64\" y=\"-48\"/></transition><transition action=\"\"><source ref=\"id41\"/><target ref=\"id39\"/><label kind=\"guard\" x=\"56\" y=\"-88\">y&lt;=4</label><label kind=\"synchronisation\" x=\"32\" y=\"-88\">i?</label></transition><transition action=\"\"><source ref=\"id42\"/><target ref=\"id41\"/><label kind=\"guard\" x=\"-40\" y=\"16\">y&gt;=5</label><label kind=\"synchronisation\" x=\"-80\" y=\"16\">i?</label></transition></template><template><name>G14</name><declaration>clock y;</declaration><location id=\"id43\" x=\"8\" y=\"32\"></location><location id=\"id44\" x=\"-152\" y=\"-96\"></location><location id=\"id45\" x=\"-152\" y=\"32\"></location><init ref=\"id45\"/><transition controllable=\"false\" action=\"\"><source ref=\"id45\"/><target ref=\"id43\"/><label kind=\"guard\" x=\"-96\" y=\"8\">y&gt;=4</label><label kind=\"synchronisation\" x=\"-48\" y=\"8\">o!</label></transition><transition controllable=\"false\" action=\"\"><source ref=\"id45\"/><target ref=\"id44\"/><label kind=\"guard\" x=\"-192\" y=\"-48\">y&lt;=5</label><label kind=\"synchronisation\" x=\"-176\" y=\"-24\">o!</label></transition></template><template><name>G15</name><declaration>clock y;</declaration><location id=\"id46\" x=\"8\" y=\"32\"></location><location id=\"id47\" x=\"-152\" y=\"-96\"></location><location id=\"id48\" x=\"-152\" y=\"32\"><label kind=\"invariant\" x=\"-168\" y=\"48\">y&lt;=3</label></location><init ref=\"id48\"/><transition controllable=\"false\" action=\"\"><source ref=\"id48\"/><target ref=\"id46\"/><label kind=\"guard\" x=\"-96\" y=\"8\">y&gt;=4</label><label kind=\"synchronisation\" x=\"-48\" y=\"8\">o!</label></transition><transition controllable=\"false\" action=\"\"><source ref=\"id48\"/><target ref=\"id47\"/><label kind=\"guard\" x=\"-192\" y=\"-48\">y&lt;=5</label><label kind=\"synchronisation\" x=\"-176\" y=\"-24\">o!</label></transition></template><template><name>G16</name><declaration>clock y;</declaration><location id=\"id49\" x=\"8\" y=\"32\"></location><location id=\"id50\" x=\"-152\" y=\"-96\"></location><location id=\"id51\" x=\"-152\" y=\"32\"><label kind=\"invariant\" x=\"-168\" y=\"48\">y&lt;=1</label></location><init ref=\"id51\"/><transition controllable=\"false\" action=\"\"><source ref=\"id51\"/><target ref=\"id49\"/><label kind=\"guard\" x=\"-96\" y=\"8\">y&gt;=4</label><label kind=\"synchronisation\" x=\"-48\" y=\"8\">o!</label></transition><transition controllable=\"false\" action=\"\"><source ref=\"id51\"/><target ref=\"id50\"/><label kind=\"guard\" x=\"-192\" y=\"-48\">y&lt;=1</label><label kind=\"synchronisation\" x=\"-176\" y=\"-24\">o!</label></transition></template><template><name>G17</name><declaration>clock y;</declaration><location id=\"id52\" x=\"8\" y=\"32\"></location><location id=\"id53\" x=\"-152\" y=\"-96\"></location><location id=\"id54\" x=\"-152\" y=\"32\"><label kind=\"invariant\" x=\"-168\" y=\"48\">y&lt;=1</label></location><init ref=\"id54\"/><transition controllable=\"false\" action=\"\"><source ref=\"id54\"/><target ref=\"id52\"/><label kind=\"guard\" x=\"-96\" y=\"8\">y&gt;=4</label><label kind=\"synchronisation\" x=\"-48\" y=\"8\">o!</label></transition><transition controllable=\"false\" action=\"\"><source ref=\"id54\"/><target ref=\"id53\"/><label kind=\"guard\" x=\"-192\" y=\"-48\">y&gt;=1</label><label kind=\"synchronisation\" x=\"-176\" y=\"-24\">o!</label></transition></template><template><name>G18</name><declaration>clock y;</declaration><location id=\"id55\" x=\"-48\" y=\"-64\"></location><location id=\"id56\" x=\"-232\" y=\"-64\"></location><location id=\"id57\" x=\"48\" y=\"56\"></location><location id=\"id58\" x=\"-144\" y=\"56\"></location><init ref=\"id58\"/><transition controllable=\"false\" action=\"\"><source ref=\"id58\"/><target ref=\"id56\"/><label kind=\"guard\" x=\"-248\" y=\"-32\">y&gt;=7</label><label kind=\"synchronisation\" x=\"-216\" y=\"-8\">o!</label></transition><transition controllable=\"false\" action=\"\"><source ref=\"id58\"/><target ref=\"id55\"/><label kind=\"guard\" x=\"-104\" y=\"-40\">y&lt;7</label><label kind=\"synchronisation\" x=\"-112\" y=\"-24\">o!</label><nail x=\"-136\" y=\"48\"/></transition><transition action=\"\"><source ref=\"id58\"/><target ref=\"id57\"/><label kind=\"synchronisation\" x=\"-56\" y=\"32\">i?</label></transition></template><system>// Place template instantiations here.\n" +
            "\n" +
            "// List one or more processes to be composed into a system.\n" +
            "system Test, G1, G2, G3, G4, G5, G6, G7, G8, G9, G10,\n" +
            " G11, G12, G13, G14, G15, G16, G17, G18;\n" +
            "\n" +
            "IO G1 { i?, o! }\n" +
            "IO G2 { i?, o! }\n" +
            "IO G3 { i?, o! }\n" +
            "IO G4 { i?, o! }\n" +
            "IO G5 { i?, o! }\n" +
            "IO G6 { i?, o! }\n" +
            "IO G7 { i?, o! }\n" +
            "IO G8 { i?, o! }\n" +
            "IO G9 { i?, o! }\n" +
            "IO G10 { i?, o! }\n" +
            "IO G11 { i?, o! }\n" +
            "IO G12 { i?, o! }\n" +
            "IO G13 { i?, o! }\n" +
            "IO G14 { i?, o! }\n" +
            "IO G15 { i?, o! }\n" +
            "IO G16 { i?, o! }\n" +
            "IO G17 { i?, o! }\n" +
            "IO G18 { i?, o! }</system></nta>";

    @BeforeClass
    public static void setUpBeforeClass() {
        Clock x = new Clock("x", "Aut");
        Clock y = new Clock("y", "Aut");
        Clock z = new Clock("z", "Aut");
        List<Clock> clocks = new ArrayList<>(Arrays.asList(x, y, z));

        Channel i = new Channel("i");
        Channel o = new Channel("o");


        ClockGuard inv0_0 = new ClockGuard(y, 50, Relation.LESS_EQUAL);
        ClockGuard inv0_1 = new ClockGuard(z, 40,  Relation.LESS_EQUAL);

        Location l0 = new Location("id0", new AndGuard(inv0_0, inv0_1), false, false, false, false);
        Location l1 = new Location("id1", new TrueGuard(), false, false, false, false);
        Location l2 = new Location("id2", new TrueGuard(), true, false, false, false);
        List<Location> locations = new ArrayList<>(Arrays.asList(l0, l1, l2));

        ClockGuard g2 = new ClockGuard(x, 4, Relation.EQUAL);
        ClockUpdate u2_0 = new ClockUpdate(x, 0);
        ClockUpdate u2_1 = new ClockUpdate(y, 0);
        ClockUpdate u2_2 = new ClockUpdate(z, 0);
        Edge e0 = new Edge(l1, l2, i, true, new TrueGuard(), new ArrayList<>());
        Edge e1 = new Edge(l2, l0, o, false, new TrueGuard(), new ArrayList<>());
        Edge e2 = new Edge(l2, l1, i, true, g2, new ArrayList<>(){{add(u2_0); add(u2_1); add(u2_2);}});
        List<Edge> edges = new ArrayList<>(Arrays.asList(e0, e1, e2));
        ArrayList BVs = new ArrayList<>();
        expected = new Automaton("Test", locations, edges, clocks, BVs,  false);
    }

    @Test
    public void testAutomaton() {
        Automaton actual = XMLParser.parse("./samples/xml/ImplTests.xml", false)[0];
        assert(new Refinement(new SimpleTransitionSystem(expected),new SimpleTransitionSystem(actual)).check());
        assert(new Refinement(new SimpleTransitionSystem(actual),new SimpleTransitionSystem(expected)).check());
    }

    @Test
    public void testParseXmlString(){
        Automaton actual = XMLParser.parseXmlString(xmlString, false)[0];

        assert(new Refinement(new SimpleTransitionSystem(expected),new SimpleTransitionSystem(actual)).check());
        assert(new Refinement(new SimpleTransitionSystem(actual),new SimpleTransitionSystem(expected)).check());
    }
}
