package game.cli;

import game.logic.Dragon;
import game.logic.Hero;
import game.logic.Labyrinth;
import game.logic.MazeBuilder;
import game.logic.Sword;

import java.util.Scanner;

public class Game {
	public static void startGame(int dimension) {
		// opening scanner
		Scanner reader = new Scanner(System.in);

		// initializing variables
		//Labyrinth lab = new Labyrinth(dimension);
		Labyrinth lab = MazeBuilder.Build(dimension);
		Hero hero = new Hero("Hero", lab);
		Sword sword = new Sword(lab, hero);
		Dragon dragon = new Dragon(lab, hero, sword);

		boolean done = false;
		while (!done) {
			// printing labyrinth
			lab.draw(hero, sword, dragon);

			// reading user input
			System.out.println();
			System.out.print("Type W/A/S/D to move: ");
			String dir = reader.next(".");

			// moving hero and dragon
			hero.move(dir, lab);
			dragon.move(lab);

			// checking if player got sword
			if (sword.isVisible()
					&& sword.getPosition().getX() == hero.getPosition().getX()
					&& sword.getPosition().getY() == hero.getPosition().getY()) {
				// hide sword from labyrinth
				sword.hide();

				// arm hero
				hero.arm();
			}

			// if hero is next to a dragon
			if (!dragon.isDead()) {
				if ((Math.abs(hero.getPosition().getX() - dragon.getPosition().getX()) <= 1)
						&& (Math.abs(hero.getPosition().getY()
								- dragon.getPosition().getY()) <= 1)) {
					// if hero has sword
					if (hero.hasSword()) {
						// kill the dragon
						dragon.setLife(0);

						hero.killedTheDragon();
					} else {
						// else kill hero
						hero.setLife(0);
						done = true;
					}
				}
			}

			// if hero is able to step on Exit, game is done
			if (lab.getLab()[hero.getPosition().getY()][hero.getPosition().getX()] == 'S')
				done = true;
		}
		// print labyrinth for the last time
		lab.draw(hero, sword, dragon);

		// displaying notification message
		System.out.println();
		if (hero.isDead())
			System.out.println("GAME OVER! You lost.");
		else
			System.out.println("CONGRATULATIONS! You won the game.");

		// closing scanner
		reader.close();
	}

	public static void main(String[] args) {
		// printing main menu
		System.out.println("--------------");
		System.out.println("Labyrinth Game");
		System.out.println("--------------");
		System.out.println();
		System.out.println("1. Play");
		System.out.println("2. Exit");
		System.out.println();

		Scanner reader = new Scanner(System.in);

		boolean done = false;
		while (!done) {
			// reading user input
			System.out.println("Choose what to do:");
			System.out.print("> ");
			int input = reader.nextInt();
			System.out.println();

			switch (input) {
			case 1:
				// reading user input
				int dimension;
				do {
					System.out.println("Insert an odd labyrinth size (>= 5): ");
					System.out.print("> ");
					dimension = reader.nextInt();
				} while (dimension % 2 == 0 || dimension < 5);

				startGame(dimension);
				done = true;
				break;
			case 2:
				System.out.println("Quitting game... Done.");
				done = true;
				break;
			default:
				System.out.println("Invalid input!");
				break;
			}
		}

		reader.close();
	}
}
