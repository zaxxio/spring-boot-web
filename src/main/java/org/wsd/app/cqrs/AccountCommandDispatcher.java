/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.cqrs;

import lombok.extern.log4j.Log4j2;
import org.wsd.app.core.commands.BaseCommand;
import org.wsd.app.core.commands.CommandHandler;
import org.wsd.app.core.infrastructure.CommandDispatcher;
import org.wsd.app.cqrs.commands.CreateAccountCommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Log4j2
public class AccountCommandDispatcher implements CommandDispatcher {

    private Map<Class<? extends BaseCommand>, List<CommandHandler>> routes = new HashMap<>();

    @Override
    public void registerHandler(Class type, CommandHandler command) {
        routes.computeIfAbsent(type, c -> new LinkedList<>())
                .add(command);
    }

    @Override
    public void send(BaseCommand command) {
        List<CommandHandler> commandHandlers = routes.get(command.getClass());
        if (commandHandlers == null && commandHandlers.size() == 0) {
            log.error("No handler registered for command " + command.getClass());
            throw new RuntimeException("No handler registered for command " + command.getClass());
        }
        if (commandHandlers.size() > 1) {
            log.error("Can not send command more than one handler ");
            throw new RuntimeException("Can not send command more than one handler ");
        }
        commandHandlers.get(0).handle(command);
    }
}
