package features;

import logic.Conjunction;
import logic.SimpleTransitionSystem;
import logic.TransitionSystem;
import models.Automaton;
import models.BoolVar;
import org.junit.Test;
import parser.XMLParser;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoolVarNamingTest {

    @Test
    public void testBooleanConjunction()
    {
        Automaton auts[] = XMLParser.parse("samples/xml/booleanRefinement.xml",false);
        Conjunction conjunction = new Conjunction(new TransitionSystem[]{new SimpleTransitionSystem(auts[2]), new SimpleTransitionSystem(auts[1])});

        List<String> names = conjunction.getBVs().stream().map(BoolVar::getUniqueName).collect(Collectors.toList());

        assertEquals(2, names.size());
        assertTrue("Template.a", names.contains("Template.a"));
        assertTrue("Template1.a", names.contains("Template1.a"));
    }
}
