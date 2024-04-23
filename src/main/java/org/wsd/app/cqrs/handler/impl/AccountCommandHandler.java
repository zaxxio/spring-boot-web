/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.cqrs.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wsd.app.core.handler.EventSourcingHandler;
import org.wsd.app.cqrs.aggregate.AccountAggregate;
import org.wsd.app.cqrs.commands.CloseAccountCommand;
import org.wsd.app.cqrs.commands.CreateAccountCommand;
import org.wsd.app.cqrs.commands.DepositCashCommand;
import org.wsd.app.cqrs.commands.WithdrawCashCommand;
import org.wsd.app.cqrs.handler.CommandHandler;

@Service
@RequiredArgsConstructor
public class AccountCommandHandler implements CommandHandler {

    private final EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(CreateAccountCommand createAccountCommand) {
        AccountAggregate accountAggregate = new AccountAggregate(createAccountCommand);
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(DepositCashCommand depositCashCommand) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(depositCashCommand.getId());
        accountAggregate.depositCash(depositCashCommand.getAmount());
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(WithdrawCashCommand withdrawCashCommand) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(withdrawCashCommand.getId());
        if (withdrawCashCommand.getAmount() > accountAggregate.getBalance()) {
            throw new IllegalStateException("Withdraw amount exceeds balance");
        }
        accountAggregate.withdrawCash(withdrawCashCommand.getAmount());
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(CloseAccountCommand closeAccountCommand) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(closeAccountCommand.getId());
        accountAggregate.closeAccount();
        eventSourcingHandler.save(accountAggregate);
    }
}
