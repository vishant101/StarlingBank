# StarlingBank
A simple app to diplay tranactions and “round-up” outgoing transactions to add them to a savings goal.

## Getting Started
1. Download and install android studio

2. Clone the repo:
- `git clone https://github.com/vishant101/StarlingBank.git`

3. Snyc gradle 

4. **[IMPORTANT]** Copy your active user acccess token from the sandbox enviroment and replace the ACCESS_TOKEN string in utils/Constants

5. Run the app on an emulator or device

## Screenshot
| Home | 
|------|
| <img src="https://github.com/vishant101/StarlingBank/blob/master/images/Home.png/" width="275" alt="Login" title="Login" /> |  


## Overview
The app first pulls the users accounts and stores them in a persistent store container using room. This means that it doesn't have to fetch the account from the internet eveytime the app is started. Once the account is fetched the transactions are fetched and displayed. A savings goal is also created. When the transfer button is pressed a round up value of each transaction is added and the total is transfered to a savings goal. 

## Architecture
The app was built with an mvvm architecture. This keeps UI code simple and free of app logic in order to make it easier to manage and test. Android databindings were uses so that the viewmodels have no refrence to the UI elemetents and thus can be tested independantly of the UI itself. An RX mindset was used to keep the app scalable going forward. Depenandancy injection was done via dagger so that components can be tested indepenantly without strong references. For networking I used retrofit, a strong yet lightwieght client. To manage databetween the screens a room database was used. This keeps the data synced between screens.
