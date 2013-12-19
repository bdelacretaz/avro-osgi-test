package ch.x42.osgi.avro.api;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;

public interface AvroSerializer {
	<T extends SpecificRecord> byte[] serialize(T record) throws IOException;
	<T extends SpecificRecord> T deserialize(Schema schema, byte [] data) throws IOException;
}
