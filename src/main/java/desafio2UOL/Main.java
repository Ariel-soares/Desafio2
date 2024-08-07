package desafio2UOL;

import java.util.Scanner;

import desafio2UOL.services.DistributionCenterService;
import desafio2UOL.services.DonationService;
import desafio2UOL.services.ItemService;
import desafio2UOL.services.OrderService;
import desafio2UOL.services.ShelterService;
import desafio2UOL.views.DistributionCenterMenu;
import desafio2UOL.views.DonationsMenu;
import desafio2UOL.views.ReportMenu;
import desafio2UOL.views.ShelterMenu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

	private static Scanner scanner = new Scanner(System.in);
	private static ShelterService shelterService = new ShelterService();
	private static DistributionCenterService distributionCenterService = new DistributionCenterService();
	private static ItemService itemService = new ItemService();
	private static DonationService donationService = new DonationService();
	private static OrderService orderService = new OrderService();

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("desafio-context");
		EntityManager em = emf.createEntityManager();

		showMenu(em);

		emf.close();
		em.close();

	}

	private static void showMenu(EntityManager em) {
		while (true) {
			System.out.println("\n1. Donation Management");
			System.out.println("2. Shelter Management");
			System.out.println("3. Distribution Center Management");
			System.out.println("4. Report menu");
			System.out.println("5. Exit");
			System.out.print("\nChoose an option:\n");
			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1:
				DonationsMenu.showDonationsMenu(scanner, distributionCenterService, itemService, donationService, em);
				break;
			case 2:
				ShelterMenu.showShelterMenu(scanner, shelterService, em, distributionCenterService, orderService);
				break;
			case 3:
				DistributionCenterMenu.showDistributionCenterMenu(scanner, em);
				break;
			case 4:
				ReportMenu.showReportMenu(scanner, distributionCenterService, shelterService, donationService, em);
				break;
			case 5:
				System.out.println("\nCLOSING SYSTEM");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid option");
			}
		}
	}
}