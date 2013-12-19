package ch.x42.osgi.avro.impl;

import org.apache.avro.specific.SpecificRecord;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import ch.x42.osgi.avro.api.AvroSerializer;

@Component
@Service
public class AvrolSerializerImpl implements AvroSerializer {

	@Override
	public <T extends SpecificRecord> byte[] serialize(T record) {
		return new byte[] { 0, 1 };
	}

	@Override
	public <T extends SpecificRecord> T deserialize(byte[] data) {
		return null;
	}

}
