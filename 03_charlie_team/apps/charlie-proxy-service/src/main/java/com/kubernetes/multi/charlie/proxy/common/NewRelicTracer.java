package com.kubernetes.multi.charlie.proxy.common;

import com.newrelic.api.agent.ConcurrentHashMapHeaders;
import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.NewRelic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class NewRelicTracer {

    private final Logger logger = LoggerFactory.getLogger(NewRelicTracer.class);

    public NewRelicTracer() {}

    public void track(
        ProducerRecord<String, String> record
    ) {

        // Trace ID
        var traceId = NewRelic.getAgent().getTraceMetadata().getTraceId();
        logger.info("Trace ID: " + traceId);

        // Span ID
        var spanId = NewRelic.getAgent().getTraceMetadata().getSpanId();
        logger.info("Span ID: " + spanId);

        // Linking Metadata
        var linkingMetadata = NewRelic.getAgent().getLinkingMetadata();
        logger.info("Linking Metadata: " + linkingMetadata.toString());

        // Add New Relic headers
        var newrelicHeaders = ConcurrentHashMapHeaders.build(HeaderType.MESSAGE);
        NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(newrelicHeaders);

        // Retrieve the generated W3C Trace Context headers and
        // insert them into the ProducerRecord headers
        if (newrelicHeaders.containsHeader("traceparent")) {
            record.headers().add("traceparent",
                    newrelicHeaders.getHeader("traceparent")
                            .getBytes(StandardCharsets.UTF_8));
        }

        if (newrelicHeaders.containsHeader("tracestate")) {
            record.headers().add("tracestate",
                    newrelicHeaders.getHeader("tracestate")
                            .getBytes(StandardCharsets.UTF_8));
        }
    }
}
