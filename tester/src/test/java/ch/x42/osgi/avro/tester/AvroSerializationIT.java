package ch.x42.osgi.avro.tester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.IOException;
import java.util.Random;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;

import ch.x42.osgi.avro.api.AvroSerializer;
import ch.x42.osgi.avro.tester.generated.AvroTestUser;

@RunWith(PaxExam.class)
public class AvroSerializationIT {

	private final Random random = new Random(System.currentTimeMillis());

	@Inject
	private AvroSerializer serializer;

	@Configuration
	public Option[] config() {
		return options(
				junitBundles(),
				mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.2"),
				mavenBundle("ch.x42.osgi", "avro-osgi-services", T.TEST_BUNDLES_VERSION),
                bundle(T.BUNDLE_FILE.toURI().toString())
		);
	}

	@Test
	public void testSerialization() throws IOException {
        final AvroTestUser u = new AvroTestUser();
        u.setName("NAME_" + random.nextInt());
        u.setFavoriteNumber(random.nextInt());

        assertEquals("Expecting equals to work", u, u);

        final byte[] data = serializer.serialize(u);
        assertNotNull("Expecting non-null data", data);
        assertTrue("Expecting non-empty data", data.length > 0);

        final AvroTestUser copy = serializer.deserialize(AvroTestUser.SCHEMA$, data, AvroTestUser.class.getClassLoader());
        assertNotNull("Expecting non-null copy", copy);
        assertEquals("Expecting copy to match", u, copy);
	}
}
