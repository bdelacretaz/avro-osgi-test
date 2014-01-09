package ch.x42.osgi.avro.api;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;

public interface AvroSerializer {
    /** Serialize the supplied record using Avro */
	<T extends SpecificRecord> byte[] serialize(T record) throws IOException;
	
	/** Deserialize the supplied data using Avro
	 * @param schema The Avro schema to use
	 * @param data The data to deserialize
	 * @param classLoader A ClassLoader that can supply the class of the deserialized object
	 * @return The deserialized object
	 * @throws IOException 
	 */
	<T extends SpecificRecord> T deserialize(Schema schema, byte [] data, ClassLoader classLoader) throws IOException;
}
