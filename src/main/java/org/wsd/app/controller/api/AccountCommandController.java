/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.controller.api;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.config.SwaggerConfig;
import org.wsd.app.core.infrastructure.CommandDispatcher;
import org.wsd.app.cqrs.commands.CreateAccountCommand;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@Tag(name = "Account Command Controller")
@SecurityRequirement(name = SwaggerConfig.BEARER_TOKEN)
public class AccountCommandController {

    private CommandDispatcher commandDispatcher;

    @Autowired
    public AccountCommandController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(CreateAccountCommand command) {
        UUID uuid = UUID.randomUUID();
        CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
                .id(uuid.toString())
                .accountHolder(command.getAccountHolder())
                .accountType(command.getAccountType())
                .createdAt(LocalDate.now())
                .balance(command.getBalance())
                .build();
        commandDispatcher.send(createAccountCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(uuid.toString());
    }

}
