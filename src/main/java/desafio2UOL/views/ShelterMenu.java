package desafio2UOL.views;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import desafio2UOL.entities.ClothItem;
import desafio2UOL.entities.DistributionCenter;
import desafio2UOL.entities.FoodItem;
import desafio2UOL.entities.HygieneItem;
import desafio2UOL.entities.Item;
import desafio2UOL.entities.Order;
import desafio2UOL.entities.Shelter;
import desafio2UOL.entities.enums.ItemName;
import desafio2UOL.entities.enums.ItemType;
import desafio2UOL.services.DistributionCenterService;
import desafio2UOL.services.OrderService;
import desafio2UOL.services.ShelterService;
import jakarta.persistence.EntityManager;

public class ShelterMenu {

	public static void showShelterMenu(Scanner scanner, ShelterService shelterService, EntityManager em,
			DistributionCenterService distributionCenterService, OrderService orderService) {
		while (true) {
			System.out.println("\n1. List Shelters");
			System.out.println("2. Find Specific Shelter");
			System.out.println("3. Add Shelter");
			System.out.println("4. Delete Shelter");
			System.out.println("5. Update Shelter");
			System.out.println("6. Order items from distribution centers");
			System.out.println("7. Exit");
			System.out.print("\nChoose an option: \n");
			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1:
				System.out.println("\nLista de abrigos cadastrados\n");
				shelterService.listShelters(em);
				break;
			case 2:
				System.out.println("\nEnter Shelter Id:\n");
				Integer idForFound = scanner.nextInt();
				shelterService.findOne(idForFound, em);
				break;
			case 3:
				addShelter(scanner, shelterService, em);
				break;
			case 4:
				System.out.println("Enter Shelter Id: ");
				Integer id = scanner.nextInt();
				shelterService.deleteShelter(id, em);
				break;
			case 5:
				showUpdateShelterMenu(scanner, shelterService, em);
				break;
			case 6:
				RequestOrderFromDistributionCenter(scanner, shelterService, em, distributionCenterService,
						orderService);
				break;
			case 7:
				System.out.println("\nReturning to main menu\n");
				return;
			default:
				System.out.println("\nInvalid option\n");
			}
		}
	}

	private static void addShelter(Scanner scanner, ShelterService shelterService, EntityManager em) {

		System.out.print("Enter name: ");
		String name = scanner.nextLine().trim();
		if (name.isEmpty()) {
			System.out.println("Name cannot be empty.");
			return;
		}

		System.out.print("Enter address: ");
		String address = scanner.nextLine().trim();
		if (address.isEmpty()) {
			System.out.println("Address cannot be empty.");
			return;
		}

		System.out.print("Enter contact: ");
		String contact = scanner.nextLine().trim();
		if (contact.isEmpty()) {
			System.out.println("Contact cannot be empty.");
			return;
		}

		System.out.print("Enter capacity: ");
		int capacity = scanner.nextInt();
		if (capacity <= 0) {
			System.out.println("Capacity must be greater than 0.");
			return;
		}

		System.out.print("Enter occupancy: ");
		int occupancy = scanner.nextInt();
		if (occupancy < 0 || occupancy > capacity) {
			System.out.println("Occupancy must be between 0 and capacity.");
			return;
		}
		scanner.nextLine();
		System.out.print("Enter responsible: ");
		String responsible = scanner.nextLine().trim();
		if (responsible.isEmpty()) {
			System.out.println("Responsible cannot be empty.");
			return;
		}

		System.out.print("Enter email: ");
		String email = scanner.nextLine().trim();
		if (!email.contains("@") || !email.contains(".")) {
			System.out.println("Email is not valid.");
			return;
		}

		Shelter shelter = new Shelter(name, address, responsible, contact, email, capacity, occupancy);
		shelterService.addShelter(shelter, em);

		System.out.println("\nCadastro realizado com sucesso \n");

	}

	private static void showUpdateShelterMenu(Scanner scanner, ShelterService shelterService, EntityManager em) {
		
		List<Shelter> shelters = shelterService.getAllShelters(em);
		for(Shelter s : shelters) {
			System.out.println("ID: " + s.getId() + " -> " + s);
		}
		
		System.out.print("\nEnter Shelter ID to update:\n");
		int id = scanner.nextInt();
		scanner.nextLine();

		Shelter shelter = shelterService.findById(id, em);
		System.out.println(shelter);
		if (shelter == null) {
			System.out.println("Shelter not found.");
			return;
		}

		while (true) {
			System.out.println("\nUpdating Shelter: " + shelter.getName());
			System.out.println("1. Update Name");
			System.out.println("2. Update Address");
			System.out.println("3. Update Contact");
			System.out.println("4. Update Capacity");
			System.out.println("5. Update Occupancy");
			System.out.println("6. Update email");
			System.out.println("7. Back to Shelters Menu and complete update");
			System.out.print("Choose an option: \n");
			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1:
				System.out.print("Enter new name: ");
				String name = scanner.nextLine();
				if (!name.isEmpty()) {
					shelter.setName(name);
				}
				break;
			case 2:
				System.out.print("Enter new address: ");
				String address = scanner.nextLine();
				if (!address.isEmpty()) {
					shelter.setAddress(address);
				}
				break;
			case 3:
				System.out.print("Enter new contact: ");
				String contact = scanner.nextLine();
				if (!contact.isEmpty()) {
					shelter.setPhoneNumber(contact);
				}
				break;
			case 4:
				System.out.print("Enter new capacity: ");
				int capacity = scanner.nextInt();
				if (capacity > 0) {
					shelter.setCapacity(capacity);
				}
				scanner.nextLine();
				break;
			case 5:
				System.out.print("Enter new occupancy: ");
				int occupancy = scanner.nextInt();
				if (occupancy >= 0) {
					shelter.setOccupancy(occupancy);
				}
				scanner.nextLine();
				break;
			case 6:
				System.out.print("Enter new email: ");
				String email = scanner.nextLine();
				if (!email.isEmpty()) {
					shelter.setEmail(email);
				}
				scanner.nextLine();
				break;
			case 7:
				shelterService.updateShelter(shelter, shelter.getId(), em);
				System.out.println("\nShelter updated successfully.\n");
				return;
			default:
				System.out.println("\nInvalid option\n");
			}
		}
	}

	private static void RequestOrderFromDistributionCenter(Scanner scanner, ShelterService shelterService,
			EntityManager em, DistributionCenterService distributionCenterService, OrderService orderService) {

		List<Shelter> shelters = shelterService.getAllShelters(em);
		Order order = new Order();
		System.out.println(shelters);

		System.out.print("Enter Shelter ID: ");
		int shelterId = scanner.nextInt();
		scanner.nextLine();
		Shelter shelter = shelterService.findById(shelterId, em);

		if (shelter == null) {
			System.out.println("Shelter not found.");
			return;
		}

		order.setRequester(shelter);

		createRequestOrder(scanner, em, order, orderService, distributionCenterService);
	}

	private static void createRequestOrder(Scanner scanner, EntityManager em, Order order, OrderService orderService,
			DistributionCenterService distributionCenterService) {

		System.out.print("Enter item type (1 - Clothes/2 - Hygiene/3 - Food/ 4 - Back to menu): ");
		Integer itemType = scanner.nextInt();
		scanner.nextLine();

		System.out.println("Enter quantity to be requested:");
		Integer quantity = scanner.nextInt();
		scanner.nextLine();

		order.setQuantity(quantity);
		Item item;

		switch (itemType) {
		case 1:

			System.out.println("Enter item name: ");
			String name = scanner.nextLine();
			System.out.println("Enter product size (Infantil/PP/P/M/G/GG)");
			String size = scanner.next().toUpperCase();
			System.out.println("Enter item gender: (1 - Masculine / 2 - Feminine)");
			Integer option = scanner.nextInt();
			scanner.nextLine();
			switch (option) {
			case 1:
				item = new ClothItem(ItemName.valueOf(name.toUpperCase()), "cloth", 'M', size);
				item.setItemType(ItemType.CLOTH);
				order.setItem(item.storageCode());
				addOrder(scanner, order, em, orderService, distributionCenterService);
				break;
			case 2:
				item = new ClothItem(ItemName.valueOf(name.toUpperCase()), "cloth", 'F', size);
				item.setItemType(ItemType.CLOTH);
				order.setItem(item.storageCode());
				addOrder(scanner, order, em, orderService, distributionCenterService);
				break;
			}
			break;
		case 2:
			System.out.println("Enter item name: ");
			name = scanner.nextLine();
			System.out.println("Enter item description: ");
			String description = scanner.nextLine();
			item = new HygieneItem(ItemName.valueOf(name.toUpperCase()), description);
			item.setItemType(ItemType.HYGIENE);
			order.setItem(item.storageCode());
			addOrder(scanner, order, em, orderService, distributionCenterService);
			break;
		case 3:
			System.out.println("Enter item name: ");
			name = scanner.nextLine();
			System.out.println("Enter item description: ");
			description = scanner.nextLine();
			System.out.println("Enter the item measurement: ");
			String measurement = scanner.nextLine();
			System.out.println("Enter item validity date: ");
			String validity = scanner.nextLine();

			item = new FoodItem(ItemName.valueOf(name.toUpperCase()), description, measurement, validity);
			item.setItemType(ItemType.FOOD);
			order.setItem(item.storageCode());
			addOrder(scanner, order, em, orderService, distributionCenterService);
			break;
		case 4:
			return;
		}
	}

	private static void addOrder(Scanner scanner, Order order, EntityManager em, OrderService orderService,
			DistributionCenterService distributionCenterService) {

		List<DistributionCenter> centers = distributionCenterService.getAllDistributionCenters(em);

		List<DistributionCenter> sortedCenters = centers.stream()
				.filter(dc -> dc.getItems().containsKey(order.getItemCode())).sorted((dc1, dc2) -> dc2.getItems()
						.get(order.getItemCode()).compareTo(dc1.getItems().get(order.getItemCode())))
				.collect(Collectors.toList());

		sortedCenters.forEach(dc -> System.out.println(dc.getName() + ": " + dc.getItems().get(order.getItemCode())));

		System.out.print("Enter the IDs of the distribution centers you want to select (comma-separated): ");
		String input = scanner.nextLine();

		List<Integer> selectedIds = Arrays.stream(input.split(",")).map(String::trim).map(Integer::parseInt)
				.collect(Collectors.toList());

		List<DistributionCenter> selectedCenters = sortedCenters.stream().filter(dc -> selectedIds.contains(dc.getId()))
				.collect(Collectors.toList());

		System.out.println("Selected Distribution Centers:");
		selectedCenters.forEach(dc -> System.out.println(dc.getId() + ": " + dc.getName()));

		order.setId(null);
		orderService.addOrder(order, em);
		System.out.println(order);

		for (DistributionCenter dc : selectedCenters) {
			dc.getOrders().add(order);
			distributionCenterService.updateDistributionCenter(dc, dc.getId(), em);
		}

		System.out.println("\nOrder Request completed");
	}

}
