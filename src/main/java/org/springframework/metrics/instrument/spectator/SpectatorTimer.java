/**
 * Copyright 2017 Pivotal Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.metrics.instrument.spectator;

import org.springframework.metrics.instrument.Clock;
import org.springframework.metrics.instrument.internal.AbstractTimer;

import java.util.concurrent.TimeUnit;

public class SpectatorTimer extends AbstractTimer {
    private com.netflix.spectator.api.Timer timer;

    public SpectatorTimer(com.netflix.spectator.api.Timer timer, Clock clock) {
        super(timer.id().name(), clock);
        this.timer = timer;
    }

    @Override
    public void record(long amount, TimeUnit unit) {
        timer.record(unit.toNanos(amount), TimeUnit.NANOSECONDS);
    }

    @Override
    public long count() {
        return timer.count();
    }

    @Override
    public double totalTime(TimeUnit unit) {
        // the Spectator Timer contract insists that nanos be returned from totalTime()
        return nanosToUnit(timer.totalTime(), unit);
    }
}
