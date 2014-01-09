package ch.x42.osgi.avro.tester;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.net.URL;
import java.util.Random;

import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.osgi.avro.protocols.ping.Ping;
import ch.x42.osgi.avro.protocols.ping.PingMessage;

@RunWith(PaxExam.class)
public class AvroIpcIT {

    public static final int HTTP_PORT = 9363;
    private final Random random = new Random(System.currentTimeMillis());
    private final Logger log = LoggerFactory.getLogger(getClass());
    
	@Configuration
	public Option[] config() {
	    // This sets the HTTP port used by the Felix OSGi HTTP service
	    System.setProperty("org.osgi.service.http.port", String.valueOf(HTTP_PORT));
	    
		return options(
				junitBundles(),
				mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.2"),
                mavenBundle("org.apache.felix", "org.apache.felix.http.jetty", "2.2.2"),
				mavenBundle("ch.x42.osgi", "avro-osgi-serializer", T.TEST_BUNDLES_VERSION),
                mavenBundle("ch.x42.osgi", "avro-osgi-rpc-protocol", T.TEST_BUNDLES_VERSION),
                mavenBundle("ch.x42.osgi", "avro-osgi-rpc-server", T.TEST_BUNDLES_VERSION),
                bundle(T.BUNDLE_FILE.toURI().toString())
		);
	}

	@Test
	public void testPingMessage() throws Exception {
	    // At this point our HTTP server with the /avro ping protocol
	    // should be running
	    // Test it as in the example at
	    // https://github.com/phunt/avro-rpc-quickstart/blob/master/src/main/java/example/Main.java
	    final URL url = new URL("http://localhost:" + HTTP_PORT + "/avro");
	    final HttpTransceiver client = new HttpTransceiver(url);
	    
	    final Ping proxy = (Ping)SpecificRequestor.getClient(Ping.class, client);
        System.out.println("Client built, got proxy");

        final PingMessage m = new PingMessage();
        final String from = "This is " + getClass().getSimpleName() + "_" + random.nextInt();
        final String comment = "Here's a test comment at " + random.nextInt();
        m.setFrom(from);
        m.setComment(comment);
        
        log.info("Sending ping: {} / {}", from, comment);
        final CharSequence result = proxy.ping(m);

        log.info("Got response: {}", result);
        assertTrue("Expecting correct prefix", result.toString().startsWith("PING FROM " + from));
        assertTrue("Expecting comment in response", result.toString().contains(comment));

	}
}