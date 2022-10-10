package devmindJava.Curs26;

import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CarRentalSystem {
   // NullPointerException nullPE = new NullPointerException("Masina introdusa nu exista in baza de date!");
    private static Scanner sc = new Scanner(System.in);
    private HashMap<String, String> rentedCars =
            new HashMap<>(100, 0.5f);

    private HashMap <String, RentedCars> proprietar = new HashMap<>(2,0.5f);


    public static void writeRentedBinary(HashMap<String , String> list) throws IOException{
        try(ObjectOutputStream writeBinary = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("CarRental.dat")))){
            writeBinary.writeObject(list);
        }
    }
    public static void writeOwnedBinary(HashMap<String, RentedCars> list) throws IOException{
        try(ObjectOutputStream writeBinary = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("CarRental.dat")))){
            writeBinary.writeObject(list);
        }
    }

        public static HashMap<String, String> readRented() throws IOException{
        HashMap<String, String> list = new HashMap<>();
        try(ObjectInputStream readBinary = new ObjectInputStream(new BufferedInputStream(new FileInputStream("CarRental.dat")))){
            list = (HashMap<String, String>) readBinary.readObject();
        }catch (ClassNotFoundException e){
            System.out.println("Exception: Class not found!");
        }
        return list;
        }

        public static HashMap<String, RentedCars> readOwner() throws IOException{
        HashMap<String,RentedCars> list = new HashMap<>();

        try(ObjectInputStream readBinary = new ObjectInputStream(new BufferedInputStream(new FileInputStream("CarRental.dat")))){
            list = (HashMap<String, RentedCars>) readBinary.readObject();
        }catch (ClassNotFoundException e){
            System.out.println("Exception: Class not found!");
        }
            return list;
        }

    private static String getPlateNo() {

        System.out.println("Introduceti numarul de inmatriculare:");
        return sc.nextLine();
    }

    private static String getOwnerName() {
        System.out.println("Introduceti numele proprietarului:");
        return sc.nextLine();
    }

    // search for a key in hashtable
    private boolean isCarRent(String plateNo) throws IsCarRentException{
        if(!rentedCars.containsKey(plateNo)) {
            throw new IsCarRentException("Masina nu este inchiriata!!!!");
        }
        return rentedCars.containsKey(plateNo);
    }

    // get the value associated to a key
    private String getCarRent(String plateNo) throws WrongCarsException {

        if(!rentedCars.containsKey(plateNo)){
            System.out.println("Masina nu se afla in baza de date");
        }

            if (rentedCars.get(plateNo) == null) {
                throw new WrongCarsException("Nu exista nici un propietar cu acest numar de inmatriculare");
            }

        return "Masina este inchiriata de catre: " + rentedCars.get(plateNo);
    }

    // add a new (key, value) pair
    private void rentCar(String plateNo, String ownerName)  {

        if (!rentedCars.containsKey(plateNo)) {
            rentedCars.put(plateNo, ownerName);

        }
        if(!proprietar.containsKey(ownerName)){
           proprietar.put(ownerName, new RentedCars());
        }
        proprietar.get(ownerName).addCar(plateNo);
    }

    // remove an existing (key, value) pair
    private void returnCar(String plateNo){
        if(rentedCars.containsKey(plateNo)){
            proprietar.get(rentedCars.get(plateNo)).removeCar(plateNo);
            rentedCars.remove(plateNo);
            System.out.println("Masina " + plateNo + " a fost returnata");
        } else {
            System.out.println("Masina nu exista");
        }

    }

    private int totalRented(){
        return rentedCars.size();
    }

    private static void printCommandsList() {
        System.out.println("help         - Afiseaza aceasta lista de comenzi");
        System.out.println("add          - Adauga o noua pereche (masina, sofer)");
        System.out.println("check        - Verifica daca o masina este deja luata");
        System.out.println("remove       - Sterge o masina existenta din hashtable");
        System.out.println("getOwner     - Afiseaza proprietarul curent al masinii");
        System.out.println("listCarsOwner - Afiseaza masinile unui proprietar");
        System.out.println("reset         - Reseteaza lista.");
        System.out.println("totalCarsOwner - Afiseaza numarul de masini ale unui proprietar");
        System.out.println("totalRented  - Afiseaza numarul total de masini inchiriate");
        System.out.println("quit         - Inchide aplicatia");
    }

    public void run() throws IOException {
        this.rentedCars = readRented();
        this.proprietar = readOwner();
        boolean quit = false;

        while (!quit) {
            System.out.println("Asteapta comanda: (help - Afiseaza lista de comenzi)");
            String command = sc.nextLine();
            switch (command) {
                case "help":
                    printCommandsList();
                    break;
                case "add":
                        rentCar(getPlateNo(), getOwnerName());
                        break;
                case "check":
                    try {
                        System.out.println(isCarRent(getPlateNo()));
                        break;
                    } catch (IsCarRentException e){
                        System.out.println(e.getMessage());
                    }finally {
                        break;
                    }

                case "remove":

                        returnCar(getPlateNo());
                        break;

                case "getOwner":
                    try {
                        System.out.println(getCarRent(getPlateNo()));
                        break;
                    }catch (WrongCarsException e){
                        System.out.println("Introdu un numar de inmatriculare valid");
                        sc.nextLine();

                    }
                case "listCarsOwner":
                    System.out.println(RentedCars.getCarsList(getOwnerName()));
                    break;
                case "reset":
                    this.rentedCars = new HashMap<String,String>(100,0.5f);
                    this.proprietar = new HashMap<String, RentedCars>(2,0.5f);
                case "totalCarsOwner":
                    System.out.println(RentedCars.getCarsNo());
                    break;
                case "totalRented":
                    try {
                        System.out.println(totalRented());
                    }catch (NullPointerException e){
                        System.out.println("Nu exista proprietarul!");
                    }
                    break;
                case "quit":
                    writeRentedBinary(rentedCars);
                    writeOwnedBinary(proprietar);
                    System.out.println("Aplicatia se inchide...");
                    quit = true;
                    break;
                default:
                    System.out.println("Unknown command. Choose from:");
                    printCommandsList();
            }
        }
    }

    public static void main(String[] args) throws WrongCarsException, IOException {

        // create and run an instance (for test purpose)
        new CarRentalSystem().run();

    }
}
