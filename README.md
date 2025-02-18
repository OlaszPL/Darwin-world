# Darwin-world
Java Desktop App that models simulation inspired by the Charles Darwin's theory of natural selection. The application allows users to configure various parameters related to the map, animals, and simulation settings, follow real-time ecosystem changes and analyze statistics.

Project realized as a part of Object Oriented Programming Course at AGH University of Krakow by:
- Katarzyna Bęben [[kasiabeben10]](https://github.com/kasiabeben10)
- Aleksander Jóźwik [[OlaszPL]](https://github.com/OlaszPL)

## About
This project is a visualization of the world of herbivores travelling in search of food to survive and a partner to reproduce to expand the number of individuals of their species. Species develop over time, through the mechanism of gene transmission from parents. Genes motivate the behavior and movement of animals.

The simulation world consists of a regular, rectangular area divided into square fields. Most of the world is covered by steppes, on which there are few plants that animals eat. Some areas are treated as a jungle, where plants grow much faster. Plants grow in random places, but their concentration is greater in the jungle than in the steppe.

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

As a user, you can choose:
- height and width of the map
- starting number of animals
- starting energy of animals
- energy necessary to consider an animal as full (and ready to reproduce)
- energy of parents used to create offspring
- minimum and maximum number of mutations in offspring (may be equal to 0)
- length of the animals' genome
- animal behavior variant <br/>
  - full predestination - the animal always executes genes one after another <br/>
  - bit of craziness - in 80% of cases, after executing a gene, the animal activates the gene that follows it immediately, in 20% of cases, however, it jumps to another, random gene
- starting number of plants
- energy provided by eating one plant
- number of plants growing every day
- plant growth variant <br/>
  - forested equator - plants prefer a horizontal strip of fields in the central part of the map (imitating the equator and surrounding areas)
  - creeping jungle - new plants appear most often near existing plants (unless the map has been completely stripped of them)


### Visualisation
### Logs
The application saves simulation logs to a csv file if this option is selected in the start window.
The above contain the following information for each day:
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

Make sure you have it, if not, you can install it from [Oracle](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html).

### Running
1. Download the latest release from the **[Releases](https://github.com/OlaszPL/Darwin-world/releases)** section.  
2. Open a terminal, go to the folder where the `.jar` file is located and run the application using the following command:
```shell
java -jar Darwin_World-1.0.jar
```
or simply double click on Darwin_World-1.0.jar file.

