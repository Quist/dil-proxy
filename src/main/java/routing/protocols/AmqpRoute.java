package routing.protocols;

import config.AmqpConfig;
import config.DilProxyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AmqpRoute extends DilRouteBuilder {
    private final static Logger logger = LoggerFactory.getLogger(AmqpRoute.class);

    private final AmqpConfig amqpConfig;
    private final Optional<Long> timeout;

    public AmqpRoute(DilProxyConfig config) {
        this.amqpConfig = config.getAmqpConfig();
        this.timeout = config.getTimeout();
    }

    @Override
    public String getToUri() {
        String options = "";

        if (timeout.isPresent()) {
            logger.info("Using " + timeout.get() + " ms as timeout for AMQP requests");
            options = "requestTimeout=" + timeout.get();
        } else {
            logger.info("No timeout value provided in config. Using AMQP default");
        }

        return String.format("amqp:queue:%s?%s", amqpConfig.getProduceQueue(), options);
    }

    @Override
    public String getListenUri() {
        return "amqp:queue:" + amqpConfig.getConsumeQueue();
    }

}
