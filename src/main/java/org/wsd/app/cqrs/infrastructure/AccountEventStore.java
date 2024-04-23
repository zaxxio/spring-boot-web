/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.cqrs.infrastructure;

import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.ConcurrencyException;
import org.springframework.stereotype.Service;
import org.wsd.app.core.events.BaseEvent;
import org.wsd.app.core.events.EventModel;
import org.wsd.app.core.infrastructure.EventStore;
import org.wsd.app.cqrs.aggregate.AccountAggregate;
import org.wsd.app.mongo.EventRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountEventStore implements EventStore {

    private final EventRepository eventRepository;
    private final AccountEventProducer accountEventProducer;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = eventRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion == -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException("Version does not match expected version.");
        }
        int version = expectedVersion;
        for (BaseEvent event : events) {
            event.setVersion(version);

            EventModel eventModel = EventModel.builder()
                    .timestamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .payload(event)
                    .build();
            EventModel persistedEventModel = eventRepository.save(eventModel);
            if (persistedEventModel != null && !persistedEventModel.getId().isEmpty()) {
                // produce kafka event's
                accountEventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) throws AggregateNotFoundException {
        List<EventModel> eventStream = eventRepository.findByAggregateIdentifier(aggregateId);
        if (eventStream == null && eventStream.isEmpty()) {
            throw new AggregateNotFoundException("");
        }
        return eventStream.stream().map(eventModel -> eventModel.getPayload()).collect(Collectors.toList());
    }
}
