# Darwin World 🐑
Java Desktop App that models simulation inspired by the Charles Darwin's theory of natural selection. The application allows users to configure various parameters related to the map, animals, and simulation settings, follow real-time ecosystem changes and analyze statistics.

Project realized as a part of Object Oriented Programming Course at AGH University of Cracow by:
- Katarzyna Bęben [[kasiabeben10]](https://github.com/kasiabeben10)
- Aleksander Jóźwik [[OlaszPL]](https://github.com/OlaszPL)

![image](https://github.com/user-attachments/assets/3ea342ed-a567-4faa-989a-9f84cca9cf8f)


## About
This project is a visualization of the world of herbivores traveling in search of food to survive and a partner to reproduce to expand the number of individuals of their species. Species develop over time through the mechanism of gene transmission from parents. Genes motivate the behavior and movement of animals.

The simulation world consists of a regular, rectangular area divided into square fields. Most of the world is covered by steppes, on which there are few plants that animals eat. Some areas are treated as jungles, where plants grow much faster. Plants grow in random places, but their concentration is greater in the jungle than in the steppe.

Each animal has a certain amount of energy, which decreases each day. Finding and eating a plant increases the energy level by a certain amount.

Daily changes to the above world include:
- removing dead animals from the map
- turning and moving each animal
- consuming plants
- breeding of animals capable of reproducing located in the same field
- growing new plants

The simulation shows how the population changes over time and allows you to observe the mechanisms that govern the considered animal world. The user can track how herbivores behave depending on the state of the ecosystem in which they live.

## Features
### Configuration
The simulation is highly customizable. The user can both set a myriad of parameters and use ready-made configurations that provide stunts that are interesting to observe.

![image](https://github.com/user-attachments/assets/965cb695-bffb-4325-b6b4-1e904a392eca)

As a user, you can choose:
- height and width of the map
- starting number of animals
- length of the animals' genome
- minimum and maximum number of mutations in offspring (may be equal to 0)
- animal behavior variant <br/>
  - full predestination - the animal always executes genes one after another <br/>
  - a bit of craziness - in 80% of cases, after executing a gene, the animal activates the gene that follows it immediately, in 20% of cases, however, it jumps to another, random gene
- starting number of plants
- number of plants growing every day
- plant growth variant <br/>
  - equatorial forest - plants prefer a horizontal strip of fields in the central part of the map (imitating the equator and surrounding areas)
  - creeping jungle - new plants appear most often near existing plants (unless the map has been completely stripped of them)
- initial animal energy
- energy necessary to consider an animal as ready to reproduce
- energy of parents used to create offspring
- energy per single movement
- energy provided by eating one plant

### Presets
App comes with some predefined presets by default. They can be used as samples to show all of the available features. User can also save own presets as csv files.

![2025-02-1913-06-01-ezgif com-video-to-gif-converter (1)](https://github.com/user-attachments/assets/f5fb0589-bdb0-49ba-a31b-1cb52cedaad3)

> [!IMPORTANT]
> Presets by default are stored and saved in user's home directory in the .darwin-world folder.

### Visualization
![2025-02-1913-23-30-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/cfe56b70-15b9-4e95-a79b-7d4df03cd423)

### Logs
The application saves simulation logs to a csv file if this option is selected in the start window.
The above contains the following information for each day:
- number of animals living on the map
- number of plants
- number of free fields on the map
- most popular genotypes
- average energy level
- average lifespan
- average number of children


## Usage
### Technical Requirements
To run the application on your computer, you should have **Java 21** installed. 

Make sure you have it; if not, you can install it from [Oracle](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html).

>[!NOTE]
> App has been tested under Windows and Linux operating systems, nevertheless it should also work under MacOS.

### Running
1. Download the latest release from the **[Releases](https://github.com/OlaszPL/Darwin-world/releases)** section.  
2. Open a terminal, go to the folder where the `.jar` file is located, and run the application using the following command:
```shell
java -jar Darwin_World-1.0.jar
```
or simply double-click on Darwin_World-1.0.jar file.

![2025-02-1913-33-48-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/ef84b10b-164b-49be-9b70-48d2550fd4a3)
