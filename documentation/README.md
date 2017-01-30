# *Fantasy VC*

**Fantasy VC** is a game that combines the fun game of fantasy sports and the exhilirating realm of stocks and finance.

Time spent: **250** hours spent in total

## User Stories

The following functionality is completed and working:

* [x] User can login or play as a guest.
* [x] Guest mode functionality allows a single player to play with one's self.
* [x] Authenticated users enter a lobby in which open games are shown.
* [x] The lobby will automatically begin the game once all players are present and ready.
* [x] Players in multi-player games can pick companies to bid on in a round-robin order.
* [x] Auction bid screen allows users to bid on each company.
* [x] A time-lapse panel shows generated notifications and updates for companies, and appropriately changes company stock values.
* [x] A quarterly panel shows all portfolios' quarterly progress and allows users to buy and sell companies.
* [x] Winners are appropriately calculated at the end of the game and all players' information are displayed.
* [x] Chat panel allows players to chat with one another.
* [x] .wav file played to signal chat notifications.
* [x] Players are allowed to select and save their own firm name, bio, and player icon.
* [x] Panel at the top of the game screens contains a player's current captial and updates in real time.

## Bugs

The following bugs were discovered at the time of this document's creation:

* [x] Occasional exceptions thrown on the AWS server when a user disconnects from it.
* [x] Occasional minor rounding issues and image formatting issues.

## License

    Copyright 2016 CS201Team26

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
