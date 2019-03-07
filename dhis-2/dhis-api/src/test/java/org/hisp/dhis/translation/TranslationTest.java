package org.hisp.dhis.translation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TranslationTest
{

    @Test
    public void testNotEquals()
    {
        Translation translation1 = new Translation( "en", TranslationProperty.DESCRIPTION, "test" );
        Translation translation2 = new Translation( "pt", TranslationProperty.DESCRIPTION, "test_pt" );

        assertNotEquals( translation1, translation2 );
    }

    @Test
    public void testEquals()
    {
        Translation translation1 = new Translation( "en", TranslationProperty.DESCRIPTION, "test" );
        Translation translation2 = new Translation( "en", TranslationProperty.DESCRIPTION, "test_pt" );

        assertEquals( translation1, translation2 );
    }
}