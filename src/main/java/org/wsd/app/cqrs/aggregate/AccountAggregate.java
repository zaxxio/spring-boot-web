/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.cqrs.aggregate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.wsd.app.core.aggregates.AggregateRoot;
import org.wsd.app.cqrs.commands.CreateAccountCommand;
import org.wsd.app.cqrs.events.AccountClosedEvent;
import org.wsd.app.cqrs.events.AccountCreatedEvent;
import org.wsd.app.cqrs.events.CashDepositedEvent;
import org.wsd.app.cqrs.events.CashWithdrawnEvent;

import java.util.Date;

@Data
@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {

    private Double balance;

    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        raiseEvent(
                AccountCreatedEvent.builder()
                        .id(createAccountCommand.getId())
                        .accountHolder(createAccountCommand.getAccountHolder())
                        .createdAt(new Date())
                        .accountType(createAccountCommand.getAccountType())
                        .balance(createAccountCommand.getBalance())
                        .build()
        );
    }

    public void apply(AccountCreatedEvent accountCreatedEvent) {
        this.id = accountCreatedEvent.getId();
        this.balance = accountCreatedEvent.getBalance();
    }

    public void depositCash(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        raiseEvent(
                CashDepositedEvent.builder()
                        .id(this.id)
                        .amount(amount)
                        .build()
        );
    }

    public void apply(CashDepositedEvent cashDepositedEvent) {
        this.id = cashDepositedEvent.getId();
        this.balance += cashDepositedEvent.getAmount();
    }

    public void withdrawCash(Double amount) {
        raiseEvent(
                CashWithdrawnEvent.builder()
                        .id(this.id)
                        .amount(amount)
                        .build()
        );
    }

    public void apply(CashWithdrawnEvent cashWithdrawnEvent) {
        this.id = cashWithdrawnEvent.getId();
        this.balance -= cashWithdrawnEvent.getAmount();
    }

    public void closeAccount() {
        raiseEvent(
                AccountClosedEvent.builder()
                        .id(this.id)
                        .build()
        );
    }

    public void apply(AccountClosedEvent accountClosedEvent) {
        this.id = accountClosedEvent.getId();
    }
}
