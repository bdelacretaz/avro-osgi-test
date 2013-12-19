package ch.x42.osgi.avro.api;

import org.apache.avro.specific.SpecificRecord;

public interface AvroSerializer {
	<T extends SpecificRecord> byte[] serialize(T record);
	<T extends SpecificRecord> T deserialize(byte [] data);
}
