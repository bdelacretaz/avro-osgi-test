package ch.x42.osgi.avro;

import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

/** Use pax exam test runner */
@RunWith( JUnit4TestRunner.class )

/** Example test that runs with pax exam, just
 *  checks that BundleContext is supplied and that
 *  we can install bundles
 */
public class AvroSerializationTest {

    @Configuration
    public Option[] config()
    {
        return options(
            junitBundles(),
            felix().version("4.0.2")
        );
    }
    
    @Test
    public void testInTestBundle() {
    	
    }
}
