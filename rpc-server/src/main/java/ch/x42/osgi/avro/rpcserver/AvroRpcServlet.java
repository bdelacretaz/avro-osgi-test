package ch.x42.osgi.avro.rpcserver;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.ResponderServlet;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.osgi.avro.protocols.ping.Ping;
import ch.x42.osgi.avro.protocols.ping.PingMessage;

/** Servlet that uses the Avro IPC mechanism to implement
 *  a simple "ping" protocol. The Avro protocol classes come
 *  from a different bundle, to verify that this works in an
 *  OSGi environment.
 */
@Component
public class AvroRpcServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final String PATH = "/avro";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Servlet avroServlet;
    
    public static class PingImpl implements Ping {
        @Override
        public CharSequence ping(PingMessage ping) throws AvroRemoteException {
            return "PING FROM " + ping.getFrom() 
                + " COMMENT " + ping.getComment()
                + " FROM " + getClass().getName()
                + " AT " + new Date();
        }
    }
    
    @Reference
    private HttpService httpService;
    
    @Activate
    protected void activate(ComponentContext ctx) throws Exception {
        avroServlet = new ResponderServlet(new SpecificResponder(Ping.class, new PingImpl()));
        httpService.registerServlet(PATH, this, null, null);
        log.info("Registered {} on {}", this, PATH);
    }
    
    @Deactivate
    protected void deactivate(ComponentContext ctx) {
        httpService.unregister(PATH);
        log.info("Unregistered {}", PATH);
        avroServlet = null;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Delegating to {}", avroServlet);
        final ClassLoader oldTCCL = Thread.currentThread().getContextClassLoader();
        try {
            // With our AVRO-1425 patch, setting the TCCL lets Avro's SpecificData
            // class find the Ping protocol and PingImpl classes
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            avroServlet.service(req,  resp);
        } finally {
            Thread.currentThread().setContextClassLoader(oldTCCL);
        }
    }
}
