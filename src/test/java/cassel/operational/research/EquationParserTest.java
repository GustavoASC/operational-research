/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Class to test equation parse
 *
 * @author gustavo
 */
public class EquationParserTest {

    @Test
    public void testParse() {
        assertEquals(7.0, new EquationParser().parse("5 + 2"));
        assertEquals(8.0, new EquationParser().parse("5 + 3"));
        assertEquals(8.0, new EquationParser().parse("3 + 5"));
        assertEquals(8.0, new EquationParser().parse("3 + 5"));
        assertEquals(10.0, new EquationParser().parse("2 * 5"));
        assertEquals(33.0, new EquationParser().parse("7 * 5 - 2"));
    }

}
