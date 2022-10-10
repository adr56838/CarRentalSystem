package devmindJava.Curs26;

import java.util.ArrayList;

public class RentedCars {
    static ArrayList<String> carsList;

    public RentedCars(){
        carsList = new ArrayList<>();
    }
    public void addCar(String plateNo){
        if(carsList.contains(plateNo)){
            System.out.println("Masina este asociata unui proprietar");
        }
        carsList.add(plateNo);
    }

    public void removeCar(String plateNo){
        if(!carsList.contains(plateNo)){
            System.out.println("Masina este disponibila");
        }
        carsList.remove(plateNo);
    }
    static int getCarsNo(){
        return carsList.size();
    }

    static ArrayList<String> getCarsList(String ownerName){

        return carsList;
    }
}
