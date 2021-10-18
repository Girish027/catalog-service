package com.tfs.dp2.catalog.monitoring;


import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(basePackages = {"com.tfs.dp2.catalog.view"})
@EnableMetrics
public class MonitoringService  extends MetricsConfigurerAdapter  {


    @Autowired
    private MonitoringConfiguration monitoringConfiguration;

    @Autowired
    private MetricRegistry registry;

    private String graphitePrefix;

    private static final String GRAPHITE_PREFIX = "dp2x.catalog";

    protected void configureReporters(String graphitePrefix) {
        this.graphitePrefix = graphitePrefix;
        configureReporters(registry);

    }


    @PostConstruct()
    public void init() {

        configureReporters(GRAPHITE_PREFIX);
    }


    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        //  readApplicationProperties();

        registerReporter(JmxReporter.forRegistry(metricRegistry)
                .build()).start();
        GraphiteReporter graphiteReporter =
                getGraphiteReporterBuilder(metricRegistry)
                        .build(getGraphite());
        registerReporter(graphiteReporter);
        graphiteReporter.start(monitoringConfiguration.getPollingtime(),
                TimeUnit.MILLISECONDS);
    }

    private GraphiteReporter.Builder getGraphiteReporterBuilder(MetricRegistry
                                                                        metricRegistry) {
        metricRegistry.register("gc", new GarbageCollectorMetricSet());
        metricRegistry.register("memory", new MemoryUsageGaugeSet());
        metricRegistry.register("threads", new ThreadStatesGaugeSet());
        return GraphiteReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .prefixedWith(graphitePrefix);
    }


    private Graphite getGraphite() {
        return new Graphite(new InetSocketAddress(monitoringConfiguration.getHost(),
                monitoringConfiguration.getPort()));
    }
}
