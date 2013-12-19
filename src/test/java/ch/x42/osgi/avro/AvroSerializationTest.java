package ch.x42.osgi.avro;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;

@RunWith(PaxExam.class)
public class AvroSerializationTest {

    @Configuration
    public Option[] config()
    {
        return options(
            junitBundles()
        );
    }
    
    @Test
    public void testInTestBundle() {
    	
    }
}
