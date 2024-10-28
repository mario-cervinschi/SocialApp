package ui;

import domain.Prietenie;
import domain.Utilizator;
import domain.validators.ValidationException;
import service.Comunitati;
import service.SocialService;

import java.util.Scanner;

public class Console {

    private final SocialService service;
    private final Comunitati comunitati;

    public Console(SocialService service) {
        this.service = service;
        comunitati = new Comunitati(service);
    }

    private void printMenu(){
        System.out.println("Meniu");
        System.out.println("1. Adauga utilizator");
        System.out.println("2. Sterge utilizator");
        System.out.println("3. Adauga prietenie");
        System.out.println("4. Sterge prietenie");
        System.out.println("5. Afisare numar de comunitati ");
        System.out.println("6. Afisare cea mai sociabila comunitate");
        System.out.println("0. Exit");
    }

    public void run(){
        Scanner scan = new Scanner(System.in);
        int ok = 1;
        while(ok == 1){
            printMenu();
            String input = scan.nextLine();
            switch (input) {
                case "1" -> addUtilizator();
                case "2" -> deleteUser();
                case "3" -> addPrietenie();
                case "4" -> deletePrietenie();
                case "5" -> connectedCommunities();
                case "6" -> mostSocialCommunities();
                case "0" -> ok = 0;
            }
        }
    }

    private void addUtilizator(){
        Scanner scan = new Scanner(System.in);
        System.out.println("First name: ");
        String firstName = scan.nextLine();
        System.out.println("Last name: ");
        String lastName = scan.nextLine();
        try{
            service.addUtilizator(new Utilizator(firstName, lastName));
        }
        catch(ValidationException | IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser(){
        Scanner scan = new Scanner(System.in);
        System.out.println("ID: ");
        String id = scan.nextLine();
        var longID = Long.parseLong(id);
        try{
            service.removeUtilizator(longID);
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private void addPrietenie(){
        Scanner scan = new Scanner(System.in);
        System.out.println("ID prieten 1: ");
        String id1 = scan.nextLine();
        System.out.println("ID prieten 2: ");
        String id2 = scan.nextLine();
        try{
            service.addPrietenie(new Prietenie(Long.parseLong(id1), Long.parseLong(id2)));
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private void deletePrietenie(){
        Scanner scan = new Scanner(System.in);
        System.out.println("ID prieten 1: ");
        String id1 = scan.nextLine();
        System.out.println("ID prieten 2: ");
        String id2 = scan.nextLine();
        try{
            service.deletePrietenie(Long.parseLong(id1), Long.parseLong(id2));
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private void connectedCommunities(){
        var nrCom = comunitati.connectedCommunities();
        System.out.println("Numar de comunitati: " + nrCom);
    }

    private void mostSocialCommunities(){
        var nrCom = comunitati.mostSociableCommunity();
        nrCom.forEach(System.out::println);
    }

}
