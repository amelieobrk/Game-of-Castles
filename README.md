# Game of Castles

## Project Overview

**Game of Castles** is a strategic simulation inspired by the classic board game Risk. Designed for 2-4 players, the objective is to conquer all castles on a two-dimensional map, which includes various terrains such as grasslands, forests, mountains, and water bodies. Castles belonging to different kingdoms are distributed across the map and connected by paths.

Game Rounds:
1. **Initial Round**: Players take turns selecting castles until all available castles are distributed.
2. **Subsequent Rounds**: Players receive troops based on the number of castles they own. Actions include distributing troops, moving troops, and attacking neighboring enemy castles.

Scoring:
Points are awarded for various actions such as selecting castles, defeating enemy troops, capturing enemy castles, and completing mission objectives.

Code Structure:
The project follows an object-oriented approach with the following main components:
- **GUI**: Manages the user interface elements.
- **Base**: Contains foundational classes such as graphs and algorithms.
- **Game**: Handles game logic and mechanics.
- **Components**: Includes elements like castles, kingdoms, and players.
- **Views**: Manages different views in the game such as game menus and information panels.

## Installation

1. **Clone the Repository**:
   - Open a terminal and clone the repository with the following command:
     ```bash
     git clone https://github.com/your-username/game-of-castles.git
     ```
   - Navigate into the project directory:
     ```bash
     cd game-of-castles
     ```

2. **Set Up the Environment**:
   - Ensure you have the [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) installed (minimum version 8).

3. **Build the Project**:
   - Use a build tool like [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/) to build the project. Example for Maven:
     ```bash
     mvn clean install
     ```

4. **Run the Application**:
   - Start the game with the following command. Example:
     ```bash
     java -jar target/game-of-castles.jar
     ```
   - Alternatively, you can open the project in an IDE like [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/) and run it directly from there.

## Technologies Used

- **Java**: Main programming language used for implementing game mechanics and logic.
- **Swing**: GUI toolkit for creating the user interface.
- **JUnit**: Testing framework for writing and running unit tests.
- **Maven/Gradle**: Build tools for managing project dependencies and building the project.
- **Git**: Version control system for managing source code.
- **GitHub**: Platform for project management and team collaboration.


##Contributors
- **Amelie Oberkirch**
- **Paul Grewe**
- **Anna-Maria Kugler**
- **Amine Couchane**

