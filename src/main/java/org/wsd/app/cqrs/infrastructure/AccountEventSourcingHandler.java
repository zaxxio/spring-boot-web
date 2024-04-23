/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.cqrs.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wsd.app.core.aggregates.AggregateRoot;
import org.wsd.app.core.events.BaseEvent;
import org.wsd.app.core.handler.EventSourcingHandler;
import org.wsd.app.core.infrastructure.EventStore;
import org.wsd.app.cqrs.aggregate.AccountAggregate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    private final EventStore eventStore;

    @Override
    public void save(AggregateRoot aggregateRoot) {
        eventStore.saveEvents(aggregateRoot.getId(), aggregateRoot.getUncommittedChanges(), aggregateRoot.getVersion());
        aggregateRoot.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String aggregateId) {
        AccountAggregate accountAggregate = new AccountAggregate();
        try {
            List<BaseEvent> events = eventStore.getEvents(aggregateId);
            if (events != null && !events.isEmpty()) {
                accountAggregate.replayEvents(events);
                Optional<Integer> latestVersion = events.stream().map(BaseEvent::getVersion).max(Comparator.naturalOrder());
                latestVersion.ifPresent(accountAggregate::setVersion);
            }
        } catch (AggregateNotFoundException e) {
            throw new RuntimeException(e);
        }
        return accountAggregate;
    }
}
