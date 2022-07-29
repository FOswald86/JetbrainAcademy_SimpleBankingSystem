package banking;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

	static Scanner scanner = new Scanner(System.in);
	static Random random = new Random();

	public static void main(String[] args) {
		DbConnection.addFileNameToUrl("test.db");
		DbCreate.createDatabase();
		DbCreate.createNewTable();
		while (mainMenu() != 0) {
			mainMenu();
		}
	}

	public static int mainMenu() {
		System.out.println("1. Create an account");
		System.out.println("2. Log into account");
		System.out.println("0. Exit");
		int menuSelection = scanner.nextInt();
		switch (menuSelection) {
			case 1:
				createAccount();
				break;
			case 2:
				logIntoAccount();
				break;
			case 0:
				System.out.println("Bye!");
				System.exit(0);
				break;
		}
		return menuSelection;
	}

	public static void createAccount() {
		String cardNumber = "400000";
		for (int i = 0; i < 9; i++) {
			cardNumber += random.nextInt(10);
		}
		int[] luhn = new int[cardNumber.length()];
		int sum = 0;

		// first 15 digits from String cardNumber to int Array
		for (int i = 0; i < cardNumber.length(); i++) {
			luhn[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
		}
		// multiply digits on odd positions * 2
		for (int i = 0; i < cardNumber.length(); i += 2) {
			luhn[i] = luhn[i] * 2;
		}
		// check for numbers > 9 and substract them with 9
		for (int i = 0; i < cardNumber.length(); i++) {
			if (luhn[i] > 9) {
				luhn[i] = luhn[i] - 9;
			}
		}
		// get sum
		for (int i = 0; i < cardNumber.length(); i++) {
			sum += luhn[i];
		}
		// get checkDigit and add to cardNumber on last position
		int checkDigit = 0;
		if (sum % 10 != 0) {
			checkDigit = Math.abs(10 - (sum % 10));
		}
		cardNumber += checkDigit;

		String PIN = "";
		for (int i = 0; i < 4; i++) {
			PIN += random.nextInt(10);
		}

		DbInsert.insertCard(cardNumber, PIN, 0);

		System.out.println("\nYour card has been created");
		System.out.println("Your card number:");
		System.out.println(cardNumber);
		System.out.println("Your card PIN:");
		System.out.println(PIN + "\n");

	}

	static String inputCardNumber = "";
	public static void logIntoAccount() {
		System.out.println("\nEnter your card number:");
		inputCardNumber = scanner.next();
		System.out.println("Enter your PIN:");
		String inputPIN = scanner.next();
		try {
			if (inputPIN.equals(DbRead.getPinWhere())) {
				System.out.println("\nYou have successfully logged in!\n");
				logIntoAccountMenu();
			} else {
				System.out.println("\nWrong card number or PIN!\n");
			}
		} catch (Exception e) {
			mainMenu();
		}
	}

	static String transferNumber = "";
	public static void logIntoAccountMenu() {
		System.out.println("1. Balance");
		System.out.println("2. Add income");
		System.out.println("3. Do transfer");
		System.out.println("4. Close account");
		System.out.println("5. Log out");
		System.out.println("0. Exit");
		int menuSelection = scanner.nextInt();
		switch (menuSelection) {
			case 1:
				System.out.println("\n" + DbRead.getBalance(inputCardNumber) + "\n");
				logIntoAccountMenu();
				break;
			case 2:
				System.out.println("\nEnter income:");
				DbInsert.addIncome(scanner.nextInt());
				System.out.println("Income was added!\n");
				logIntoAccountMenu();
				break;
			case 3:
				System.out.println("\nTransfer");
				System.out.println("Enter card number:");
				transferNumber = scanner.next();

				if (!(inputCardNumber.equals(transferNumber))) {
					if (luhnCheckTransferNumber()) {
						if (DbRead.checkTransferNumberExists()) {
							System.out.println("Enter how much money you want to transfer:");
							int transferAmount = scanner.nextInt();
							if (DbRead.getBalance(inputCardNumber) >= transferAmount) {
								DbInsert.transfer(transferAmount);
								System.out.println("Success!\n");
								logIntoAccountMenu();
							} else {
								System.out.println("Not enough money!\n");
								logIntoAccountMenu();
							}
						} else {
							System.out.println("Such a card does not exist.\n");
							logIntoAccountMenu();
						}
					} else {
						System.out.println("Probably you made a mistake in the card number. Please try again!\n");
						logIntoAccountMenu();
					}
				} else {
					System.out.println("You can't transfer money to the same account!\n");
					logIntoAccountMenu();
				}

				break;
			case 4:
				DbDelete.deleteAccount();
				break;
			case 5:
				System.out.println("\nYou have successfully logged out!\n");
				mainMenu();
				break;
			case 0:
				System.out.println("Bye!");
				System.exit(0);
				break;
		}
	}

	static boolean luhnCheckTransferNumber() {

		int[] cardIntArray=new int[transferNumber.length()];

		for(int i=0;i<transferNumber.length();i++)
		{
			char c= transferNumber.charAt(i);
			cardIntArray[i]=  Integer.parseInt(""+c);
		}

		for(int i=cardIntArray.length-2;i>=0;i=i-2)
		{
			int num = cardIntArray[i];
			num = num * 2;
			if(num>9)
			{
				num = num%10 + num/10;
			}
			cardIntArray[i]=num;
		}

		int sum = Arrays.stream(cardIntArray).sum();

		if(sum%10==0) {
			return true;
		}
		return false;
	}
}

