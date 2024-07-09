package desafio2UOL.views;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import desafio2UOL.entities.ClothItem;
import desafio2UOL.entities.DistributionCenter;
import desafio2UOL.entities.Donation;
import desafio2UOL.entities.FoodItem;
import desafio2UOL.entities.HygieneItem;
import desafio2UOL.entities.Item;
import desafio2UOL.services.DistributionCenterService;
import desafio2UOL.services.DonationService;
import desafio2UOL.services.ItemService;
import jakarta.persistence.EntityManager;

public class DonationsMenus {

	public static void showDonationsMenu(Scanner scanner, DistributionCenterService distributionCenterService,
			ItemService itemService, DonationService donationService, EntityManager em) {
		while (true) {
			System.out.println("Donations Menu:");
			System.out.println("1. Add Donation Manually");
			System.out.println("2. Add Donation from CSV");
			System.out.println("3. List Donations");
			System.out.println("4. Back to Main Menu");
			System.out.print("Choose an option: ");
			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1:
				DonationsMenus.addDonationManually(scanner, distributionCenterService, itemService, donationService,
						em);
				break;
			case 2:
				addDonationFromCSV(scanner, distributionCenterService, itemService, donationService, em);
				break;
			case 3:
				listDonations(donationService, em);
				break;
			case 4:
				return;
			default:
				System.out.println("Invalid option");
			}
		}
	}

	private static void listDonations(DonationService donationService, EntityManager em) {
		List<Donation> donations = donationService.getAllDonations(em);
		System.out.println(donations);
	}

	public static void addDonationManually(Scanner scanner, DistributionCenterService distributionCenterService,
			ItemService itemService, DonationService donationService, EntityManager em) {

		Donation donation = new Donation();
		List<Item> items = new ArrayList<>();

		while (true) {
			System.out.print("Enter item type (1 - Clothes/2 - Hygiene/3 - Food/ 4 - End Donation): ");
			Integer itemType = scanner.nextInt();
			scanner.nextLine();

			switch (itemType) {
			case 1:
				System.out.println("Enter quantity to be added:");
				Integer quantity = scanner.nextInt();
				System.out.println("Enter item name: ");
				String name = scanner.nextLine();
				System.out.println("Enter product size (Infantil/PP/P/M/G/GG)");
				String size = scanner.next().toUpperCase();
				System.out.println("Enter item gender: (1 - Masculine / 2 - Feminine)");
				Integer option = scanner.nextInt();
				switch (option) {
				case 1:
					items.add(new ClothItem(name, "cloth", 'M', size, quantity));
					break;

				case 2:
					items.add(new ClothItem(name, "cloth", 'F', size, quantity));
					break;
				}
				break;
			case 2:
				System.out.println("Enter quantity to be added:");
				quantity = scanner.nextInt();
				System.out.println("Enter item name: ");
				name = scanner.nextLine();
				System.out.println("Enter item description: ");
				String description = scanner.nextLine();
				items.add(new HygieneItem(null, name, description, quantity));
				break;
			case 3:
				System.out.println("Enter quantity to be added:");
				quantity = scanner.nextInt();
				System.out.println("Enter item description: ");
				description = scanner.nextLine();
				System.out.println("Enter the item measurement: ");
				String measurement = scanner.nextLine();
				System.out.println("Enter item validity date: ");
				String validity = scanner.nextLine();
				items.add(new FoodItem(null, description, measurement, validity, quantity));
				break;
			case 4:

				for (Item i : items) {
					donation.getItens().add(i);
				}

				List<DistributionCenter> distributionCenters = distributionCenterService.getAllDistributionCenters(em);
				System.out.println("Available Distribution Centers:");
				for (DistributionCenter dc : distributionCenters) {
					System.out.println(dc.getId() + ": " + dc.getName());
				}

				System.out.print("\nEnter distribution center ID: \n");
				int distributionCenterId = scanner.nextInt();
				scanner.nextLine();

				DistributionCenter distributionCenter = distributionCenterService.findById(distributionCenterId, em);

				if (distributionCenter != null) {

					itemService.addItemList(items, em);

					donation.setCenterId(distributionCenter);
					donationService.addDonation(donation, em);
					distributionCenter.getDonations().add(donation);
					distributionCenterService.addDonation(donation, distributionCenterId, em);
					System.out.println("\nDonation added\n");
					return;
				} else
					System.out.println("Distribution Center not found.");

				return;
			default:
				System.out.println("Invalid option");
			}
		}
	}

	

	private static void addDonationFromCSV(Scanner scanner, DistributionCenterService distributionCenterService,
			ItemService itemService, DonationService donationService, EntityManager em) {

		System.out.print("Enter CSV file path: ");
		String csvFilePath = scanner.nextLine();
		Map<Integer, List<Item>> donationMap = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

			String line = br.readLine();

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int distributionCenterId = Integer.parseInt(values[0]);
				String itemType = values[1];

					Item item = createItemFromCSV(itemType, values);
					
					if (item == null) {
						System.out.println("Invalid item type in CSV: " + itemType);
						continue;
					}
					
					donationMap.computeIfAbsent(distributionCenterId, k -> new ArrayList<>()).add(item);
			}

			for (Map.Entry<Integer, List<Item>> entry : donationMap.entrySet()) {
				int distributionCenterId = entry.getKey();
				List<Item> items = entry.getValue();

				DistributionCenter distributionCenter = distributionCenterService.findById(distributionCenterId, em);
				if (distributionCenter == null) {
					System.out.println("Distribution Center not found: " + distributionCenterId);
					continue;
				}

				itemService.addItemList(items, em);
				Donation donation = new Donation(null, distributionCenter);
				for (Item item : items) {

					donation.addItem(item);
					distributionCenter.getItems().add(item);
				}
				System.out.println(donation.getItens());
				donationService.addDonation(donation, em);
				distributionCenterService.updateDistributionCenter(distributionCenter, distributionCenterId, em);
			}

			System.out.println("Donations added from CSV successfully.\n");
		} catch (IOException e) {
			System.out.println("Error reading CSV file: " + e.getMessage());
		}
	}

	private static Item createItemFromCSV(String itemType, String[] values) {
		switch (itemType.toLowerCase()) {
		case "cloth":
			ClothItem cloth = new ClothItem();
			cloth.setId(null);
			cloth.setName(values[3]);
			cloth.setDescription(values[4]);
			cloth.setGender(values[5].toUpperCase().charAt(0));
			cloth.setSize(values[6]);
			cloth.setQuantity(Integer.parseInt(values[2]));

			return cloth;

		case "food":
			FoodItem food = new FoodItem();
			food.setId(null);
			food.setDescription(values[3]);
			food.setMeasurement(values[4]);
			food.setValidity(values[5]);
			food.setQuantity(Integer.parseInt(values[2]));
			return food;

		case "hygiene":
			HygieneItem hygiene = new HygieneItem();
			hygiene.setName(values[3]);
			hygiene.setDescription(values[4]);
			hygiene.setQuantity(Integer.parseInt(values[2]));
			return hygiene;

		default:
			return null;
		}
	}

}
