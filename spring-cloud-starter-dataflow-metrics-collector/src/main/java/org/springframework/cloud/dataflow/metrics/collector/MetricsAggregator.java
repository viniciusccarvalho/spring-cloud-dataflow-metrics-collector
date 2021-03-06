/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.metrics.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.dataflow.metrics.collector.model.ApplicationMetrics;
import org.springframework.cloud.dataflow.metrics.collector.services.ApplicationMetricsService;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * Adds the incoming {@link ApplicationMetrics} payload into the backend store
 * @author Vinicius Carvalho
 */
@Component
public class MetricsAggregator {

	private ApplicationMetricsService service;

	private Logger logger = LoggerFactory.getLogger(MetricsAggregator.class);

	public MetricsAggregator(ApplicationMetricsService service) {
		this.service = service;
	}

	@StreamListener(Sink.INPUT)
	public void receive(ApplicationMetrics metrics) {
		if (metrics.getProperties().get(ApplicationMetrics.APPLICATION_GUID) != null
				&& metrics.getProperties().get(ApplicationMetrics.APPLICATION_NAME) != null
				&& metrics.getProperties().get(ApplicationMetrics.STREAM_NAME) != null) {
			this.service.add(metrics);
		}else{
			if(logger.isDebugEnabled()){
				logger.debug("Metric : {} is missing key properties and will not be consumed by the collector",metrics.getName());
			}
		}
	}

}
