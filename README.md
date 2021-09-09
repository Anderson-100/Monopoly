# Monopoly
A virtual, text-based version of Monopoly, coded in Java.

This program displays text using JOptionPane to make it a bit more visually appealing, and it also reads the "theme" of the game from CSV files, which makes it easy to create new themes for the game.

**Backstory:** I originally created this as part of my summer (2020) assignment for my AP Computer Science Principles course in high school. However, due to complications related to COVID, I was switched to an online version of the course that had a different summer assignment. I still decided to finish off the project though, since I had already spent so much time on it, and here it is.

## Documentation
This is essentially a journal that I kept for myself to summarize what I did each day and keep track of what I need to do afterwards. Read through if you're interested, I guess.

### 2020/09/01

Today I started working on my Monopoly program. It shouldn't be too difficult in terms of pure coding, but it is definitely taking some brain power on the creative side right now. I just spent the last hour or two coming up with a theme for my game. At first, I tried to make it a funny 2020 memes edition, but I realized that 2020 was less memey and more depressing, and obviously I shouldn't really be joking about the depressing things, so I decided to scrap that and go with the good ol' car theme. The game is gonna be kinda like Monopoly Empire, where you basically buy companies and try to "build a tower" (reach a certain number of points), with each company logo being a different size (different number of points). I think this approach seems a lot more fun as the game doesn't drag out as long while everyone is trying to get everyone else bankrupt. 

### 2020/09/02

Today I had the idea of making my game so that you can change the theme every time you play it. The Board class constructor takes in the file location of a custom list of properties and then builds the board accordingly. I was having an issue with the Board constructor. At first, I was using an ArrayList that was initially set to a size of 40. Then, I would plug in the Go Space, Jail Space, Free Parking Space, and Go To Jail Space at indices 0, 10, 20, and 30 respectively. However, I was getting IndexOutOfBounds errors on this because these indices are technically greater than than the ArrayList's size. I thought that the initial capacity would give me 40 nulls within the list, but I realized that all it did was *allocate the memory needed* to store 40 items in the list, and the list was still completely empty. I switched it over to a normal Array and it worked perfectly as I wanted.

Notable accomplishments:

- Completed the Board constructor - a virtual Monopoly board can now be created perfectly
- Figured out how to read Property information from a CSV file

### 2020/09/03

Today I successfully got the program to read descriptions for the Chance cards from a CSV file. I also added the points for each property, which I forgot last time, and set the winning score to be the total score of all the properties added together divided by the number of players playing. So if there are a total of 80 points available and there are 4 players playing, you would have to get 20 points to win. I think I pretty much have all the "game elements" I need. Now, I can start creating the logic and gameplay portion of it.

### 2020/09/04

Started writing the logic and gameplay portion of the program.

### 2020/09/05

Today I made some major changes to the gameplay:

- I decided to remove the ability to build houses and hotels on properties. Since the game is meant to be played like Monopoly Empire, the game will most likely end before anyone can get anywhere with houses and hotels.
- I added the rent prices to the ThemeSpaces CSV. I was hoping that there would be some sort of mathematical formula or relationship that I could use to determine the property's rent, but it seems like this is not the case. I also reversed the order of the CSV, so that the cheapest space is now at the top and the most expensive at the bottom, which is the correct order in which they would show up on the board.

Other than that, I just made some more progress on the gameplay overall. I have completed the logic for the specialty spaces in the game, and now I'm working on the "normal" (property) spaces, which has turned out to be surprisingly complicated.

### 2020/09/06

Today I pretty much finished the program. I wrote the propertyAuction method, which runs through the process of auctioning a property when the player who lands on it doesn't want to buy it. I had to make some minor changes to the PropertySpace and Player classes (adding/editing some methods) in order to fit my needs.

The last things I need to do is implement the part where the players pay rent, and also the part where the game kicks out a player if they run out of money. Actually nvm I just implemented the part that kicks the player out if they run out of money. As I was saying, these should be pretty simple to do.

Since playing Monopoly through the console can be a bit boring, I want to implement the Java GUI into this game. The GUI will make the game look a bit better and also allow the player to read through the whole thing before clicking to go on to the next screen. In the console, it just prints everything out until it reaches a reader.nextInt(), which is usually a bunch of text that goes by way too quickly for any human to actually read. I also want to see if I can make a preview drawing/diagram of the game board at the start of the game. I think having the visual available will make the game more interesting.

### 2020/09/07

Today I added the rent paying part and a few more items to the list of chance cards. Tomorrow I will start working on implementing the Java GUI into the game.

### 2020/09/08

I started implementing the GUI into the game. I put the `JOptionPane` and `JInputPane` inside of methods so that I can just call on those methods and input the message, title, and options that I want to include without having to type out the whole thing every time (**abstraction!**). One thing I really want to do is the get rid of the `Cancel` button from `JInputPane` because I literally do not need it at all, but that appears to be really complicated so I'll worry about it later.

I also finished the list of chance cards, but that's not as interesting.
