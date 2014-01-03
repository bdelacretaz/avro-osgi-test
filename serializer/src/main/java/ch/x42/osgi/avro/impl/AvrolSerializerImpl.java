package ch.x42.osgi.avro.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import ch.x42.osgi.avro.api.AvroSerializer;

@Component
@Service
public class AvrolSerializerImpl implements AvroSerializer {

	@Override
	public <T extends SpecificRecord> byte[] serialize(T record) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final Encoder encoder = EncoderFactory.get().binaryEncoder(os, null);
        new SpecificDatumWriter<T>(record.getSchema()).write(record, encoder);
        encoder.flush();
        return os.toByteArray();
	}

	@SuppressWarnings("unchecked")
    @Override
	public <T extends SpecificRecord> T deserialize(Schema schema, byte[] data) throws IOException {
        if(data == null) {
            throw new IllegalArgumentException("Null data");
        }
        final ByteArrayInputStream stream = new ByteArrayInputStream(data);
        final Decoder decoder = DecoderFactory.get().binaryDecoder(stream, null);
        final SpecificDatumReader<T> reader = new SpecificDatumReader<T>(schema);
        final Object result = reader.read(null, decoder);
        if(!(result instanceof SpecificRecord)) {
            throw new IOException(
                    "Expected a SpecificRecord, got a " + result.getClass().getName()
                    + " - this is usually caused by the avro deserializer not finding "
                    + "the class that's being deserialized."
                    );
        }
        return (T)result;
	}

}
