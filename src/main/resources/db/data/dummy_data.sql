insert into account values('cc856b46-4c6e-11ea-b77f-2e728ce88125', 'Naruto', 12345.00);
insert into account values('d0c91658-4c6e-11ea-b77f-2e728ce88125', 'Sakura', 12345.00);

insert into event_store values('d0c91658-4c6e-11ea-b77f-2e728ce88125', '{"type_info":"account_created","accountId":"d0c91658-4c6e-11ea-b77f-2e728ce88125","name":"Sakura","id":"d0c91658-4c6e-11ea-b77f-2e728ce88125"}');
insert into event_store values('d0c91658-4c6e-11ea-b77f-2e728ce88125', '{"type_info":"money_credited","accountId":"d0c91658-4c6e-11ea-b77f-2e728ce88125","amount":12345.0,"id":"d0c91658-4c6e-11ea-b77f-2e728ce88125"}');

insert into event_store values('cc856b46-4c6e-11ea-b77f-2e728ce88125', '{"type_info":"account_created","accountId":"cc856b46-4c6e-11ea-b77f-2e728ce88125","name":"Naruto","id":"cc856b46-4c6e-11ea-b77f-2e728ce88125"}');
insert into event_store values('cc856b46-4c6e-11ea-b77f-2e728ce88125', '{"type_info":"money_credited","accountId":"cc856b46-4c6e-11ea-b77f-2e728ce88125","amount":12345.0,"id":"cc856b46-4c6e-11ea-b77f-2e728ce88125"}');