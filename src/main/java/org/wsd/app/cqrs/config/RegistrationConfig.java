/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.cqrs.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.wsd.app.core.infrastructure.CommandDispatcher;
import org.wsd.app.cqrs.commands.CloseAccountCommand;
import org.wsd.app.cqrs.commands.CreateAccountCommand;
import org.wsd.app.cqrs.commands.DepositCashCommand;
import org.wsd.app.cqrs.commands.WithdrawCashCommand;
import org.wsd.app.cqrs.handler.CommandHandler;
import org.wsd.app.cqrs.handler.impl.AccountCommandHandler;
import org.wsd.app.cqrs.infrastructure.AccountCommandDispatcher;

@Configuration
public class RegistrationConfig {

    @Autowired
    @Qualifier("accountCommandDispatcher")
    private AccountCommandDispatcher commandDispatcher;

    @Autowired
    @Qualifier("accountCommandHandler")
    private AccountCommandHandler commandHandler;


    @PostConstruct
    public void setup() {
        commandDispatcher.registerHandler(CreateAccountCommand.class,
                (cmd) -> commandHandler.handle((CreateAccountCommand) cmd));
        commandDispatcher.registerHandler(DepositCashCommand.class,
                (cmd) -> commandHandler.handle((DepositCashCommand) cmd));
        commandDispatcher.registerHandler(WithdrawCashCommand.class,
                (cmd) -> commandHandler.handle((WithdrawCashCommand) cmd));
        commandDispatcher.registerHandler(CloseAccountCommand.class,
                (cmd) -> commandHandler.handle((CloseAccountCommand) cmd));
    }

}
