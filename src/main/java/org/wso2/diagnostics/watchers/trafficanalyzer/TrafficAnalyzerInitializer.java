/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.diagnostics.watchers.trafficanalyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.diagnostics.actionexecutor.ServerProcess;
import org.wso2.diagnostics.utils.ConfigMapHolder;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.wso2.diagnostics.utils.CommonUtils.getBooleanValue;
import static org.wso2.diagnostics.utils.CommonUtils.getIntegerValue;

import static org.wso2.diagnostics.utils.Constants.LAST_SECOND_REQUESTS_ENABLED;
import static org.wso2.diagnostics.utils.Constants.LAST_SECOND_REQUESTS_WINDOW_SIZE;
import static org.wso2.diagnostics.utils.Constants.LAST_SECOND_REQUESTS_INTERVAL;
import static org.wso2.diagnostics.utils.Constants.LAST_SECOND_REQUESTS_DELAY;
import static org.wso2.diagnostics.utils.Constants.LAST_FIFTEEN_SECONDS_REQUESTS_ENABLED;
import static org.wso2.diagnostics.utils.Constants.LAST_FIFTEEN_SECONDS_REQUESTS_WINDOW_SIZE;
import static org.wso2.diagnostics.utils.Constants.LAST_FIFTEEN_SECONDS_REQUESTS_INTERVAL;
import static org.wso2.diagnostics.utils.Constants.LAST_FIFTEEN_SECONDS_REQUESTS_DELAY;
import static org.wso2.diagnostics.utils.Constants.LAST_MINUTE_REQUESTS_ENABLED;
import static org.wso2.diagnostics.utils.Constants.LAST_MINUTE_REQUESTS_WINDOW_SIZE;
import static org.wso2.diagnostics.utils.Constants.LAST_MINUTE_REQUESTS_INTERVAL;
import static org.wso2.diagnostics.utils.Constants.LAST_MINUTE_REQUESTS_DELAY;
import static org.wso2.diagnostics.utils.Constants.WATCHER_INITIAL_DELAY;

public class TrafficAnalyzerInitializer {

    private static final Logger log = LogManager.getLogger(TrafficAnalyzerInitializer.class);

    public static void init() {

        Map<String, Object> configMap =  ConfigMapHolder.getInstance().getConfigMap();
        ScheduledExecutorService globalExecutorService = Executors.newSingleThreadScheduledExecutor();

        boolean isLastSecondRequestsEnabled = getBooleanValue(configMap.get(LAST_SECOND_REQUESTS_ENABLED), false);
        if (isLastSecondRequestsEnabled) {
            int lastSecondRequestsWindowSize = getIntegerValue(configMap.get(LAST_SECOND_REQUESTS_WINDOW_SIZE), 300);
            int lastSecondRequestsInterval = getIntegerValue(configMap.get(LAST_SECOND_REQUESTS_INTERVAL), 1);
            int lastSecondRequestsDelay = getIntegerValue(configMap.get(LAST_SECOND_REQUESTS_DELAY), 60);
            TrafficAnalyzer lastSecondRequests = new TrafficAnalyzer(ServerProcess.getProcessId(),
                    lastSecondRequestsWindowSize, lastSecondRequestsDelay, "LastSecondRequests");
            globalExecutorService.scheduleAtFixedRate(lastSecondRequests, WATCHER_INITIAL_DELAY,
                    lastSecondRequestsInterval, java.util.concurrent.TimeUnit.SECONDS);
            log.info("LastSecondRequests Traffic Analyzer is enabled with window size: " + lastSecondRequestsWindowSize +
                    ", interval: " + lastSecondRequestsInterval + ", delay: " + lastSecondRequestsDelay);
        }

        boolean isLastFifteenSecondsRequestsEnabled =
                getBooleanValue(configMap.get(LAST_FIFTEEN_SECONDS_REQUESTS_ENABLED), false);
        if (isLastFifteenSecondsRequestsEnabled) {
            int lastFifteenSecondsRequestsWindowSize =
                    getIntegerValue(configMap.get(LAST_FIFTEEN_SECONDS_REQUESTS_WINDOW_SIZE), 100);
            int lastFifteenSecondsRequestsInterval =
                    getIntegerValue(configMap.get(LAST_FIFTEEN_SECONDS_REQUESTS_INTERVAL), 15);
            int lastFifteenSecondsRequestsDelay =
                    getIntegerValue(configMap.get(LAST_FIFTEEN_SECONDS_REQUESTS_DELAY), 4);
            TrafficAnalyzer lastFifteenSecondsRequests = new TrafficAnalyzer(ServerProcess.getProcessId(),
                    lastFifteenSecondsRequestsWindowSize, lastFifteenSecondsRequestsDelay, "Last15SecondRequests");
            globalExecutorService.scheduleAtFixedRate(lastFifteenSecondsRequests, WATCHER_INITIAL_DELAY,
                    lastFifteenSecondsRequestsInterval, java.util.concurrent.TimeUnit.SECONDS);
            log.info("LastFifteenSecondsRequests Traffic Analyzer is enabled with window size: " +
                    lastFifteenSecondsRequestsWindowSize + ", interval: " + lastFifteenSecondsRequestsInterval +
                    ", delay: " + lastFifteenSecondsRequestsDelay);
        }

        boolean isLastMinuteRequestsEnabled = getBooleanValue(configMap.get(LAST_MINUTE_REQUESTS_ENABLED), false);
        if (isLastMinuteRequestsEnabled) {
            int lastMinuteRequestsWindowSize = getIntegerValue(configMap.get(LAST_MINUTE_REQUESTS_WINDOW_SIZE), 100);
            int lastMinuteRequestsInterval = getIntegerValue(configMap.get(LAST_MINUTE_REQUESTS_INTERVAL), 60);
            int lastMinuteRequestsDelay = getIntegerValue(configMap.get(LAST_MINUTE_REQUESTS_DELAY), 1);
            TrafficAnalyzer lastMinuteRequests = new TrafficAnalyzer(ServerProcess.getProcessId(),
                    lastMinuteRequestsWindowSize, lastMinuteRequestsDelay, "LastMinuteRequests");
            globalExecutorService.scheduleAtFixedRate(lastMinuteRequests, WATCHER_INITIAL_DELAY,
                    lastMinuteRequestsInterval, java.util.concurrent.TimeUnit.SECONDS);
            log.info("LastMinuteRequests Traffic Analyzer is enabled with window size: " +
                    lastMinuteRequestsWindowSize + ", interval: " + lastMinuteRequestsInterval +
                    ", delay: " + lastMinuteRequestsDelay);
        }
    }
}
